SELECT
  p.person_id                  AS patientId,
  p.uuid                       AS patientUuid,
  COALESCE(pn.given_name, '')  AS givenName,
  COALESCE(pn.family_name, '') AS familyName,
  TIMESTAMPDIFF(YEAR, p.birthdate, CURDATE()) AS age,
  COUNT(e.encounter_id)        AS encounterCount,
  MAX(e.encounter_datetime)    AS mostRecentEncounterDate
FROM encounter e
JOIN patient pt ON pt.patient_id = e.patient_id
JOIN person p   ON p.person_id = pt.patient_id
LEFT JOIN person_name pn ON pn.person_id = p.person_id AND pn.voided = 0 AND pn.preferred = 1
WHERE e.voided = 0
  AND (:startDate IS NULL OR e.encounter_datetime >= :startDate)
  AND (:endDate IS NULL OR e.encounter_datetime < DATE_ADD(:endDate, INTERVAL 1 DAY))
GROUP BY p.person_id, p.uuid, pn.given_name, pn.family_name, p.birthdate
ORDER BY familyName, givenName
