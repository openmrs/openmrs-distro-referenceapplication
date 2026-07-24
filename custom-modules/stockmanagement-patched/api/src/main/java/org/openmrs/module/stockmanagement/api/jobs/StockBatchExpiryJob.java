package org.openmrs.module.stockmanagement.api.jobs;

import org.apache.commons.lang.*;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationTag;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.LocationService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.StockLocationTags;
import org.openmrs.module.stockmanagement.api.Privileges;
import org.openmrs.module.stockmanagement.api.Roles;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.utils.*;
import org.openmrs.module.stockmanagement.api.utils.StringUtils;
import org.openmrs.notification.Alert;
import org.openmrs.notification.Template;
import org.openmrs.scheduler.tasks.AbstractTask;
import org.openmrs.util.OpenmrsConstants;

import javax.mail.Session;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StockBatchExpiryJob extends AbstractTask {
	
	private static AtomicBoolean isAlreadyRunning = new AtomicBoolean(false);
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private Session session = null;
	
	@Override
	public void execute() {
		if (isAlreadyRunning.get()) {
			log.debug("Stock batch job is already running");
			return;
		}
		try {
			if (isAlreadyRunning.getAndSet(true)) {
				log.debug("Stock batch job is already running");
				return;
			}
			executeInternal();
		}
		catch (Exception exception) {
			log.error("Error occurred while executing stock batch job");
			log.error(exception);
		}
		finally {
			isAlreadyRunning.set(false);
		}
	}
	
	protected void executeInternal() {
        if (!GlobalProperties.isStockBatchJobEnabled()) {
            log.debug("Stock batch job is not enabled under settings");
            return;
        }

        log.debug("Fetching the batch numbers due for notification");
        StockManagementService stockManagementService = Context.getService(StockManagementService.class);

        Integer defaultExpNotificationPeriod = GlobalProperties.getStockBatchDefaultExpiryNotificationNoticePeriod();
        Map<Integer, StockBatchDTO> stockBatchNumbers = stockManagementService.getExpiringStockBatchesDueForNotification(defaultExpNotificationPeriod)
                .stream().collect(Collectors.toMap(StockBatchDTO::getId, p -> p));
        if (stockBatchNumbers.isEmpty()) {
            log.debug("No stock batch numbers found");
            return;
        }

        // Remove stock batch numbers that do not have any stock
        Map<Integer, List<StockItemInventory>> stockBatchBalance = new HashMap<>();
        removeItemsWithoutStock(stockBatchNumbers, stockBatchBalance, stockManagementService);
        if (stockBatchNumbers.isEmpty()) {
            log.debug("No stock batch numbers with balance");
            return;
        }

        Map<Integer, Location> locationMap = new HashMap<>();
        Map<Integer, String> stockItemNameMap = new HashMap<>();
        Map<Integer, PartyDTO> partyDTOLocationMap = new HashMap<>();
        Map<Integer, PartyDTO> locationPartyDTOMap = new HashMap<>();
        Map<Integer, Boolean> batchNumbersNotified = new HashMap<>();

        List<Integer> mainStoreLocationIds = new ArrayList<>();
        {
            LocationService locationService = Context.getLocationService();
            LocationTag mainStore = locationService.getLocationTagByName(StockLocationTags.MAIN_STORE_LOCATION_TAG);
            if (mainStore != null) {
                List<Location> mainStoreLocations = locationService.getLocationsHavingAnyTag(Arrays.asList(mainStore));
                mainStoreLocationIds = mainStoreLocations.stream().map(p -> p.getLocationId()).collect(Collectors.toList());
                mainStoreLocations.forEach(p -> locationMap.putIfAbsent(p.getId(), p));
            }
        }

        log.debug("Getting the users to notify");
        Map<Integer, List<Integer>> usersToNotify = getUsersToBeNotified(mainStoreLocationIds, partyDTOLocationMap, stockBatchBalance, stockManagementService);
        if (usersToNotify.isEmpty() || usersToNotify.entrySet().stream().allMatch(p -> p.getValue().isEmpty())) {
            log.debug("No person to notify for the stock batch number that are expiring");
            return;
        }
        partyDTOLocationMap.entrySet().forEach(p -> locationPartyDTOMap.putIfAbsent(p.getValue().getLocationId(), p.getValue()));

        log.debug("Processing alerts");
        Map<Pair<Integer, Integer>, List<StockItemInventory>> partyStockItemMap = stockBatchBalance.entrySet().stream().map(p -> p.getValue())
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(p -> new Pair<Integer, Integer>(p.getPartyId(), p.getStockItemId())
                ));
        Map<Integer, User> users = new HashMap<>();
        Map<Integer, List<Pair<Integer, Integer>>> userLocationsToNotify = processAlerts(stockBatchBalance, stockBatchNumbers, usersToNotify, mainStoreLocationIds, locationMap, partyDTOLocationMap, locationPartyDTOMap, stockItemNameMap, partyStockItemMap, users, batchNumbersNotified, stockManagementService);
        log.debug("Processing mail notifications");

        boolean sentEmails = processMailNotifications(stockBatchBalance, stockBatchNumbers, usersToNotify, mainStoreLocationIds, locationMap, partyDTOLocationMap, locationPartyDTOMap, stockItemNameMap, partyStockItemMap, users, userLocationsToNotify, batchNumbersNotified, stockManagementService);
        if (!batchNumbersNotified.isEmpty()) {
            stockManagementService.updateStockBatchExpiryNotificationDate(batchNumbersNotified.keySet(), new Date());
        }
    }
	
	private Map<Integer, List<Pair<Integer, Integer>>> processAlerts(Map<Integer, List<StockItemInventory>> stockBatchBalance,
                                                                     Map<Integer, StockBatchDTO> stockBatchNumbers,
                                                                     Map<Integer, List<Integer>> locationUsersToNotify,
                                                                     List<Integer> mainStoreLocationIds,
                                                                     Map<Integer, Location> locationMap,
                                                                     Map<Integer, PartyDTO> partyLocationDTOMap,
                                                                     Map<Integer, PartyDTO> locationPartyDTOMap,
                                                                     Map<Integer, String> stockItemNames,
                                                                     Map<Pair<Integer, Integer>, List<StockItemInventory>> partyStockItemMap,
                                                                     Map<Integer, User> users,
                                                                     Map<Integer, Boolean> batchNumbersNotified,
                                                                     StockManagementService stockManagementService) {
        Map<Integer, List<Pair<Integer, Integer>>> userLocationsToNotify = locationUsersToNotify.entrySet().stream().map(p -> {
            return p.getValue().stream().map(x -> new Pair<Integer, Integer>(x, p.getKey()));
        }).flatMap(Function.identity()).collect(Collectors.groupingBy(p -> p.getValue1()));
        ensureStockItemNamesLoaded(partyStockItemMap, stockItemNames, stockManagementService);
        Map<Integer, Alert> alertMap = new HashMap<>();
        for (Map.Entry<Integer, List<Pair<Integer, Integer>>> userLocationToNotify : userLocationsToNotify.entrySet()) {
            List<Integer> partyIds = userLocationToNotify.getValue().stream().map(p -> {
                PartyDTO partyDTO = locationPartyDTOMap.get(p.getValue2());
                return partyDTO != null ? partyDTO.getId() : null;
            }).filter(p -> p != null).collect(Collectors.toList());
            List<Map.Entry<Pair<Integer, Integer>, List<StockItemInventory>>> userPartyStockItemMap = partyStockItemMap.entrySet().stream().filter(p -> partyIds.contains(p.getKey().getValue1()))
                    .collect(Collectors.toList());
            if (userPartyStockItemMap.isEmpty()) {
                // Check if user has permissions to main store. They automatically get access to every other location
                if (userLocationToNotify.getValue().stream().anyMatch(p -> mainStoreLocationIds.contains(p.getValue2()))) {
                    userPartyStockItemMap = partyStockItemMap.entrySet().stream().collect(Collectors.toList());
                } else {
                    continue;
                }
            }
            int maxAlerts = 4;
            User user = users.getOrDefault(userLocationToNotify.getKey(), null);
            if (user == null) {
                user = Context.getUserService().getUser(userLocationToNotify.getKey());
                if (user == null) {
                    continue;
                }
                users.putIfAbsent(userLocationToNotify.getKey(), user);
            }

            for (Map.Entry<Pair<Integer, Integer>, List<StockItemInventory>> partyStockItems : userPartyStockItemMap) {
                if (maxAlerts == 0) break;
                PartyDTO partyDTO = partyLocationDTOMap.get(partyStockItems.getKey().getValue1());
                if (partyDTO == null) continue;
                for (StockItemInventory stockItemInventory : partyStockItems.getValue()) {
                    if (maxAlerts == 0) break;
                    Alert alert = alertMap.getOrDefault(stockItemInventory.getPartyStockItemBatchHashCode(), null);
                    if (alert == null) {
                        StockBatchDTO stockBatchDTO = stockBatchNumbers.getOrDefault(stockItemInventory.getStockBatchId(), null);
                        if (stockBatchDTO == null) continue;
                        String alertLine = String.format("Batch %1$s for %2$s at %3$s with %4$s %5$s expires on %6$s",
                                stockBatchDTO.getBatchNo(),
                                stockItemNames.getOrDefault(stockItemInventory.getStockItemId(), ""),
                                partyDTO.getName(),
                                NumberFormatUtil.qtyDisplayFormat(stockItemInventory.getQuantity()),
                                stockItemInventory.getQuantityUoM(),
                                DateUtil.formatDDMMMyyyy(stockBatchDTO.getExpiration())
                        );

                        alert = new Alert(alertLine, user);
                        alert.setDateToExpire(DateUtils.addDays(new Date(), 7));
                        Context.getAlertService().saveAlert(alert);
                        alertMap.putIfAbsent(stockItemInventory.getPartyStockItemBatchHashCode(), alert);
                    } else {
                        alert.addRecipient(user);
                        Context.getAlertService().saveAlert(alert);
                    }
                    batchNumbersNotified.putIfAbsent(stockItemInventory.getStockBatchId(), true);
                    maxAlerts--;
                }
            }
        }
        return userLocationsToNotify;
    }
	
	@SuppressWarnings({ "unchecked" })
	private boolean processMailNotifications(Map<Integer, List<StockItemInventory>> stockBatchBalance,
                                             Map<Integer, StockBatchDTO> stockBatchNumbers,
                                             Map<Integer, List<Integer>> locationUsersToNotify,
                                             List<Integer> mainStoreLocationIds,
                                             Map<Integer, Location> locationMap,
                                             Map<Integer, PartyDTO> partyLocationDTOMap,
                                             Map<Integer, PartyDTO> locationPartyDTOMap,
                                             Map<Integer, String> stockItemNames,
                                             Map<Pair<Integer, Integer>, List<StockItemInventory>> partyStockItemMap,
                                             Map<Integer, User> users,
                                             Map<Integer, List<Pair<Integer, Integer>>> userLocationsToNotify,
                                             Map<Integer, Boolean> batchNumbersNotified,
                                             StockManagementService stockManagementService) {
        if (!SmtpUtil.hasSmptHostSetup()) {
            log.error("Stock Batch Job: Not processing mail notifications since smtp settings are not setup");
            return false;
        }

        Template template = null;
        try {
            List<Template> templates = Context.getMessageService().getTemplatesByName("STOCK_MGMT_BATCH_EXPIRING");
            if (templates.isEmpty()) {
                log.debug("Template STOCK_MGMT_BATCH_EXPIRING not found");
                return false;
            }
            template = templates.get(0);
        } catch (Exception exception) {
            log.error(exception);
            return false;
        }

        session = SmtpUtil.getSession();
        String healthCenterName = stockManagementService.getHealthCenterName();
        String mailMoreInfoMessage = Context.getMessageSourceService().getMessage("stockmanagement.stockrule.mailmoreinfoexpiring");
        boolean sentAtleastOneEmail = false;


        List<Integer> usersWithMainStorePermissions = userLocationsToNotify.entrySet().stream().filter(p -> p.getValue().stream().anyMatch(x -> mainStoreLocationIds.contains(x.getValue2())))
                .map(p -> p.getKey()).distinct().collect(Collectors.toList());

        boolean hasProcessedMainStore = false;
        for (Map.Entry<Integer, List<Pair<Integer, Integer>>> userIdLocationMapping : userLocationsToNotify.entrySet()) {
            Map<Integer, Boolean> userBatchNumbersNotified = new HashMap<>();
            List<String> receipients = new ArrayList<>();
            boolean isProcessingAllLocations = false;
            User user = null;
            if (usersWithMainStorePermissions.contains(userIdLocationMapping.getKey())) {
                if (hasProcessedMainStore) continue;
                for (Integer userId : usersWithMainStorePermissions) {
                    user = users.getOrDefault(userId, null);
                    if (user == null) {
                        user = Context.getUserService().getUser(userId);
                        if (user == null) {
                            continue;
                        }
                    }

                    String emailAddress = stockManagementService.getUserEmailAddress(user);
                    if(org.apache.commons.lang.StringUtils.isBlank(emailAddress)){
                        continue;
                    }
                    receipients.add(emailAddress);
                    isProcessingAllLocations = true;
                }
                hasProcessedMainStore = true;
                if (receipients.isEmpty()) {
                    continue;
                }
            } else {
                user = users.getOrDefault(userIdLocationMapping.getKey(), null);
                if (user == null) {
                    user = Context.getUserService().getUser(userIdLocationMapping.getKey());
                    if (user == null) {
                        continue;
                    }
                }

                String emailAddress = stockManagementService.getUserEmailAddress(user);
                if(org.apache.commons.lang.StringUtils.isBlank(emailAddress)){
                    continue;
                }
                receipients.add(emailAddress);
            }

            List<Integer> partyIds = isProcessingAllLocations ? null : userIdLocationMapping.getValue().stream().map(p -> {
                PartyDTO partyDTO = locationPartyDTOMap.get(p.getValue2());
                return partyDTO != null ? partyDTO.getId() : null;
            }).filter(p -> p != null).collect(Collectors.toList());
            Collection<Map.Entry<Pair<Integer, Integer>, List<StockItemInventory>>> userPartyStockItemMap = isProcessingAllLocations ? partyStockItemMap.entrySet() :
                    partyStockItemMap.entrySet().stream().filter(p -> partyIds.contains(p.getKey().getValue1()))
                            .collect(Collectors.toList());
            if (userPartyStockItemMap.isEmpty()) {
                continue;
            }

            StringBuilder stringBuilder = new StringBuilder();
            int itemCount = 0;
            boolean hasMore = false;
            int locationCount = 0;
            // Locations inventory
            for (Map.Entry<Integer, List<Map.Entry<Pair<Integer, Integer>, List<StockItemInventory>>>> partyItems :
                    userPartyStockItemMap.stream().collect(Collectors.groupingBy(p -> p.getKey().getValue1())).entrySet()) {
                if (itemCount == 100) {
                    hasMore = true;
                    break;
                }
                PartyDTO partyDTO = partyLocationDTOMap.get(partyItems.getKey());
                if (partyDTO == null) continue;
                if (locationCount > 0) {
                    stringBuilder.append("<tr><td colspan='9' style='padding: 0.25rem;height:5px;border-bottom:0;'></td></tr>");
                }
                stringBuilder.append(String.format("<tr><td colspan='9' style='padding: 0.5rem 0.5rem 0.5rem 0.5rem; font-size: 95%%;border-bottom:solid 1px grey;'>Location: <b>%1s</b></td></tr>", partyDTO.getName()));
                // Stock items inventory
                int stockItemsCount = 0;
                for (Map.Entry<Pair<Integer, Integer>, List<StockItemInventory>> stockItem : partyItems.getValue()) {
                    int batchCount = stockItem.getValue().size();
                    stringBuilder.append("<tr>");
                    stringBuilder.append(String.format("<td  rowspan='%1$s' colspan='3'  valign='top' style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%%;border-bottom:solid 1px grey;border-right:solid 1px grey;'>%2$s</td>", stockItem.getValue().size(), stockItemNames.getOrDefault(stockItem.getKey().getValue2(), "")));

                    boolean isAfterRow = false;
                    for (StockItemInventory batchNumber : stockItem.getValue()) {
                        if (isAfterRow) {
                            stringBuilder.append("<tr>");
                        }

                        StockBatchDTO stockBatchDTO = stockBatchNumbers.getOrDefault(batchNumber.getStockBatchId(), null);
                        if (stockBatchDTO == null) continue;

                        stringBuilder.append("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%;border-bottom:solid 1px grey;'>&nbsp;&nbsp;&nbsp;</td>");
                        stringBuilder.append(String.format("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%%;border-bottom:solid 1px grey;border-right:solid 1px grey;text-align:right;'>%1s</td>", stockBatchDTO.getBatchNo()));
                        stringBuilder.append("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%;border-bottom:solid 1px grey;'>&nbsp;&nbsp;&nbsp;</td>");
                        stringBuilder.append(String.format("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%%;border-bottom:solid 1px grey;border-right:solid 1px grey;text-align:right;'>%1$s %2$s</td>", NumberFormatUtil.qtyDisplayFormat(batchNumber.getQuantity()), batchNumber.getQuantityUoM()));
                        stringBuilder.append("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%;border-bottom:solid 1px grey;'>&nbsp;&nbsp;&nbsp;</td>");
                        stringBuilder.append(String.format("<td style='padding: 0.2rem 0.5rem 0.2rem 0.5rem; font-size: 95%%;border-bottom:solid 1px grey;text-align:right;'>%1s</td>", DateUtil.formatDDMMMyyyy(stockBatchDTO.getExpiration())));

                        if (isAfterRow) {
                            stringBuilder.append("</tr>");
                        }
                        userBatchNumbersNotified.putIfAbsent(batchNumber.getStockBatchId(), true);
                        isAfterRow = true;
                        itemCount++;
                    }
                    stockItemsCount++;
                }
                locationCount++;
            }

            String moreInfo = "";
            if (hasMore) {
                moreInfo = String.format("<br>%1s<br/>", mailMoreInfoMessage);
            }

            String body = template.getTemplate()
                    .replace("%STOCK_ITEMS%", stringBuilder.toString())
                    .replace("%MORE_INFO%", moreInfo)
                    .replace("%CENTER_NAME%", healthCenterName)
                    .replace("%RECPT_NAME%", receipients.size() > 1 ? "Hello" : String.format("Dear <b>%1s %2s</b>", user.getFamilyName(), user.getGivenName()));
            try {
                SmtpUtil.sendEmail(template.getSubject().replace("%CENTER_NAME%", healthCenterName), body, session, receipients.toArray(new String[receipients.size()]));
                sentAtleastOneEmail = true;
                batchNumbersNotified.putAll(userBatchNumbersNotified);
            } catch (Exception exception) {
                log.error(exception);
                continue;
            }
        }
        return sentAtleastOneEmail;
    }
	
	private void ensureStockItemNamesLoaded(Map<Pair<Integer, Integer>, List<StockItemInventory>> partyStockItemMap, Map<Integer, String> stockItemNames, StockManagementService stockManagementService) {
        List<Integer> stockItemNamesToFetch = partyStockItemMap.keySet().stream().map(p -> p.getValue2()).distinct().filter(p -> !stockItemNames.containsKey(p)).collect(Collectors.toList());
        if (!stockItemNamesToFetch.isEmpty()) {
            stockItemNames.putAll(stockManagementService.getStockItemNames(stockItemNamesToFetch));
        }
    }
	
	private Map<Integer, List<Integer>> getUsersToBeNotified(List<Integer> mainStoreLocationIds, Map<Integer, PartyDTO> partyLocationDTOMap, Map<Integer, List<StockItemInventory>> stockBatchBalances, StockManagementService stockManagementService) {

        List<Role> rolesWithPrivileges = new ArrayList<>();
        UserService userService = Context.getUserService();
        Role role = userService.getRole(Roles.INVENTORY_MANAGER);
        if (role != null) rolesWithPrivileges.add(role);
        role = userService.getRole(Roles.INVENTORY_CLERK);
        if (role != null) rolesWithPrivileges.add(role);
        if (rolesWithPrivileges.isEmpty()) {
            rolesWithPrivileges = userService.getAllRoles().stream().filter(p -> p.hasPrivilege(Privileges.TASK_STOCKMANAGEMENT_STOCKOPERATIONS_MUTATE)).collect(Collectors.toList());
        }
        if (rolesWithPrivileges.isEmpty()) {
            return new HashMap<>();
        }

        // Take care of child roles
        List<String> rolesToUse = new ArrayList<>();
        Map<String, List<String>> roleChain = new HashMap<>();
        Map<Integer, List<Integer>> result = new HashMap<>();
        for (Role entry : rolesWithPrivileges) {
            String roleName = entry.getName();
            rolesToUse.add(roleName);
            if (roleChain.containsKey(roleName)) {
                List<String> chain = roleChain.get(roleName);
                if (chain != null) {
                    rolesToUse.addAll(chain);
                }
            } else {
                if (!entry.getRetired()) {
                    List<String> chain = entry.getAllChildRoles().stream().filter(p -> !p.getRetired()).map(p -> p.getRole()).collect(Collectors.toList());
                    roleChain.putIfAbsent(roleName, chain);
                    rolesToUse.addAll(chain);
                }
            }
        }
        rolesToUse = rolesToUse.stream().distinct().collect(Collectors.toList());
        if (rolesToUse.isEmpty()) return result;

        List<Integer> partyIds = stockBatchBalances.entrySet().stream().map(p -> p.getValue()).flatMap(Collection::stream).map(p -> p.getPartyId()).distinct().collect(Collectors.toList());
        PartySearchFilter partySearchFilter = new PartySearchFilter();
        partySearchFilter.setPartyIds(partyIds);
        List<PartyDTO> partyDTOList = stockManagementService.findParty(partySearchFilter).getData();
        partyDTOList.forEach(p -> partyLocationDTOMap.putIfAbsent(p.getId(), p));
        List<Integer> locationsToFetch = new ArrayList<>(mainStoreLocationIds);
        locationsToFetch.addAll(partyDTOList.stream().map(p -> p.getLocationId()).collect(Collectors.toList()));
        for (Integer recordToFetch : locationsToFetch) {
            if (!result.containsKey(recordToFetch)) {
                List<Integer> usersAssigned = stockManagementService.getActiveUsersAssignedForScope(recordToFetch, rolesToUse);
                result.putIfAbsent(recordToFetch, usersAssigned);
            }
        }

        return result;
    }
	
	private void removeItemsWithoutStock(Map<Integer, StockBatchDTO> stockBatchDTOs, Map<Integer, List<StockItemInventory>> stockBatchBalance, StockManagementService stockManagementService) {
        List<PartyDTO> allStockHoldingAreas = stockManagementService.getAllStockHoldingPartyList();
        if (allStockHoldingAreas.isEmpty()) {
            log.error("Found no locations tagged with tags for stock holding areas");
            stockBatchDTOs.clear();
            return;
        }

        int startIndex = 0;
        int batchSize = 100;
        boolean hasMoreBalanceToFetch = true;
        do {

            List<StockBatchDTO> batch = stockBatchDTOs.values().stream().skip(startIndex * batchSize).limit(batchSize).collect(Collectors.toList());
            if (batch.isEmpty()) break;
            List<StockItemInventory> inventory = stockManagementService.getStockBatchLocationInventory(batch.stream().map(p -> p.getId()).collect(Collectors.toList()));
            Map<Integer, List<StockItemInventory>> batchInventories = inventory.stream().collect(Collectors.groupingBy(StockItemInventory::getStockBatchId));
            List<StockBatchDTO> toRemove = new ArrayList<>();
            for (StockBatchDTO stockBatchDTO : batch) {
                List<StockItemInventory> batchInventory = batchInventories.getOrDefault(stockBatchDTO.getId(), null);
                if (batchInventory == null) {
                    toRemove.add(stockBatchDTO);
                    continue;
                }
                batchInventory.removeIf(p -> p.getQuantity().compareTo(BigDecimal.ZERO) <= 0);
                if (batchInventory.isEmpty()) {
                    toRemove.add(stockBatchDTO);
                }
                stockBatchBalance.putIfAbsent(stockBatchDTO.getId(), batchInventory);
            }
            toRemove.forEach(p -> stockBatchDTOs.remove(p.getId()));
            hasMoreBalanceToFetch = batch.size() >= batchSize;
            startIndex++;
        } while (hasMoreBalanceToFetch);
    }
}
