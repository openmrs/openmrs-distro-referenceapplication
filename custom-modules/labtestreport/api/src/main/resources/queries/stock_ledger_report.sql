SELECT
  stockItemId,
  itemName,
  locationId,
  locationName,
  txDate AS ledgerDate,
  incoming AS incomingQty,
  outgoing AS outgoingQty,
  SUM(netChange) OVER (PARTITION BY stockItemId, locationId ORDER BY txDate) AS remainingQty,
  unitName
FROM (
  SELECT
    si.stock_item_id AS stockItemId,
    si.common_name  AS itemName,
    p.party_id       AS locationId,
    l.name           AS locationName,
    DATE(sit.date_created) AS txDate,
    SUM(CASE WHEN sit.quantity > 0 THEN sit.quantity ELSE 0 END) AS incoming,
    SUM(CASE WHEN sit.quantity < 0 THEN -sit.quantity ELSE 0 END) AS outgoing,
    SUM(sit.quantity) AS netChange,
    un.name AS unitName
  FROM stockmgmt_stock_item_transaction sit
  JOIN stockmgmt_stock_item si ON si.stock_item_id = sit.stock_item_id
  JOIN stockmgmt_party p ON p.party_id = sit.party_id
  LEFT JOIN location l ON l.location_id = p.location_id
  LEFT JOIN concept_name un ON un.concept_id = si.dispensing_unit_id AND un.locale = 'en' AND un.locale_preferred = 1
  WHERE si.voided = 0
    AND (:startDate IS NULL OR DATE(sit.date_created) >= :startDate)
    AND (:endDate IS NULL OR DATE(sit.date_created) < DATE_ADD(:endDate, INTERVAL 1 DAY))
    AND (:locationUuid IS NULL OR l.uuid = :locationUuid)
  GROUP BY si.stock_item_id, si.common_name, p.party_id, l.name, DATE(sit.date_created), un.name
) daily
ORDER BY itemName, locationName, ledgerDate
