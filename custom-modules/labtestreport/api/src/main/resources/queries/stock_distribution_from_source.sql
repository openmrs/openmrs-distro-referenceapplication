SELECT
  si.stock_item_id AS stockItemId,
  si.common_name   AS itemName,
  destP.party_id   AS locationId,
  destL.name       AS locationName,
  SUM(sit.quantity) AS quantity,
  un.name          AS unitName,
  srcL.name        AS sourceLocationName
FROM stockmgmt_stock_item_transaction sit
JOIN stockmgmt_stock_item si ON si.stock_item_id = sit.stock_item_id
JOIN stockmgmt_stock_operation so ON so.stock_operation_id = sit.stock_operation_id
JOIN stockmgmt_stock_operation_type sot ON sot.stock_operation_type_id = so.operation_type_id
JOIN stockmgmt_party srcP ON srcP.party_id = so.source_id
LEFT JOIN location srcL ON srcL.location_id = srcP.location_id
JOIN stockmgmt_party destP ON destP.party_id = sit.party_id
LEFT JOIN location destL ON destL.location_id = destP.location_id
LEFT JOIN concept_name un ON un.concept_id = si.dispensing_unit_id AND un.locale = 'en' AND un.locale_preferred = 1
WHERE si.voided = 0
  AND sot.operation_type = 'transferout'
  AND sit.quantity > 0
  AND sit.party_id != so.source_id
  AND (:sourceLocationUuid IS NULL OR srcL.uuid = :sourceLocationUuid)
  AND (:startDate IS NULL OR DATE(sit.date_created) >= :startDate)
  AND (:endDate IS NULL OR DATE(sit.date_created) < DATE_ADD(:endDate, INTERVAL 1 DAY))
GROUP BY si.stock_item_id, si.common_name, destP.party_id, destL.name, un.name, srcL.name
ORDER BY quantity DESC
