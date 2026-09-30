-- =============================================================================
-- Screen Positive, Pending Confirmation
-- One row per patient enrolled in the RHD Registry program now whose latest Category at Diagnosis is
-- Screen + pending confirmatory echo: they screened positive and no confirmatory echo has recategorised them.
-- =============================================================================
SELECT
    MAX(rhd_id.identifier)                                          AS rhd_id,
    MAX(CONCAT(pn.given_name, ' ', pn.family_name))                 AS full_name,
    TIMESTAMPDIFF(YEAR, p.birthdate, CURDATE())                     AS age_years,
    p.gender                                                        AS sex,
    MAX(cardiac_loc.name)                                           AS cardiac_clinic,
    MAX(primary_loc.name)                                           AS primary_care_clinic,
    -- The Date of Diagnosis recorded with the category, else the date of the category itself
    DATE(COALESCE((
        SELECT MAX(d.value_datetime)
        FROM obs d
        WHERE d.encounter_id = cat.encounter_id AND d.voided = 0
          AND d.concept_id = (SELECT concept_id FROM concept WHERE uuid = '159948AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA')
    ), cat.obs_datetime))                                           AS screen_date,
    p.uuid                                                          AS patient_uuid

FROM obs cat
JOIN person p ON p.person_id = cat.person_id AND p.voided = 0 AND p.dead = 0
JOIN patient pat ON pat.patient_id = p.person_id AND pat.voided = 0

LEFT JOIN person_name pn ON pn.person_id = p.person_id AND pn.voided = 0 AND pn.preferred = 1
LEFT JOIN patient_identifier rhd_id
    ON rhd_id.patient_id = p.person_id AND rhd_id.voided = 0
    AND rhd_id.identifier_type = (SELECT patient_identifier_type_id FROM patient_identifier_type
                                   WHERE uuid = '240f85fa-46e1-540e-9234-2796c623f7ea')
LEFT JOIN person_attribute pa_primary
    ON pa_primary.person_id = p.person_id AND pa_primary.voided = 0
    AND pa_primary.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type
                                                WHERE name = 'Health Center' LIMIT 1)
LEFT JOIN person_attribute pa_cardiac
    ON pa_cardiac.person_id = p.person_id AND pa_cardiac.voided = 0
    AND pa_cardiac.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type
                                                WHERE uuid = 'fe261119-2911-5b36-be40-8f9827826987')
LEFT JOIN location cardiac_loc ON cardiac_loc.location_id = pa_cardiac.value
LEFT JOIN location primary_loc ON primary_loc.location_id = pa_primary.value

WHERE cat.voided = 0
    AND cat.concept_id = (SELECT concept_id FROM concept WHERE uuid = '1a5aa050-661d-5e89-95d7-c1eba476df22')
    AND cat.value_coded = (SELECT concept_id FROM concept WHERE uuid = '27f33ebe-77fb-575f-b737-00aa47dae6d8')
    -- The patient's latest Category at Diagnosis
    AND NOT EXISTS (
        SELECT 1 FROM obs later
        WHERE later.person_id = cat.person_id AND later.concept_id = cat.concept_id AND later.voided = 0
          AND (later.obs_datetime > cat.obs_datetime
               OR (later.obs_datetime = cat.obs_datetime AND later.obs_id > cat.obs_id))
    )
    -- Enrolled in the RHD Registry now
    AND EXISTS (
        SELECT 1 FROM patient_program pp
        JOIN program pr ON pr.program_id = pp.program_id AND pr.uuid = '7d73e143-a550-5a9d-aecd-dd771add098d'
        WHERE pp.patient_id = p.person_id AND pp.voided = 0 AND pp.date_completed IS NULL
    )

GROUP BY cat.obs_id, cat.encounter_id, cat.obs_datetime, p.person_id, p.birthdate, p.gender, p.uuid

ORDER BY screen_date, rhd_id
