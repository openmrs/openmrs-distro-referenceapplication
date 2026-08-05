SELECT
  p.person_id                  AS patientId,
  p.uuid                       AS patientUuid,
  COALESCE(pn.given_name, '')  AS givenName,
  COALESCE(pn.family_name, '') AS familyName,
  v.visit_id                    AS visitId,
  v.date_started                AS visitDate,
  COALESCE(l.name, '')          AS locationName,
  COALESCE(GROUP_CONCAT(DISTINCT COALESCE(pv.name, TRIM(CONCAT(pvpn.given_name, ' ', pvpn.family_name))) SEPARATOR ', '), '') AS providerName
FROM visit v
JOIN patient pt ON pt.patient_id = v.patient_id
JOIN person p   ON p.person_id = pt.patient_id
LEFT JOIN person_name pn ON pn.person_id = p.person_id AND pn.voided = 0 AND pn.preferred = 1
LEFT JOIN location l ON l.location_id = v.location_id
LEFT JOIN encounter e ON e.visit_id = v.visit_id AND e.voided = 0
LEFT JOIN encounter_provider ep ON ep.encounter_id = e.encounter_id AND ep.voided = 0
LEFT JOIN provider pv ON pv.provider_id = ep.provider_id
LEFT JOIN person_name pvpn ON pvpn.person_id = pv.person_id AND pvpn.voided = 0 AND pvpn.preferred = 1
WHERE v.voided = 0
  AND (:startDate IS NULL OR v.date_started >= :startDate)
  AND (:endDate IS NULL OR v.date_started < DATE_ADD(:endDate, INTERVAL 1 DAY))
GROUP BY p.person_id, p.uuid, pn.given_name, pn.family_name, v.visit_id, v.date_started, l.name
ORDER BY familyName, givenName, v.date_started
