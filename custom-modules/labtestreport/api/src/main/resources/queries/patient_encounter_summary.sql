SELECT
  p.person_id                  AS patientId,
  p.uuid                       AS patientUuid,
  COALESCE(pn.given_name, '')  AS givenName,
  COALESCE(pn.family_name, '') AS familyName,
  TIMESTAMPDIFF(YEAR, p.birthdate, CURDATE()) AS age,
  COUNT(DISTINCT v.visit_id)   AS visitCount,
  MAX(v.date_started)          AS mostRecentVisitDate,
  p.gender                     AS sex,
  pi_nid.identifier            AS nationalId,
  pa_phone.value                AS phoneNumber
FROM visit v
JOIN patient pt ON pt.patient_id = v.patient_id
JOIN person p   ON p.person_id = pt.patient_id
LEFT JOIN person_name pn ON pn.person_id = p.person_id AND pn.voided = 0 AND pn.preferred = 1
LEFT JOIN patient_identifier pi_nid
  ON pi_nid.patient_id = pt.patient_id AND pi_nid.voided = 0
  AND pi_nid.identifier_type = (SELECT patient_identifier_type_id FROM patient_identifier_type WHERE name = 'National ID')
LEFT JOIN person_attribute pa_phone
  ON pa_phone.person_id = p.person_id AND pa_phone.voided = 0
  AND pa_phone.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type WHERE name = 'Phone Number')
WHERE v.voided = 0
  AND (:startDate IS NULL OR v.date_started >= :startDate)
  AND (:endDate IS NULL OR v.date_started < DATE_ADD(:endDate, INTERVAL 1 DAY))
GROUP BY p.person_id, p.uuid, pn.given_name, pn.family_name, p.birthdate, p.gender, pi_nid.identifier, pa_phone.value
ORDER BY familyName, givenName
