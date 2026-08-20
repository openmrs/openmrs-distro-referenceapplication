-- Counts, per stock item/location, how many days WITH RECORDED ACTIVITY within the range ended
-- at a zero-or-below running balance. This is an approximation of true stockout frequency: gaps
-- between transaction dates aren't back-filled, so it undercounts a stockout that persists across
-- several calendar days with no further activity recorded in between.
SELECT
  d.stockItemId,
  d.itemName,
  d.locationId,
  d.locationName,
  SUM(CASE WHEN d.dayEndBalance <= 0 THEN 1 ELSE 0 END) AS stockoutDays,
  COUNT(*) AS activeDays
FROM (
  SELECT
    si.stock_item_id AS stockItemId,
    si.common_name   AS itemName,
    p.party_id       AS locationId,
    l.name           AS locationName,
    DATE(sit.date_created) AS txDate,
    SUM(SUM(sit.quantity)) OVER (PARTITION BY si.stock_item_id, p.party_id ORDER BY DATE(sit.date_created)) AS dayEndBalance
  FROM stockmgmt_stock_item_transaction sit
  JOIN stockmgmt_stock_item si ON si.stock_item_id = sit.stock_item_id
  JOIN stockmgmt_party p ON p.party_id = sit.party_id
  LEFT JOIN location l ON l.location_id = p.location_id
  WHERE si.voided = 0
    AND (:locationUuid IS NULL OR l.uuid = :locationUuid)
    AND (:startDate IS NULL OR DATE(sit.date_created) >= :startDate)
    AND (:endDate IS NULL OR DATE(sit.date_created) < DATE_ADD(:endDate, INTERVAL 1 DAY))
  GROUP BY si.stock_item_id, si.common_name, p.party_id, l.name, DATE(sit.date_created)
) d
GROUP BY d.stockItemId, d.itemName, d.locationId, d.locationName
ORDER BY stockoutDays DESC
