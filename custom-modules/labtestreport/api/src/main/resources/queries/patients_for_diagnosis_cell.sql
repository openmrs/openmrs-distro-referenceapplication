SELECT DISTINCT
  p.person_id                  AS patientId,
  p.uuid                       AS patientUuid,
  COALESCE(pn.given_name, '')  AS givenName,
  COALESCE(pn.family_name, '') AS familyName,
  pi.identifier                AS identifier,
  p.gender                     AS sex,
  pi_nid.identifier            AS nationalId,
  pa_phone.value                AS phoneNumber
FROM encounter_diagnosis ed
JOIN encounter e     ON e.encounter_id = ed.encounter_id
JOIN person p        ON p.person_id = ed.patient_id
JOIN patient pt      ON pt.patient_id = p.person_id
LEFT JOIN person_name pn        ON pn.person_id = p.person_id AND pn.voided = 0 AND pn.preferred = 1
LEFT JOIN patient_identifier pi ON pi.patient_id = pt.patient_id AND pi.voided = 0 AND pi.preferred = 1
LEFT JOIN patient_identifier pi_nid
  ON pi_nid.patient_id = pt.patient_id AND pi_nid.voided = 0
  AND pi_nid.identifier_type = (SELECT patient_identifier_type_id FROM patient_identifier_type WHERE name = 'National ID')
LEFT JOIN person_attribute pa_phone
  ON pa_phone.person_id = p.person_id AND pa_phone.voided = 0
  AND pa_phone.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type WHERE name = 'Phone Number')
WHERE ed.voided = 0
  AND ed.diagnosis_coded = :diagnosisConceptId
  AND (:gender IS NULL OR p.gender = :gender)
  AND (:startDate IS NULL OR e.encounter_datetime >= :startDate)
  AND (:endDate IS NULL OR e.encounter_datetime < DATE_ADD(:endDate, INTERVAL 1 DAY))
  AND (
    :ageGroup IS NULL
    OR (:ageGroup = '0-4'   AND TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 0  AND 4)
    OR (:ageGroup = '5-14'  AND TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 5  AND 14)
    OR (:ageGroup = '15-18' AND TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 15 AND 18)
    OR (:ageGroup = '19-49' AND TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 19 AND 49)
    OR (:ageGroup = '50-65' AND TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 50 AND 65)
    OR (:ageGroup = '65+'   AND TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) > 65)
  )
ORDER BY familyName, givenName
