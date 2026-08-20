SELECT
  si.stock_item_id AS stockItemId,
  si.common_name   AS itemName,
  p.party_id       AS locationId,
  l.name           AS locationName,
  sr.name          AS ruleName,
  sr.quantity      AS reorderLevel,
  COALESCE(onhand.onHandQty, 0) AS onHandQty
FROM stockmgmt_stock_rule sr
JOIN stockmgmt_stock_item si ON si.stock_item_id = sr.stock_item_id
JOIN stockmgmt_party p ON p.location_id = sr.location_id
LEFT JOIN location l ON l.location_id = sr.location_id
LEFT JOIN (
  SELECT stock_item_id, party_id, SUM(quantity) AS onHandQty
  FROM stockmgmt_stock_item_transaction
  GROUP BY stock_item_id, party_id
) onhand ON onhand.stock_item_id = si.stock_item_id AND onhand.party_id = p.party_id
WHERE sr.voided = 0
  AND sr.enabled = 1
  AND si.voided = 0
  AND (:locationUuid IS NULL OR l.uuid = :locationUuid)
  AND COALESCE(onhand.onHandQty, 0) < sr.quantity
ORDER BY (sr.quantity - COALESCE(onhand.onHandQty, 0)) DESC
