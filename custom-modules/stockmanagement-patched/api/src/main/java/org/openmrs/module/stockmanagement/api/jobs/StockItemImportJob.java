package org.openmrs.module.stockmanagement.api.jobs;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBaseBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.CSVReaderHeaderAwareBuilder;
import com.opencsv.RFC4180Parser;
import com.opencsv.RFC4180ParserBuilder;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Drug;
import org.openmrs.api.context.Context;
import org.openmrs.module.stockmanagement.api.StockManagementService;
import org.openmrs.module.stockmanagement.api.dto.*;
import org.openmrs.module.stockmanagement.api.model.StockItem;
import org.openmrs.module.stockmanagement.api.model.StockItemPackagingUOM;
import org.openmrs.module.stockmanagement.api.model.StockItemReference;
import org.openmrs.module.stockmanagement.api.model.StockSource;
import org.openmrs.module.stockmanagement.api.utils.GlobalProperties;

import java.io.File;
import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StockItemImportJob {
	
	private Path file;
	
	private boolean hasHeader;
	
	private ImportResult result = new ImportResult();
	
	private int batchSize = 50;
	
	private int DRUG_ID = 0;
	
	private int CONCEPT_ID = 1;
	
	private int EXPIRES = 2;
	
	private int EXPIRY_NOTICE = 3;
	
	private int CATEGORY = 4;
	
	private int DISPENSING_UNIT = 5;
	
	private int DISPENSING_PUOM = 6;
	
	private int PACK_SIZE_FOR_DISPENSING_PUOM = 7;
	
	private int COMMON_NAME = 8;
	
	private int ABBREVIATION = 9;
	
	private int PREFERRED_VENDOR = 10;
	
	private int REORDER_LEVEL = 11;
	
	private int REORDER_LVEL_PUOM = 12;
	
	private int PURCHASE_PRICE = 13;
	
	private int PURCHASE_PRICE_PUOM = 14;
	
	private int VENDOR_REFERENCE_ITEM_CODE = 15;
	
	private static Pattern NON_ASCII_PATTERN = Pattern.compile("[^A-Za-z0-9]");
	
	public StockItemImportJob(Path file, boolean hasHeader) {
        this.file = file;
        this.hasHeader = hasHeader;
        result.setErrors(new ArrayList<>());
    }
	
	public int getBatchSize() {
		return batchSize;
	}
	
	public void setBatchSize(int batchSize) {
		if (batchSize > 0) {
			this.batchSize = batchSize;
		}
	}
	
	private boolean isBlank(String value) {
		return StringUtils.isBlank(value) || value.toLowerCase().equals("null");
	}
	
	private Object validateLine(String[] line) {
        if (line == null || line.length == 0) return null;
        Object[] objects = new Object[16];
        List<String> errors = new ArrayList<>();
        if (line.length < 3) {
            errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.minimumfields"));
            return errors;
        }
        if (!isBlank(line[DRUG_ID])) {
            try {
                Integer value = Integer.parseInt(line[DRUG_ID]);
                if (value > 0) {
                    objects[DRUG_ID] = value;
                } else {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.drugidpositive"));
                }
            } catch (Exception ex) {
                errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.drugidnumber"));
            }
        }

        if (objects[DRUG_ID] == null && !isBlank(line[CONCEPT_ID])) {
            try {
                Integer value = Integer.parseInt(line[CONCEPT_ID]);
                if (value > 0) {
                    objects[CONCEPT_ID] = value;
                } else {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.conceptidpositive"));
                }
            } catch (Exception ex) {
                errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.conceptidnumber"));
            }
        } else if (objects[DRUG_ID] == null) {
            errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.drugconceptidrequired"));
        }

        if (!isBlank(line[EXPIRES])) {
            Boolean value = null;
            String token = line[EXPIRES].toLowerCase();
            if (token.equals("1") || token.equals("yes")) {
                value = true;
            } else if (token.equals("0") || token.equals("no")) {
                value = false;
            }
            if (value != null) {
                objects[EXPIRES] = value;
            } else {
                errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.expiresinvalid"));
            }
        }

        if (!isBlank(line[EXPIRY_NOTICE])) {
            try {
                Integer value = Integer.parseInt(line[EXPIRY_NOTICE]);
                if (value > 0) {
                    objects[EXPIRY_NOTICE] = value;
                } else {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.expirynoticepositive"));
                }
            } catch (Exception ex) {
                errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.expirynoticenumber"));
            }
        }

        if (!isBlank(line[CATEGORY])) {
            try {
                Integer value = Integer.parseInt(line[CATEGORY]);
                if (value > 0) {
                    objects[CATEGORY] = value;
                } else {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.categorypositive"));
                }
            } catch (Exception ex) {
                errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.categorynumber"));
            }
        }

        if (line.length > DISPENSING_UNIT && !isBlank(line[DISPENSING_UNIT])) {
            try {
                Integer value = Integer.parseInt(line[DISPENSING_UNIT]);
                if (value > 0) {
                    objects[DISPENSING_UNIT] = value;
                } else {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.dispenseunitpositive"));
                }
            } catch (Exception ex) {
                errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.dispenseunitnumber"));
            }
        }

        if (line.length > DISPENSING_PUOM && !isBlank(line[DISPENSING_PUOM])) {
            try {
                Integer value = Integer.parseInt(line[DISPENSING_PUOM]);
                if (value > 0) {
                    objects[DISPENSING_PUOM] = value;
                } else {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.dispensepuompositive"));
                }
            } catch (Exception ex) {
                errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.dispensepuomnumber"));
            }
        }

        if (line.length > PACK_SIZE_FOR_DISPENSING_PUOM && !isBlank(line[PACK_SIZE_FOR_DISPENSING_PUOM])) {
            try {
                BigDecimal value = new BigDecimal(line[PACK_SIZE_FOR_DISPENSING_PUOM]);
                if (value.compareTo(BigDecimal.ZERO) > 0) {
                    objects[PACK_SIZE_FOR_DISPENSING_PUOM] = value;
                } else {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.packsizepositive"));
                }
            } catch (Exception ex) {
                errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.packsizenumber"));
            }
        }

        if (objects[PACK_SIZE_FOR_DISPENSING_PUOM] != null && objects[DISPENSING_PUOM] == null) {
            errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.packsizeeanddispensinguomrequired"));
        }

        if (line.length > COMMON_NAME && !isBlank(line[COMMON_NAME])) {
            objects[COMMON_NAME] = line[COMMON_NAME];
        }

        if (line.length > ABBREVIATION && !isBlank(line[ABBREVIATION])) {
            objects[ABBREVIATION] = line[ABBREVIATION];
        }

        if (line.length > PREFERRED_VENDOR && !isBlank(line[PREFERRED_VENDOR])) {
            objects[PREFERRED_VENDOR] = line[PREFERRED_VENDOR];
        }


        if (line.length > VENDOR_REFERENCE_ITEM_CODE && !isBlank(line[VENDOR_REFERENCE_ITEM_CODE])) {
            objects[VENDOR_REFERENCE_ITEM_CODE] = line[VENDOR_REFERENCE_ITEM_CODE];
        }

        if (line.length > REORDER_LEVEL && !isBlank(line[REORDER_LEVEL])) {
            try {
                BigDecimal value = new BigDecimal(line[REORDER_LEVEL]);
                if (value.compareTo(BigDecimal.ZERO) >= 0) {
                    objects[REORDER_LEVEL] = value;
                } else {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.reorderlevelpositive"));
                }
            } catch (Exception ex) {
                errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.reorderlevelnumber"));
            }
        }

        if (line.length > REORDER_LVEL_PUOM && !isBlank(line[REORDER_LVEL_PUOM])) {
            try {
                Integer value = Integer.parseInt(line[REORDER_LVEL_PUOM]);
                if (value > 0) {
                    objects[REORDER_LVEL_PUOM] = value;
                } else {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.reorderleveluompositive"));
                }
            } catch (Exception ex) {
                errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.reorderleveluomnumber"));
            }
        }

        if ((objects[REORDER_LEVEL] != null || objects[REORDER_LVEL_PUOM] != null) && (objects[REORDER_LEVEL] == null || objects[REORDER_LVEL_PUOM] == null)) {
            errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.reorderlevelanduomrequired"));
        }

        if (line.length > PURCHASE_PRICE && !isBlank(line[PURCHASE_PRICE])) {
            try {
                BigDecimal value = new BigDecimal(line[PURCHASE_PRICE]);
                if (value.compareTo(BigDecimal.ZERO) >= 0) {
                    objects[PURCHASE_PRICE] = value;
                } else {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.purchasepricepositive"));
                }
            } catch (Exception ex) {
                errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.purchasepricenumber"));
            }
        }

        if (line.length > PURCHASE_PRICE_PUOM && !isBlank(line[PURCHASE_PRICE_PUOM])) {
            try {
                Integer value = Integer.parseInt(line[PURCHASE_PRICE_PUOM]);
                if (value > 0) {
                    objects[PURCHASE_PRICE_PUOM] = value;
                } else {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.purchasepriceuompositive"));
                }
            } catch (Exception ex) {
                errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.purchasepriceuomnumber"));
            }
        }

        if ((objects[PURCHASE_PRICE] != null || objects[PURCHASE_PRICE_PUOM] != null) && (objects[PURCHASE_PRICE] == null || objects[PURCHASE_PRICE_PUOM] == null)) {
            errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.purchasepriceanduomrequired"));
        }

        return errors.isEmpty() ? objects : errors;
    }
	
	@SuppressWarnings({ "unchecked" })
	private void updateStockItems(Map<Integer, Object[]> stockItems) {
        StockManagementService stockManagementService = Context.getService(StockManagementService.class);

        List<StockItemDTO> stockItemIds = stockManagementService.getExistingStockItemIds(stockItems.values().stream().map(p -> {
            StockItemSearchFilter.ItemGroupFilter itemGroupFilter = new StockItemSearchFilter.ItemGroupFilter();
            if (p[DRUG_ID] != null) {
                itemGroupFilter.setDrugId((Integer) p[DRUG_ID]);
                itemGroupFilter.setIsDrug(true);
            } else {
                itemGroupFilter.setConceptId((Integer) p[CONCEPT_ID]);
                itemGroupFilter.setIsDrug(false);
            }
            return itemGroupFilter;
        }).collect(Collectors.groupingBy(p -> p)).keySet());

        // Get the rowIds for existing stock items
        Map<Object, List<Object[]>> rowsToUpdate = stockItems.entrySet().stream().map(p -> new Object[]{p.getKey(),
                stockItemIds.stream().filter(x -> (p.getValue()[DRUG_ID] != null && p.getValue()[DRUG_ID].equals(x.getDrugId())) ||
                                (p.getValue()[DRUG_ID] == null && x.getDrugId() == null && p.getValue()[CONCEPT_ID].equals(x.getConceptId()))
                ).findFirst()
        }).filter(p -> ((Optional<?>) p[1]).isPresent()).collect(Collectors.groupingBy(p -> p[0]));

        List<Map.Entry<Integer, Object[]>> newStockItems = stockItems.entrySet().stream().filter(p -> !rowsToUpdate.containsKey(p.getKey())).collect(Collectors.toList());
        List<Map.Entry<Integer, Object[]>> rowsToCreate = new ArrayList<>();

        if (!newStockItems.isEmpty()) {
            // validate minimum information required to be created.
            for (Map.Entry<Integer, Object[]> entryToCreate : newStockItems) {
                List<String> errors = new ArrayList<>();
                if (entryToCreate.getValue()[EXPIRES] == null) {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.expiresrequired"));
                }
                if (entryToCreate.getValue()[DISPENSING_UNIT] == null) {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.dispenseunitrequired"));
                }
                if (entryToCreate.getValue()[DISPENSING_PUOM] == null) {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.dispensepuomrequired"));
                }
                if (entryToCreate.getValue()[PACK_SIZE_FOR_DISPENSING_PUOM] == null) {
                    errors.add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.packsizerequired"));
                }
                if (!errors.isEmpty()) {
                    result.getErrors().add(String.format("Row %1s: %2s", entryToCreate.getKey(), String.join(", ", errors)));
                    continue;
                }
                rowsToCreate.add(entryToCreate);
            }
        }

        if (rowsToUpdate.isEmpty() && rowsToCreate.isEmpty()) {
            return;
        }

        // prefetch the concepts and drugs
        List<Integer> drugIds = rowsToCreate.stream().map(p -> p.getValue()[DRUG_ID]).filter(p -> p != null).map(p -> (Integer) p).distinct().collect(Collectors.toList());
        List<Integer> conceptIds = stockItems.entrySet().stream().map(p -> Arrays.asList(
                p.getValue()[CONCEPT_ID],
                p.getValue()[DISPENSING_UNIT],
                p.getValue()[DISPENSING_PUOM],
                p.getValue()[REORDER_LVEL_PUOM],
                p.getValue()[PURCHASE_PRICE_PUOM],
                p.getValue()[CATEGORY]
        )).flatMap(Collection::stream).filter(p -> p != null).map(p -> (Integer) p).distinct().collect(Collectors.toList());

        Map<Integer, List<Drug>> drugs = stockManagementService.getDrugs(drugIds).stream().collect(Collectors.groupingBy(Drug::getDrugId));
        Map<Integer, List<Concept>> concepts = stockManagementService.getConcepts(conceptIds).stream().collect(Collectors.groupingBy(Concept::getConceptId));
        Map<String, StockSource> stockSources = new HashMap<>();
        Concept unknownConcept = null;

        Map<Integer, List<StockItem>> stockItemsToUpdate = null;
        Map<Integer, List<StockItemPackagingUOM>> stockItemPackagingUoms = null;
        Map<Integer, StockItem> prevStockItemDrugs = null;
        Map<Integer, StockItem> prevStockItemConcepts = null;

        if (!rowsToUpdate.isEmpty()) {
            stockItemsToUpdate = stockManagementService.getStockItems(stockItemIds.stream().map(p -> p.getId()).collect(Collectors.toList()))
                    .stream().collect(Collectors.groupingBy(StockItem::getId));

            // Prefetch the stockitempackaging uoms
            stockItemPackagingUoms = stockManagementService.getStockItemPackagingUOMs(
                    rowsToUpdate.entrySet().stream().map(p -> {
                        Integer stockItemId = ((Optional<StockItemDTO>) (p.getValue().get(0)[1])).get().getId();
                        Object[] updates = stockItems.get(p.getKey());
                        List<Integer> conceptIdsForUom = new ArrayList<Integer>();
                        if (DISPENSING_PUOM < updates.length && updates[DISPENSING_PUOM] != null) {
                            conceptIdsForUom.add((Integer) updates[DISPENSING_PUOM]);
                        }
                        if (REORDER_LVEL_PUOM < updates.length && updates[REORDER_LVEL_PUOM] != null) {
                            if (conceptIdsForUom.isEmpty() || !conceptIdsForUom.get(0).equals(updates[REORDER_LVEL_PUOM])) {
                                conceptIdsForUom.add((Integer) updates[REORDER_LVEL_PUOM]);
                            }
                        }

                        if (PURCHASE_PRICE_PUOM < updates.length && updates[PURCHASE_PRICE_PUOM] != null) {
                            if (conceptIdsForUom.isEmpty() || (!conceptIdsForUom.get(0).equals(updates[PURCHASE_PRICE_PUOM]) && (
                                    conceptIdsForUom.size() < 2 || !conceptIdsForUom.get(1).equals(updates[PURCHASE_PRICE_PUOM])
                            ))) {
                                conceptIdsForUom.add((Integer) updates[PURCHASE_PRICE_PUOM]);
                            }
                        }
                        if (conceptIdsForUom.isEmpty()) return null;
                        return new StockItemPackagingUOMSearchFilter.ItemGroupFilter(stockItemId, conceptIdsForUom);
                    }).filter(p -> p != null).collect(Collectors.toList())).stream().collect(Collectors.groupingBy(p -> p.getStockItem().getId()));
        }

        if (!rowsToCreate.isEmpty()) {
            prevStockItemDrugs = new HashMap<>();
            prevStockItemConcepts = new HashMap<>();
        }

        for (Map.Entry<Integer, Object[]> recordToProcess : stockItems.entrySet()) {
            Object[] valuesToProcess = null;
            boolean isNewRecord = true;
            Boolean isVeryNewRecordDrug = null;
            if (rowsToUpdate.containsKey(recordToProcess.getKey())) {
                valuesToProcess = recordToProcess.getValue();
                isNewRecord = false;
            } else {
                Optional<Map.Entry<Integer, Object[]>> rowFound = rowsToCreate.stream().filter(p -> p.getKey().equals(recordToProcess.getKey())).findFirst();
                if (rowFound.isPresent()) {
                    valuesToProcess = rowFound.get().getValue();
                }
            }
            if (valuesToProcess == null) {
                continue;
            }

            StockItem stockItem = null;
            Object[] updates = valuesToProcess;
            if (isNewRecord) {
                boolean isNew = true;
                if (updates[DRUG_ID] != null) {
                    stockItem = prevStockItemDrugs.getOrDefault(updates[DRUG_ID], null);
                    if (stockItem != null) {
                        isNew = false;
                    } else {
                        isVeryNewRecordDrug = true;
                        stockItem = new StockItem();
                        List<Drug> drugCollection = drugs.getOrDefault(updates[DRUG_ID], null);
                        if (drugCollection == null) {
                            result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(),
                                    String.format(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.drugnofound"),
                                            updates[DRUG_ID].toString())));
                            continue;
                        }
                        stockItem.setDrug(drugCollection.get(0));
                        stockItem.setConcept(stockItem.getDrug().getConcept());
                        stockItem.setIsDrug(true);
                    }
                } else if (updates[CONCEPT_ID] != null) {
                    stockItem = prevStockItemConcepts.getOrDefault(updates[CONCEPT_ID], null);
                    if (stockItem != null) {
                        isNew = false;
                    } else {
                        isVeryNewRecordDrug = false;
                        stockItem = new StockItem();
                        List<Concept> conceptCollection = concepts.getOrDefault(updates[CONCEPT_ID], null);
                        if (conceptCollection == null) {
                            result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(),
                                    String.format(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.conceptnofound"),
                                            updates[CONCEPT_ID].toString())));
                            continue;
                        }
                        stockItem.setConcept(conceptCollection.get(0));
                        stockItem.setIsDrug(false);
                    }
                }

                if (isNew) {
                    stockItem.setCreator(Context.getAuthenticatedUser());
                    stockItem.setDateCreated(new Date());
                }
            } else {
                List<Object[]> rowToUpdate = rowsToUpdate.get(recordToProcess.getKey());
                List<StockItem> stockItemCollection = stockItemsToUpdate.getOrDefault(((Optional<StockItemDTO>) (rowToUpdate.get(0)[1])).get().getId(), null);
                if (stockItemCollection != null) {
                    stockItem = stockItemCollection.get(0);
                }
            }

            if (stockItem == null) {
                result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(), Context.getMessageSourceService().getMessage("stockmanagement.importoperation.stockitemmismatch")));
                continue;
            }

            boolean saveDispensingStockItemPackagingUOM = false;
            BigDecimal packSizeToUpdate = null;
            StockItemPackagingUOM dispensingStockItemPackagingUOM = null;
            StockItemPackagingUOM dispensingUomToSet = null;
            StockItemPackagingUOM reorderLevelUomToSet = null;
            StockItemPackagingUOM purchasePriceUomToSet = null;
            BigDecimal reorderlevelToSet = null;
            BigDecimal purchasePriceToSet = null;
            Concept dispensingUnitToSet = null;
            StockSource stockSourceToSet = null;
            Concept categoryConceptToSet = null;
            Boolean expiresToSet = null;
            Integer expiryNoticeToSet = null;


            if (updates[EXPIRES] != null && !updates[EXPIRES].equals(Boolean.valueOf(stockItem.getHasExpiration()))) {
                expiresToSet = (Boolean) updates[EXPIRES];
            }

            if (updates[EXPIRY_NOTICE] != null && (stockItem.getExpiryNotice() == null || !updates[EXPIRY_NOTICE].equals(stockItem.getExpiryNotice()))) {
                expiryNoticeToSet = (Integer) updates[EXPIRY_NOTICE];
            }

            if (updates[CATEGORY] != null && (stockItem.getCategory() == null || !stockItem.getCategory().getId().equals(updates[CATEGORY]))) {
                List<Concept> conceptCollection = concepts.getOrDefault(updates[CATEGORY], null);
                if (conceptCollection == null) {
                    result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(),
                            String.format(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.conceptnofound"),
                                    updates[CATEGORY].toString())));
                    continue;
                }
               categoryConceptToSet = conceptCollection.get(0);
            }

            List<StockItemPackagingUOM> uoms = null;
            if(!isNewRecord) {
                uoms = stockItemPackagingUoms.getOrDefault(stockItem.getId(), null);
            }

            if (DISPENSING_UNIT < updates.length && updates[DISPENSING_UNIT] != null) {
                List<Concept> conceptCollection = concepts.getOrDefault(updates[DISPENSING_UNIT], null);
                if (conceptCollection == null) {
                    result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(),
                            String.format(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.conceptnofound"),
                                    updates[DISPENSING_UNIT].toString())));
                    continue;
                }
                if (stockItem.getDispensingUnit() == null || !stockItem.getDispensingUnit().getUuid().equals(conceptCollection.get(0).getUuid())) {
                    dispensingUnitToSet = conceptCollection.get(0);
                }
            }

            if (DISPENSING_PUOM < updates.length && updates[DISPENSING_PUOM] != null) {
                List<Concept> conceptCollection = concepts.getOrDefault(updates[DISPENSING_PUOM], null);
                if (conceptCollection == null) {
                    result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(),
                            String.format(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.conceptnofound"),
                                    updates[DISPENSING_PUOM].toString())));
                    continue;
                }

                BigDecimal packSize = null;
                if (PACK_SIZE_FOR_DISPENSING_PUOM < updates.length && updates[PACK_SIZE_FOR_DISPENSING_PUOM] != null) {
                    packSize = (BigDecimal) updates[PACK_SIZE_FOR_DISPENSING_PUOM];
                }

                if (uoms != null) {
                    Optional<StockItemPackagingUOM> uom = uoms.stream().filter(p -> p.getPackagingUom().getConceptId().equals((Integer) updates[DISPENSING_PUOM])).findFirst();
                    if (uom.isPresent()) {
                        dispensingStockItemPackagingUOM = uom.get();
                    }
                }

                if (dispensingStockItemPackagingUOM != null) {
                    if (packSize != null && packSize.compareTo(dispensingStockItemPackagingUOM.getFactor()) != 0) {
                        packSizeToUpdate = packSize;
                        saveDispensingStockItemPackagingUOM = true;
                    }
                } else {
                    if (packSize == null) {
                        result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(), Context.getMessageSourceService().getMessage("stockmanagement.importoperation.packsizeeanddispensinguomrequired")));
                        continue;
                    }
                    dispensingStockItemPackagingUOM = new StockItemPackagingUOM();
                    dispensingStockItemPackagingUOM.setStockItem(stockItem);
                    dispensingStockItemPackagingUOM.setPackagingUom(conceptCollection.get(0));
                    dispensingStockItemPackagingUOM.setFactor(packSize);
                    dispensingStockItemPackagingUOM.setCreator(Context.getAuthenticatedUser());
                    dispensingStockItemPackagingUOM.setDateCreated(new Date());
                    dispensingUomToSet = dispensingStockItemPackagingUOM;
                    saveDispensingStockItemPackagingUOM = true;
                }
            }

            if (COMMON_NAME < updates.length && updates[COMMON_NAME] != null) {
                stockItem.setCommonName((String) updates[COMMON_NAME]);
            }

            if (ABBREVIATION < updates.length && updates[ABBREVIATION] != null) {
                stockItem.setAcronym((String) updates[ABBREVIATION]);
            }

            if (PREFERRED_VENDOR < updates.length && updates[PREFERRED_VENDOR] != null) {
                String preferredVendorName = ((String) updates[PREFERRED_VENDOR]).trim();
                if(preferredVendorName.length() > 255){
                    result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(),
                            Context.getMessageSourceService().getMessage("stockmanagement.importoperation.exceeds255")));
                    continue;
                }

                String cacheKey = NON_ASCII_PATTERN.matcher(preferredVendorName).replaceAll("").toLowerCase();
                if (isBlank(cacheKey)) {
                    cacheKey = preferredVendorName.toLowerCase();
                }
                StockSource stockSource = stockSources.getOrDefault(cacheKey, null);
                if (stockSource == null) {
                    StockSourceSearchFilter filter = new StockSourceSearchFilter();
                    filter.setIncludeVoided(true);
                    filter.setTextSearch(preferredVendorName);
                    List<StockSource> stockSourcesInDb = stockManagementService.findStockSources(filter).getData();
                    if (!stockSourcesInDb.isEmpty()) {
                        stockSource = stockSourcesInDb.get(0);
                        for (StockSource stockSourceInDb : stockSourcesInDb) {
                            String nameNomalized = NON_ASCII_PATTERN.matcher(stockSourceInDb.getName()).replaceAll("").toLowerCase();
                            String acronymNomalized = NON_ASCII_PATTERN.matcher(stockSourceInDb.getAcronym()).replaceAll("").toLowerCase();
                            stockSource = stockSourceInDb;
                            if (nameNomalized.equals(acronymNomalized)) {
                                stockSources.put(nameNomalized, stockSource);
                            } else {
                                stockSources.put(nameNomalized, stockSource);
                                stockSources.put(acronymNomalized, stockSource);
                            }
                        }
                    }
                }

                if (stockSource == null) {
                    if (unknownConcept == null) {
                        String unknownConceptId = GlobalProperties.getUnknownConceptId();
                        if (unknownConceptId != null) {
                            if(StringUtils.isNumeric(unknownConceptId)){
                                unknownConcept = Context.getConceptService().getConcept(unknownConceptId);
                            }else{
                                unknownConcept = Context.getConceptService().getConceptByUuid(unknownConceptId);
                            }
                        }
                    }
                    if (unknownConcept == null) {
                        result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(),
                                Context.getMessageSourceService().getMessage("stockmanagement.importoperation.unknownconceptnofoundstocksource")));
                        continue;
                    }

                    stockSource = new StockSource();
                    stockSource.setName(preferredVendorName);
                    stockSource.setAcronym(preferredVendorName);
                    stockSource.setSourceType(unknownConcept);
                    stockSource.setCreator(Context.getAuthenticatedUser());
                    stockSource.setDateCreated(new Date());
                    stockManagementService.saveStockSource(stockSource);
                    stockSources.put(cacheKey, stockSource);
                }

                if (stockItem.getPreferredVendor() == null || !stockItem.getPreferredVendor().getUuid().equals(stockSource.getUuid())) {
                    stockSourceToSet = stockSource;
                }
            }

            if (REORDER_LVEL_PUOM < updates.length && updates[REORDER_LVEL_PUOM] != null) {
                List<Concept> conceptCollection = concepts.getOrDefault(updates[REORDER_LVEL_PUOM], null);
                if (conceptCollection == null) {
                    result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(),
                            String.format(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.conceptnofound"),
                                    updates[REORDER_LVEL_PUOM].toString())));
                    continue;
                }

                StockItemPackagingUOM uom = null;
                if (uoms != null) {
                    Optional<StockItemPackagingUOM> uomOptional = uoms.stream().filter(p -> p.getPackagingUom().getConceptId().equals((Integer) updates[REORDER_LVEL_PUOM])).findFirst();
                    if (uomOptional.isPresent()) {
                        uom = uomOptional.get();
                    }
                }

                if (uom == null) {
                    if (updates[DISPENSING_PUOM] != null && updates[DISPENSING_PUOM].equals(updates[REORDER_LVEL_PUOM])) {
                        uom = dispensingStockItemPackagingUOM;
                    } else {
                        result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(),
                                String.format(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.packagingunitwithconceptnoyfound"),
                                        updates[REORDER_LVEL_PUOM].toString())));
                        continue;
                    }
                }

                if (stockItem.getReorderLevelUOM() == null || !stockItem.getReorderLevelUOM().getUuid().equals(uom.getUuid())) {
                    reorderLevelUomToSet = uom;
                }


                if (REORDER_LEVEL < updates.length && updates[REORDER_LEVEL] != null) {
                    if (stockItem.getReorderLevel() == null || stockItem.getReorderLevel().compareTo((BigDecimal) updates[REORDER_LEVEL]) != 0) {
                        reorderlevelToSet = (BigDecimal) updates[REORDER_LEVEL];
                    }
                }
            }

            if (PURCHASE_PRICE_PUOM < updates.length && updates[PURCHASE_PRICE_PUOM] != null) {
                List<Concept> conceptCollection = concepts.getOrDefault(updates[PURCHASE_PRICE_PUOM], null);
                if (conceptCollection == null) {
                    result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(),
                            String.format(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.conceptnofound"),
                                    updates[PURCHASE_PRICE_PUOM].toString())));
                    continue;
                }

                StockItemPackagingUOM uom = null;
                if (uoms != null) {
                    Optional<StockItemPackagingUOM> uomOptional = uoms.stream().filter(p -> p.getPackagingUom().getConceptId().equals((Integer) updates[PURCHASE_PRICE_PUOM])).findFirst();
                    if (uomOptional.isPresent()) {
                        uom = uomOptional.get();
                    }
                }

                if (uom == null) {
                    if (updates[DISPENSING_PUOM] != null && updates[DISPENSING_PUOM].equals(updates[PURCHASE_PRICE_PUOM])) {
                        uom = dispensingStockItemPackagingUOM;
                    } else {
                        result.getErrors().add(String.format("Row %1s: %2s", recordToProcess.getKey(),
                                String.format(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.packagingunitwithconceptnoyfound"),
                                        updates[PURCHASE_PRICE_PUOM].toString())));
                        continue;
                    }
                }

                if (stockItem.getPurchasePriceUoM() == null || !stockItem.getPurchasePriceUoM().getUuid().equals(uom.getUuid())) {
                    purchasePriceUomToSet = uom;
                }

                if (PURCHASE_PRICE < updates.length && updates[PURCHASE_PRICE] != null) {
                    if (stockItem.getPurchasePrice() == null || stockItem.getPurchasePrice().compareTo((BigDecimal) updates[PURCHASE_PRICE]) != 0) {
                        purchasePriceToSet = (BigDecimal) updates[PURCHASE_PRICE];
                    }
                }
            }

            boolean saveStockItem = isVeryNewRecordDrug != null;

            if (packSizeToUpdate != null) {
                dispensingStockItemPackagingUOM.setFactor(packSizeToUpdate);
                dispensingStockItemPackagingUOM.setDateChanged(new Date());
                dispensingStockItemPackagingUOM.setChangedBy(Context.getAuthenticatedUser().getChangedBy());
            }
            if (dispensingUnitToSet != null) {
                stockItem.setDispensingUnit(dispensingUnitToSet);
                saveStockItem = true;
            }
            if (dispensingUomToSet != null) {
                stockItem.setDispensingUnitPackagingUoM(dispensingUomToSet);
                saveStockItem = true;
            }
            if (reorderLevelUomToSet != null) {
                stockItem.setReorderLevelUOM(reorderLevelUomToSet);
                saveStockItem = true;
            }
            if (purchasePriceUomToSet != null) {
                stockItem.setPurchasePriceUoM(purchasePriceUomToSet);
                saveStockItem = true;
            }
            if (reorderlevelToSet != null) {
                stockItem.setReorderLevel(reorderlevelToSet);
                saveStockItem = true;
            }
            if (purchasePriceToSet != null) {
                stockItem.setPurchasePrice(purchasePriceToSet);
                saveStockItem = true;
            }
            if (stockSourceToSet != null) {
                stockItem.setPreferredVendor(stockSourceToSet);
                saveStockItem = true;
            }
            if(expiresToSet != null){
                stockItem.setHasExpiration(expiresToSet);
                saveStockItem=true;
            }
            if(expiryNoticeToSet != null){
                stockItem.setExpiryNotice(expiryNoticeToSet);
                saveStockItem=true;
            }
            if(categoryConceptToSet != null){
                stockItem.setCategory(categoryConceptToSet);
                saveStockItem=true;
            }

            if (updates[VENDOR_REFERENCE_ITEM_CODE] != null && stockItem.getPreferredVendor() != null) {
                StockItemReference stockItemReference = new StockItemReference();
                Set<StockItemReference> stockItemReferenceSet = new HashSet<>();
                stockItemReference.setStockReferenceCode(updates[VENDOR_REFERENCE_ITEM_CODE].toString());
                stockItemReference.setStockItem(stockItem);
                stockItemReference.setReferenceSource(stockItem.getPreferredVendor());
                stockItemReferenceSet.add(stockItemReference);
                stockItem.setStockItemReferences(stockItemReferenceSet);
                saveStockItem = true;
            }

            if (saveStockItem) {
                stockItem.setDateChanged(new Date());
                stockItem.setChangedBy(Context.getAuthenticatedUser());
                if (saveDispensingStockItemPackagingUOM) {
                    stockManagementService.saveStockItem(stockItem, dispensingStockItemPackagingUOM);
                } else {
                    stockManagementService.saveStockItem(stockItem);
                }
                if (isVeryNewRecordDrug != null) {
                    if (isVeryNewRecordDrug) {
                        result.setCreatedCount(result.getCreatedCount() + 1);
                        prevStockItemDrugs.put((Integer) updates[DRUG_ID], stockItem);
                    } else {
                        result.setUpdatedCount(result.getUpdatedCount() + 1);
                        prevStockItemConcepts.put((Integer) updates[CONCEPT_ID], stockItem);
                    }
                }else{
                    result.setUpdatedCount(result.getUpdatedCount() + 1);
                }
            } else if (saveDispensingStockItemPackagingUOM) {
                stockManagementService.saveStockItemPackagingUOM(dispensingStockItemPackagingUOM);
                result.setUpdatedCount(result.getUpdatedCount() + 1);
            }else{
                result.setNotChangedCount(result.getNotChangedCount() + 1);
            }
        }
    }
	
	@SuppressWarnings({ "unchecked" })
	public void execute() {
        CSVReader csvReader = null;
        int row = 0;
        boolean hasErrors = false;
        try {
            try(Writer writer = Files.newBufferedWriter( new File(file.toString() + "_errors").toPath())) {
                boolean resetErrors = false;
                try (Reader reader = Files.newBufferedReader(file)) {
                    RFC4180Parser parser = new RFC4180ParserBuilder().withSeparator(',').withQuoteChar('\"').build();
                    if (hasHeader) {
                        csvReader = new CSVReaderHeaderAwareBuilder(reader).withCSVParser(parser).build();
                    } else {
                        csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).build();
                    }
                    String[] csvLine = null;
                    boolean processedPending = false;
                    Map<Integer, Object[]> list = new HashMap<>();
                    while ((csvLine = csvReader.readNext()) != null) {
                        row++;
                        processedPending = false;
                        resetErrors = false;
                        if(result.getErrors().size() > 10){
                            hasErrors=true;
                            for(String error: result.getErrors()){
                                writer.append(error);
                                writer.append("\r\n");
                            }
                            result.getErrors().clear();
                        }
                        Object validationResult = validateLine(csvLine);
                        if (validationResult == null) {
                            continue;
                        } else if (validationResult instanceof List<?>) {
                            List<String> errors = (List<String>) validationResult;
                            result.getErrors().add(String.format("Row %1s: %2s", row, String.join(", ", errors)));
                            continue;
                        }

                        list.put(row, (Object[]) validationResult);
                        if (list.size() == getBatchSize()) {
                            updateStockItems(list);
                            processedPending = true;
                            list.clear();
                        }

                    }
                    if (!processedPending) {
                        updateStockItems(list);
                    }
                }
                if(hasErrors){
                    for(String error: result.getErrors()){
                        writer.append(error);
                        writer.append("\r\n");
                    }
                    result.getErrors().clear();
                    result.getErrors().add(Context.getMessageSourceService().getMessage("stockmanagement.importoperation.errorswhileimporting"));
                    result.setHasErrorFile(true);
                } else if (result.getErrors().isEmpty()) {
                    result.setSuccess(true);
                }
            }
        } catch (Exception exception) {
            result.getErrors().add(0, "Stopped processing at row " + Integer.toString(row));
            result.getErrors().add(exception.toString());
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (Exception exception) {
                }
            }
        }
    }
	
	public Object getResult() {
		return result;
	}
}
