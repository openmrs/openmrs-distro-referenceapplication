package org.openmrs.module.stockmanagement.api.jobs;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.StockRuleCurrentQuantity;
import org.openmrs.module.stockmanagement.api.dto.StockRuleNotificationUser;
import org.openmrs.module.stockmanagement.api.utils.GlobalProperties;
import org.openmrs.module.stockmanagement.api.utils.NumberFormatUtil;
import org.openmrs.module.stockmanagement.api.utils.Pair;
import org.openmrs.module.stockmanagement.api.utils.SmtpUtil;
import org.openmrs.notification.Alert;
import org.openmrs.notification.Template;
import org.openmrs.scheduler.tasks.AbstractTask;

import javax.mail.Session;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StockRuleEvaluationJob extends AbstractTask {
	
	private static final AtomicBoolean isAlreadyRunning = new AtomicBoolean(false);
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private Session session = null;
	
	@Override
	public void execute() {
		if (isAlreadyRunning.get()) {
			log.debug("Stock rule job is already running");
			return;
		}
		try {
			if (isAlreadyRunning.getAndSet(true)) {
				log.debug("Stock rule job is already running");
				return;
			}
			executeInternal();
		}
		catch (Exception exception) {
			log.error("Error occurred while executing stock rule job");
			log.error(exception);
		}
		finally {
			isAlreadyRunning.set(false);
		}
	}
	
	protected void executeInternal() {
        if (!GlobalProperties.isStockRuleJobEnabled()) {
            log.debug("Stock rule job is not enabled under settings");
            return;
        }

        log.debug("Fetching the rules");
        StockManagementService stockManagementService = Context.getService(StockManagementService.class);
        Integer lastStockRuleId = 0;
        int batchSize = GlobalProperties.getStockRuleJobBatchSize().intValue();
        List<StockRuleNotificationUser> rules = new ArrayList<>();
        boolean hasMoreResults = false;
        do {
            List<StockRuleNotificationUser> rulesFetched = stockManagementService.getDueStockRules(lastStockRuleId, batchSize);
            rules.addAll(rulesFetched);
            hasMoreResults = rulesFetched.size() >= batchSize;
            if (hasMoreResults) {
                lastStockRuleId = rulesFetched.get(rulesFetched.size() - 1).getId();
            }
        } while (hasMoreResults);

        if (rules.isEmpty()) {
            log.debug("No stock rules found");
            return;
        }

        log.debug("Getting the users to notify");
        Map<Integer, List<Integer>> usersToNotify = getUsersToBeNotified(rules, stockManagementService);
        if (usersToNotify.isEmpty() || !usersToNotify.entrySet().stream().anyMatch(p -> p.getValue() != null)) {
            log.debug("No person to notify for the configured stock rules");
            processRulesNoRecipientsAtAll(rules, stockManagementService);
            return;
        }

        log.debug("Flagging rules without receiptients as evaluated");
        processRulesWithNoRecipients(rules, usersToNotify, stockManagementService);

        // Remove rules we are not updating
        rules.removeIf(p -> {
            List<Integer> alertUsers = usersToNotify.getOrDefault(p.getAlertRoleLocationHashCode(), null);
            List<Integer> mailUsers = usersToNotify.getOrDefault(p.getMailRoleLocationHashCode(), null);
            return (alertUsers == null && mailUsers == null);
        });

        log.debug("Calculating item balances 1: " + rules.size());
        Map<Integer, List<StockRuleCurrentQuantity>> stockItemLocationEnableDescendantsBalanceMap = null;
        {
            // Get stock item balances
            List<StockRuleCurrentQuantity> itemBalances =
                    rules.stream().filter(p -> p.getEnableDescendants() != null && p.getEnableDescendants()).map(p ->
                                    new StockRuleCurrentQuantity(p.getStockItemId(), p.getLocationId(), BigDecimal.ZERO, true)
                    ).distinct().collect(Collectors.toList());
            stockManagementService.setStockItemCurrentBalanceWithDescendants(itemBalances);


            log.debug("Calculating item balances 2" + rules.size());
            List<StockRuleCurrentQuantity> itemBalances2 =
                    rules.stream().filter(p -> p.getEnableDescendants() == null || !p.getEnableDescendants()).map(p ->
                                    new StockRuleCurrentQuantity(p.getStockItemId(), p.getLocationId(), BigDecimal.ZERO, false)
                    ).distinct().collect(Collectors.toList());
            stockManagementService.setStockItemCurrentBalanceWithoutDescendants(itemBalances2);
            itemBalances.addAll(itemBalances2);
            stockItemLocationEnableDescendantsBalanceMap = itemBalances.stream().collect(Collectors.groupingBy(StockRuleCurrentQuantity::getStockItemLocationEnableDescendantsHashCode));
        }

        // Process rules with enough quantities. The remaining rules don't have enough quantities
        log.debug("Skipping rules with enough stock");
        processRulesWithEnoughQuantities(rules, stockItemLocationEnableDescendantsBalanceMap, stockManagementService);

        if (rules.isEmpty()) {
            return;
        }

        Map<Integer, String> conceptNames = new HashMap<>();
        Map<Integer, String> stockItemNames = new HashMap<>();
        Map<Integer, String> locationNames = new HashMap<>();
        Map<Integer, User> users = new HashMap<>();

        log.debug("Preparing mapping of users to notify and rules");
        Map<Integer, List<Pair<Integer, Integer>>> userIdLocationRoleRuleMappings = usersToNotify.entrySet().stream()
                .filter(p -> p.getValue() != null)
                .map(p -> p.getValue().stream().map(x -> new Pair<Integer, Integer>(p.getKey(), x)))
                .flatMap(Function.identity()).distinct().collect(Collectors.groupingBy(p -> p.getValue2()));

        log.debug("Processing alerts");
        processAlerts(rules, usersToNotify, stockItemNames, locationNames, userIdLocationRoleRuleMappings, conceptNames, users, stockItemLocationEnableDescendantsBalanceMap, stockManagementService);

        log.debug("Processing mail notifications");
        processMailNotifications(rules, usersToNotify, stockItemNames, locationNames, userIdLocationRoleRuleMappings, conceptNames, users, stockItemLocationEnableDescendantsBalanceMap, stockManagementService);

        // update the rules next date their to do an evaluation
        rules.stream().collect(Collectors.groupingBy(StockRuleNotificationUser::getEvaluationFrequency))
                .forEach((evaluationFrequency, rulesForFrequency) -> {
                    updateStockRuleJobNextEvaluationDate(rulesForFrequency, evaluationFrequency.intValue(), stockManagementService);
                });

        // update the rules next date a notification is to be resent
        rules.stream().collect(Collectors.groupingBy(StockRuleNotificationUser::getActionFrequency))
                .forEach((actionFrequency, rulesForFrequency) -> {
                    updateStockRuleJobNextActionDate(rulesForFrequency, actionFrequency.intValue(), stockManagementService);
                });

    }
	
	@SuppressWarnings({ "unchecked" })
	private void processMailNotifications(List<StockRuleNotificationUser> rules,
                                          Map<Integer, List<Integer>> usersToNotify,
                                          Map<Integer, String> stockItemNames,
                                          Map<Integer, String> locationNames,
                                          Map<Integer, List<Pair<Integer, Integer>>> userIdLocationRoleRuleMappings,
                                          Map<Integer, String> conceptNames,
                                          Map<Integer, User> users,
                                          Map<Integer, List<StockRuleCurrentQuantity>> stockItemLocationEnableDescendantsBalanceMap,
                                          StockManagementService stockManagementService) {
        if (!SmtpUtil.hasSmptHostSetup()) {
            log.error("Stock Rule Job: Not processing mail notifications since smtp settings are not setup");
            return;
        }

        Template template = null;
        try {
            List<Template> templates = Context.getMessageService().getTemplatesByName("STOCK_MGMT_QTY_BELOW_THRESHOLD");
            if (templates.isEmpty()) {
                log.debug("Template STOCK_MGMT_QTY_BELOW_THRESHOLD not found");
                return;
            }
            template = templates.get(0);
        } catch (Exception exception) {
            log.error(exception);
            return;
        }

        session = SmtpUtil.getSession();
        String healthCenterName = stockManagementService.getHealthCenterName();
        String mailMoreInfoMessage = Context.getMessageSourceService().getMessage("stockmanagement.stockrule.mailmoreinfobelowthreshold");
        for (Map.Entry<Integer, List<Pair<Integer, Integer>>> userIdLocationRoleRuleMapping : userIdLocationRoleRuleMappings.entrySet()) {
            User user = users.getOrDefault(userIdLocationRoleRuleMapping.getKey(), null);
            if (user == null) {
                user = Context.getUserService().getUser(userIdLocationRoleRuleMapping.getKey());
                if (user == null) {
                    continue;
                }
            }

            String emailAddress = stockManagementService.getUserEmailAddress(user);
            if(StringUtils.isBlank(emailAddress)){
                continue;
            }

            List<StockRuleNotificationUser> rulesToAlertTheUserAbout = rules.stream().filter(p -> userIdLocationRoleRuleMapping.getValue().stream().anyMatch(x -> x.getValue1().equals(p.getMailRoleLocationHashCode())))
                    .sorted(Comparator.comparing(StockRuleNotificationUser::getLocationId)
                            .thenComparing(StockRuleNotificationUser::getStockItemId)).collect(Collectors.toList());
            if (rulesToAlertTheUserAbout.isEmpty()) continue;

            ensureStockItemNamesLoaded(rulesToAlertTheUserAbout, stockItemNames, stockManagementService);
            ensureConceptNamesLoaded(rulesToAlertTheUserAbout, conceptNames, stockManagementService);
            ensureLocationNamesLoaded(rulesToAlertTheUserAbout, locationNames, stockManagementService);

            StringBuilder stringBuilder = new StringBuilder();
            int itemCount = Math.min(rulesToAlertTheUserAbout.size(), 30);
            for (StockRuleNotificationUser rule : rulesToAlertTheUserAbout) {
                if (itemCount == 0) {
                    break;
                }
                BigDecimal quantityRule = rule.getQuantity().divide(rule.getFactor(), 2, BigDecimal.ROUND_HALF_EVEN);
                List<StockRuleCurrentQuantity> quantityCurrent = stockItemLocationEnableDescendantsBalanceMap.getOrDefault(rule.getStockItemLocationEnableDescendantsHashCode(), null);
                BigDecimal currentQuantity = BigDecimal.ZERO;
                if (quantityCurrent != null) {
                    currentQuantity = quantityCurrent.get(0).getQuantity().divide(rule.getFactor(), 2, BigDecimal.ROUND_HALF_EVEN);
                }

                if (itemCount > 1) {
                    stringBuilder.append("<tr>");
                    stringBuilder.append(String.format("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%%;border-bottom:solid 1px grey;border-right:solid 1px grey;'>%1s</td>", locationNames.getOrDefault(rule.getLocationId(), "")));
                    stringBuilder.append("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%;border-bottom:solid 1px grey;'>&nbsp;</td>");
                    stringBuilder.append(String.format("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%%;border-bottom:solid 1px grey;border-right:solid 1px grey;'>%1s</td>", stockItemNames.getOrDefault(rule.getStockItemId(), "")));
                    stringBuilder.append("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%;border-bottom:solid 1px grey;'>&nbsp;</td>");
                    stringBuilder.append(String.format("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%%;border-bottom:solid 1px grey;border-right:solid 1px grey;'>%1s %2s</td>", NumberFormatUtil.qtyDisplayFormat(quantityRule), conceptNames.getOrDefault(rule.getPackagingConceptId(), "")));
                    stringBuilder.append("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%;border-bottom:solid 1px grey;'>&nbsp;</td>");
                    stringBuilder.append(String.format("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%%;border-bottom:solid 1px grey;'>%1s %2s</td>", NumberFormatUtil.qtyDisplayFormat(currentQuantity), conceptNames.getOrDefault(rule.getPackagingConceptId(), "")));
                    stringBuilder.append("</tr>");
                } else {
                    stringBuilder.append("<tr>");
                    stringBuilder.append(String.format("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%%;border-right:solid 1px grey;'>%1s</td>", locationNames.getOrDefault(rule.getLocationId(), "")));
                    stringBuilder.append("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%;'>&nbsp;</td>");
                    stringBuilder.append(String.format("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%%;border-right:solid 1px grey;'>%1s</td>", stockItemNames.getOrDefault(rule.getStockItemId(), "")));
                    stringBuilder.append("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%;'>&nbsp;</td>");
                    stringBuilder.append(String.format("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%%;border-right:solid 1px grey;'>%1s %2s</td>", NumberFormatUtil.qtyDisplayFormat(quantityRule), conceptNames.getOrDefault(rule.getPackagingConceptId(), "")));
                    stringBuilder.append("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%;'>&nbsp;</td>");
                    stringBuilder.append(String.format("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%%;'>%1s %2s</td>", NumberFormatUtil.qtyDisplayFormat(currentQuantity), conceptNames.getOrDefault(rule.getPackagingConceptId(), "")));
                    stringBuilder.append("</tr>");
                }
                itemCount--;
            }

            String moreInfo = "";
            if (rulesToAlertTheUserAbout.size() > 30) {
                moreInfo = String.format("<br>%1s<br/>", mailMoreInfoMessage);
            }

            String body = template.getTemplate()
                    .replace("%STOCK_ITEMS%", stringBuilder.toString())
                    .replace("%MORE_INFO%", moreInfo)
                    .replace("%CENTER_NAME%", healthCenterName)
                    .replace("%RECPT_NAME%", String.format("%1s %2s", user.getFamilyName(), user.getGivenName()));
            try {
                SmtpUtil.sendEmail(session, template.getSubject().replace("%CENTER_NAME%", healthCenterName), body, emailAddress);
            } catch (Exception exception) {
                log.error(exception);
                continue;
            }
        }
    }
	
	private void ensureStockItemNamesLoaded(List<StockRuleNotificationUser> rules, Map<Integer, String> stockItemNames, StockManagementService stockManagementService) {
        List<Integer> stockItemNamesToFetch = rules.stream().map(p -> p.getStockItemId()).distinct().filter(p -> !stockItemNames.containsKey(p)).collect(Collectors.toList());
        if (!stockItemNamesToFetch.isEmpty()) {
            stockItemNames.putAll(stockManagementService.getStockItemNames(stockItemNamesToFetch));
        }
    }
	
	private void ensureConceptNamesLoaded(List<StockRuleNotificationUser> rules, Map<Integer, String> conceptNames, StockManagementService stockManagementService) {
        List<Integer> conceptNamesToFetch = rules.stream().map(p -> p.getPackagingConceptId()).distinct().filter(p -> !conceptNames.containsKey(p)).collect(Collectors.toList());
        if (!conceptNamesToFetch.isEmpty()) {
            conceptNames.putAll(stockManagementService.getConceptNames(conceptNamesToFetch));
        }
    }
	
	private void ensureLocationNamesLoaded(List<StockRuleNotificationUser> rules, Map<Integer, String> locationNames, StockManagementService stockManagementService) {
        List<Integer> locationNamesToFetch = rules.stream().map(p -> p.getLocationId()).distinct().filter(p -> !locationNames.containsKey(p)).collect(Collectors.toList());
        if (!locationNamesToFetch.isEmpty()) {
            locationNames.putAll(stockManagementService.getLocationNames(locationNamesToFetch));
        }
    }
	
	private void processAlerts(List<StockRuleNotificationUser> rules,
                               Map<Integer, List<Integer>> usersToNotify,
                               Map<Integer, String> stockItemNames,
                               Map<Integer, String> locationNames,
                               Map<Integer, List<Pair<Integer, Integer>>> userIdLocationRoleRuleMappings,
                               Map<Integer, String> conceptNames,
                               Map<Integer, User> users,
                               Map<Integer, List<StockRuleCurrentQuantity>> stockItemLocationEnableDescendantsBalanceMap,
                               StockManagementService stockManagementService) {
        for (Map.Entry<Integer, List<Pair<Integer, Integer>>> userIdLocationRoleRuleMapping : userIdLocationRoleRuleMappings.entrySet()) {
            List<StockRuleNotificationUser> rulesToAlertTheUserAbout = rules.stream().filter(p -> userIdLocationRoleRuleMapping.getValue().stream().anyMatch(x -> x.getValue1().equals(p.getAlertRoleLocationHashCode())))
                    .collect(Collectors.toList());
            if (rulesToAlertTheUserAbout.isEmpty()) continue;
            int maxAlerts = 4;
            if (rulesToAlertTheUserAbout.stream().limit(maxAlerts).anyMatch(p -> p.getAlert() == null)) {
                ensureStockItemNamesLoaded(rulesToAlertTheUserAbout, stockItemNames, stockManagementService);
                ensureConceptNamesLoaded(rulesToAlertTheUserAbout, conceptNames, stockManagementService);
                ensureLocationNamesLoaded(rulesToAlertTheUserAbout, locationNames, stockManagementService);
            }

            User user = users.getOrDefault(userIdLocationRoleRuleMapping.getKey(), null);
            if (user == null) {
                user = Context.getUserService().getUser(userIdLocationRoleRuleMapping.getKey());
                if (user == null) {
                    continue;
                }
                users.putIfAbsent(userIdLocationRoleRuleMapping.getKey(), user);
            }

            for (StockRuleNotificationUser rule : rulesToAlertTheUserAbout) {
                if (maxAlerts == 0) break;
                if (rule.getAlert() == null) {
                    BigDecimal quantityRule = rule.getQuantity().divide(rule.getFactor(), 2, BigDecimal.ROUND_HALF_EVEN);
                    List<StockRuleCurrentQuantity> quantityCurrent = stockItemLocationEnableDescendantsBalanceMap.getOrDefault(rule.getStockItemLocationEnableDescendantsHashCode(), null);
                    BigDecimal currentQuantity = BigDecimal.ZERO;
                    if (quantityCurrent != null) {
                        currentQuantity = quantityCurrent.get(0).getQuantity().divide(rule.getFactor(), 2, BigDecimal.ROUND_HALF_EVEN);
                    }

                    String alertLine = String.format("Stock alert: %1$s at %2$s currently %3$s %4$s at or below threshold %5$s %4$s",
                            stockItemNames.getOrDefault(rule.getStockItemId(), ""),
                            locationNames.getOrDefault(rule.getLocationId(), ""),
                            NumberFormatUtil.qtyDisplayFormat(currentQuantity),
                            conceptNames.getOrDefault(rule.getPackagingConceptId(), ""),
                            NumberFormatUtil.qtyDisplayFormat(quantityRule)
                    );

                    Alert alert = new Alert(alertLine, user);
                    alert.setDateToExpire(DateUtils.addDays(new Date(), 7));
                    Context.getAlertService().saveAlert(alert);
                    rule.setAlert(alert);
                } else {
                    rule.getAlert().addRecipient(user);
                    Context.getAlertService().saveAlert(rule.getAlert());
                }
                maxAlerts--;
            }
        }
    }
	
	private void processRulesWithEnoughQuantities(List<StockRuleNotificationUser> rules,
                                                  Map<Integer, List<StockRuleCurrentQuantity>> stockItemLocationEnableDescendantsBalanceMap,
                                                  StockManagementService stockManagementService) {
        List<StockRuleNotificationUser> rulesWithEnoughQuantites = rules.stream().filter(p -> {
            List<StockRuleCurrentQuantity> stockRuleCurrentQuantity = stockItemLocationEnableDescendantsBalanceMap.getOrDefault(p.getStockItemLocationEnableDescendantsHashCode(), null);
            return stockRuleCurrentQuantity != null && stockRuleCurrentQuantity.get(0).getQuantity().compareTo(p.getQuantity()) > 0;
        }).collect(Collectors.toList());

        if (rulesWithEnoughQuantites.isEmpty()) {
            return;
        }

        rulesWithEnoughQuantites.stream().collect(Collectors.groupingBy(StockRuleNotificationUser::getEvaluationFrequency))
                .forEach((evaluationFrequency, rulesForFrequency) -> {
                    updateStockRuleJobNextEvaluationDate(rulesForFrequency, evaluationFrequency.intValue(), stockManagementService);
                });

        rules.removeAll(rulesWithEnoughQuantites);
    }
	
	private void processRulesNoRecipientsAtAll(List<StockRuleNotificationUser> rules,
                                               StockManagementService stockManagementService) {
        rules.stream().collect(Collectors.groupingBy(StockRuleNotificationUser::getEvaluationFrequency))
                .forEach((evaluationFrequency, rulesForFrequency) -> {
                    updateStockRuleJobNextEvaluationDate(rulesForFrequency, evaluationFrequency.intValue(), stockManagementService);
                });
    }
	
	private void processRulesWithNoRecipients(List<StockRuleNotificationUser> rules,
                                              Map<Integer, List<Integer>> usersToNotify,
                                              StockManagementService stockManagementService) {
        rules.stream().filter(p -> {
            List<Integer> alertUsers = usersToNotify.getOrDefault(p.getAlertRoleLocationHashCode(), null);
            p.setAlertUserIds(alertUsers);
            List<Integer> mailUsers = usersToNotify.getOrDefault(p.getMailRoleLocationHashCode(), null);
            p.setMailUserIds(mailUsers);
            return (alertUsers == null && mailUsers == null);
        }).collect(Collectors.groupingBy(StockRuleNotificationUser::getEvaluationFrequency))
                .forEach((evaluationFrequency, rulesForFrequency) -> {
                    updateStockRuleJobNextEvaluationDate(rulesForFrequency, evaluationFrequency.intValue(), stockManagementService);
                });
    }
	
	private Map<Integer, List<Integer>> getUsersToBeNotified(List<StockRuleNotificationUser> rules, StockManagementService stockManagementService) {
        // Get users assigned the scope of the role and location.
        Map<Integer, Pair<Integer, List<String>>> roleLocationUsersToNotify = new HashMap<>();
        for (StockRuleNotificationUser stockRuleNotificationUser : rules) {
            if (stockRuleNotificationUser.getAlertRole() != null && !roleLocationUsersToNotify.containsKey(stockRuleNotificationUser.getAlertRoleLocationHashCode())) {
                roleLocationUsersToNotify.putIfAbsent(stockRuleNotificationUser.getAlertRoleLocationHashCode(),
                        new Pair<>(stockRuleNotificationUser.getLocationId(), new ArrayList<>(Arrays.asList(stockRuleNotificationUser.getAlertRole()))));
            }

            if (stockRuleNotificationUser.getMailRole() != null && !roleLocationUsersToNotify.containsKey(stockRuleNotificationUser.getMailRoleLocationHashCode())) {
                roleLocationUsersToNotify.putIfAbsent(stockRuleNotificationUser.getMailRoleLocationHashCode(),
                        new Pair<>(stockRuleNotificationUser.getLocationId(), new ArrayList<>(Arrays.asList(stockRuleNotificationUser.getMailRole()))));
            }
        }

        // Take care of child roles
        if (roleLocationUsersToNotify.isEmpty()) return new HashMap<>();
        Map<String, List<String>> roleChain = new HashMap<>();
        UserService userService = Context.getUserService();
        Map<Integer, List<Integer>> result = new HashMap<>();
        for (Map.Entry<Integer, Pair<Integer, List<String>>> entry : roleLocationUsersToNotify.entrySet()) {
            String roleName = entry.getValue().getValue2().get(0);
            if (roleChain.containsKey(roleName)) {
                List<String> chain = roleChain.get(roleName);
                if (chain != null) {
                    entry.getValue().getValue2().addAll(chain);
                } else {
                    result.put(entry.getKey(), null);
                }
            } else {
                Role role = userService.getRole(roleName);
                if (role.getRetired()) {
                    roleChain.putIfAbsent(roleName, null);
                    result.put(entry.getKey(), null);
                } else {
                    List<String> chain = role.getAllChildRoles().stream().filter(p -> !p.getRetired()).map(p -> p.getRole()).collect(Collectors.toList());
                    roleChain.putIfAbsent(roleName, chain);
                    entry.getValue().getValue2().addAll(chain);
                }
            }
        }

        // fetch the users who have been assigned the roles at that location.
        List<Map.Entry<Integer, Pair<Integer, List<String>>>> recordsToFetch = roleLocationUsersToNotify.entrySet().stream().filter(p -> !result.containsKey(p.getKey())).collect(Collectors.toList());
        if (recordsToFetch.isEmpty()) return result;

        for (Map.Entry<Integer, Pair<Integer, List<String>>> recordToFetch : recordsToFetch) {
            if (recordToFetch.getValue().getValue2().isEmpty()) {
                result.putIfAbsent(recordToFetch.getKey(), null);
            } else {
                List<Integer> usersAssigned = stockManagementService.getActiveUsersAssignedForScope(recordToFetch.getValue().getValue1(),
                        recordToFetch.getValue().getValue2());
                result.putIfAbsent(recordToFetch.getKey(), usersAssigned.isEmpty() ? null : usersAssigned);
            }
        }

        return result;
    }
	
	private void updateStockRuleJobNextEvaluationDate(List<StockRuleNotificationUser> ruleNotificationUsers, int evaluationFrequency, StockManagementService stockManagementService) {
        boolean hasMoreRecords = true;
        int updateBatchSize = 100;
        int startIndex = 0;
        do {
            List<Integer> batch = ruleNotificationUsers.stream().skip(startIndex * updateBatchSize).map(p -> p.getId()).limit(updateBatchSize).collect(Collectors.toList());
            if (batch.isEmpty()) {
                break;
            }
            stockManagementService.updateStockRuleJobNextEvaluationDate( batch, DateUtils.addMinutes(new Date(), evaluationFrequency));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            hasMoreRecords = batch.size() >= updateBatchSize;
            startIndex++;
        }
        while (hasMoreRecords);
    }
	
	private void  updateStockRuleJobNextActionDate(List<StockRuleNotificationUser> ruleNotificationUsers, int actionFrequency, StockManagementService stockManagementService) {
        boolean hasMoreRecords = true;
        int updateBatchSize = 100;
        int startIndex = 0;
        do {
            List<Integer> batch = ruleNotificationUsers.stream().skip(startIndex * updateBatchSize).map(p -> p.getId()).limit(updateBatchSize).collect(Collectors.toList());
            if (batch.isEmpty()) {
                break;
            }
            stockManagementService.updateStockRuleJobNextActionDate(batch, DateUtils.addMinutes(new Date(), actionFrequency));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            hasMoreRecords = batch.size() >= updateBatchSize;
            startIndex++;
        }
        while (hasMoreRecords);
    }
}
