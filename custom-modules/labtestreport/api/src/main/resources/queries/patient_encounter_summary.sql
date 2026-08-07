SELECT
  p.person_id                  AS patientId,
  p.uuid                       AS patientUuid,
  COALESCE(pn.given_name, '')  AS givenName,
  COALESCE(pn.family_name, '') AS familyName,
  TIMESTAMPDIFF(YEAR, p.birthdate, CURDATE()) AS age,
  COUNT(DISTINCT v.visit_id)   AS visitCount,
  MAX(v.date_started)          AS mostRecentVisitDate,
  p.gender                     AS sex,
  MAX(pi_nid.identifier)       AS nationalId,
  MAX(pa_phone.value)          AS phoneNumber,
  GROUP_CONCAT(DISTINCT l.name ORDER BY l.name SEPARATOR ', ') AS location,
  GROUP_CONCAT(DISTINCT pr.name ORDER BY pr.name SEPARATOR ', ') AS serviceType
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
LEFT JOIN location l ON l.location_id = v.location_id
-- The program active (enrolled, not yet completed) at the time this particular visit started --
-- one of our four service-type programs, if any -- classifies the visit as PH/SRH/Nutrition/Pediatric.
LEFT JOIN patient_program pp
  ON pp.patient_id = pt.patient_id AND pp.voided = 0
  AND pp.date_enrolled <= v.date_started
  AND (pp.date_completed IS NULL OR pp.date_completed >= v.date_started)
LEFT JOIN program pr
  ON pr.program_id = pp.program_id
  AND pr.uuid IN (
    '2433ebba-8ffb-11f1-a103-1afee95a890c', -- Nutrition Registration
    'f73376c9-7bdf-44e5-ba97-ddf4db5bc9f9', -- Sexual Reproductive Health (SRH)
    'bd6b8c0a-49c9-4f98-afea-8b8fcd999688', -- Primary Health Care
    '9138885e-f9f4-4981-b1fb-ef3d022228bd'  -- Pediatric Consultation
  )
WHERE v.voided = 0
  AND (:startDate IS NULL OR v.date_started >= :startDate)
  AND (:endDate IS NULL OR v.date_started < DATE_ADD(:endDate, INTERVAL 1 DAY))
-- Deliberately NOT grouping by pi_nid.identifier/pa_phone.value (aggregated with MAX() above instead):
-- a patient with more than one non-voided National ID/Phone Number would otherwise fan out into
-- one row per combination, making the same patient appear to be duplicated in the report.
GROUP BY p.person_id, p.uuid, pn.given_name, pn.family_name, p.birthdate, p.gender
ORDER BY familyName, givenName
