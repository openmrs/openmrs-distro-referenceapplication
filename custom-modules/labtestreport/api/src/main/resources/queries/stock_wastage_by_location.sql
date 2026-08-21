SELECT
  si.stock_item_id AS stockItemId,
  si.common_name   AS itemName,
  p.party_id       AS locationId,
  l.name           AS locationName,
  SUM(-sit.quantity) AS quantity,
  un.name          AS unitName,
  NULL             AS sourceLocationName
FROM stockmgmt_stock_item_transaction sit
JOIN stockmgmt_stock_item si ON si.stock_item_id = sit.stock_item_id
JOIN stockmgmt_stock_operation so ON so.stock_operation_id = sit.stock_operation_id
JOIN stockmgmt_stock_operation_type sot ON sot.stock_operation_type_id = so.operation_type_id
JOIN stockmgmt_party p ON p.party_id = sit.party_id
LEFT JOIN location l ON l.location_id = p.location_id
LEFT JOIN concept_name un ON un.concept_id = si.dispensing_unit_id AND un.locale = 'en' AND un.locale_preferred = 1
WHERE si.voided = 0
  AND sot.operation_type = 'disposed'
  AND sit.quantity < 0
  AND (:locationUuid IS NULL OR l.uuid = :locationUuid)
  AND (:startDate IS NULL OR DATE(sit.date_created) >= :startDate)
  AND (:endDate IS NULL OR DATE(sit.date_created) < DATE_ADD(:endDate, INTERVAL 1 DAY))
GROUP BY si.stock_item_id, si.common_name, p.party_id, l.name, un.name
ORDER BY quantity DESC
