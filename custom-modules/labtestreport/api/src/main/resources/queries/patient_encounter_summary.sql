SELECT
  p.person_id                  AS patientId,
  p.uuid                       AS patientUuid,
  COALESCE(pn.given_name, '')  AS givenName,
  COALESCE(pn.family_name, '') AS familyName,
  TIMESTAMPDIFF(YEAR, p.birthdate, CURDATE()) AS age,
  COUNT(DISTINCT v.visit_id)   AS visitCount,
  MAX(v.date_started)          AS mostRecentVisitDate
FROM visit v
JOIN patient pt ON pt.patient_id = v.patient_id
JOIN person p   ON p.person_id = pt.patient_id
LEFT JOIN person_name pn ON pn.person_id = p.person_id AND pn.voided = 0 AND pn.preferred = 1
WHERE v.voided = 0
  AND (:startDate IS NULL OR v.date_started >= :startDate)
  AND (:endDate IS NULL OR v.date_started < DATE_ADD(:endDate, INTERVAL 1 DAY))
GROUP BY p.person_id, p.uuid, pn.given_name, pn.family_name, p.birthdate
ORDER BY familyName, givenName
