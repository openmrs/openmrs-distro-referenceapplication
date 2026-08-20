SELECT
  si.stock_item_id AS stockItemId,
  si.common_name   AS itemName,
  p.party_id       AS locationId,
  l.name           AS locationName,
  SUM(sit.quantity) AS onHandQty
FROM stockmgmt_stock_item_transaction sit
JOIN stockmgmt_stock_item si ON si.stock_item_id = sit.stock_item_id
JOIN stockmgmt_party p ON p.party_id = sit.party_id
LEFT JOIN location l ON l.location_id = p.location_id
WHERE si.voided = 0
  AND (:locationUuid IS NULL OR l.uuid = :locationUuid)
GROUP BY si.stock_item_id, si.common_name, p.party_id, l.name
ORDER BY si.common_name, l.name
