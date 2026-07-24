SELECT
  stockItemId,
  itemName,
  txDate AS ledgerDate,
  incoming AS incomingQty,
  outgoing AS outgoingQty,
  SUM(netChange) OVER (PARTITION BY stockItemId ORDER BY txDate) AS remainingQty
FROM (
  SELECT
    si.stock_item_id AS stockItemId,
    si.common_name  AS itemName,
    DATE(sit.date_created) AS txDate,
    SUM(CASE WHEN sit.quantity > 0 THEN sit.quantity ELSE 0 END) AS incoming,
    SUM(CASE WHEN sit.quantity < 0 THEN -sit.quantity ELSE 0 END) AS outgoing,
    SUM(sit.quantity) AS netChange
  FROM stockmgmt_stock_item_transaction sit
  JOIN stockmgmt_stock_item si ON si.stock_item_id = sit.stock_item_id
  WHERE si.voided = 0
    AND (:startDate IS NULL OR DATE(sit.date_created) >= :startDate)
    AND (:endDate IS NULL OR DATE(sit.date_created) < DATE_ADD(:endDate, INTERVAL 1 DAY))
  GROUP BY si.stock_item_id, si.common_name, DATE(sit.date_created)
) daily
ORDER BY itemName, ledgerDate
