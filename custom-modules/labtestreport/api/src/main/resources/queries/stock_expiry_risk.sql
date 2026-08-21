SELECT
  si.stock_item_id AS stockItemId,
  si.common_name   AS itemName,
  p.party_id       AS locationId,
  l.name           AS locationName,
  sb.batch_no      AS batchNo,
  sb.expiration    AS expirationDate,
  SUM(sit.quantity) AS remainingQty,
  DATEDIFF(sb.expiration, CURDATE()) AS daysUntilExpiry,
  un.name          AS unitName
FROM stockmgmt_stock_item_transaction sit
JOIN stockmgmt_stock_batch sb ON sb.stock_batch_id = sit.stock_batch_id
JOIN stockmgmt_stock_item si ON si.stock_item_id = sit.stock_item_id
JOIN stockmgmt_party p ON p.party_id = sit.party_id
LEFT JOIN location l ON l.location_id = p.location_id
LEFT JOIN concept_name un ON un.concept_id = si.dispensing_unit_id AND un.locale = 'en' AND un.locale_preferred = 1
WHERE si.voided = 0
  AND sb.expiration IS NOT NULL
  AND (:locationUuid IS NULL OR l.uuid = :locationUuid)
  AND (:daysAhead IS NULL OR DATEDIFF(sb.expiration, CURDATE()) <= :daysAhead)
GROUP BY si.stock_item_id, si.common_name, p.party_id, l.name, sb.batch_no, sb.expiration, un.name
HAVING remainingQty > 0
ORDER BY daysUntilExpiry ASC
