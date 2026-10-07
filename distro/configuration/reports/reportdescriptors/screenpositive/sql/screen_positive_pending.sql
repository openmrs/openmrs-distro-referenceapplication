-- =============================================================================
-- Screen Positive, Pending Confirmation
-- One row per patient enrolled in the RHD Registry program now whose latest Screen + answer is Yes and whose
-- diagnosis has no Diagnosis Details yet, as ACT 2.0 listed screen positive patients pending diagnosis.
-- =============================================================================
SELECT
    MAX(rhd_id.identifier)                                          AS rhd_id,
    MAX(CONCAT(pn.given_name, ' ', pn.family_name))                 AS full_name,
    TIMESTAMPDIFF(YEAR, p.birthdate, CURDATE())                     AS age_years,
    p.gender                                                        AS sex,
    MAX(cardiac_loc.name)                                           AS cardiac_clinic,
    MAX(primary_loc.name)                                           AS primary_care_clinic,
    -- The Screen + Date recorded with the answer, else the date of the answer itself
    DATE(COALESCE((
        SELECT MAX(d.value_datetime)
        FROM obs d
        WHERE d.encounter_id = scr.encounter_id AND d.voided = 0
          AND d.concept_id = (SELECT concept_id FROM concept WHERE uuid = '96d6d328-87ba-5a2e-bb71-2d1cd73b3e90')
    ), scr.obs_datetime))                                           AS screen_date,
    p.uuid                                                          AS patient_uuid,
    -- The form to record the diagnosis in, as ACT 2.0's row opened the patient form
    enc.uuid                                                        AS encounter_uuid,
    frm.uuid                                                        AS form_uuid

FROM obs scr
JOIN person p ON p.person_id = scr.person_id AND p.voided = 0 AND p.dead = 0
JOIN patient pat ON pat.patient_id = p.person_id AND pat.voided = 0
-- The encounter holding the active primary diagnosis, else the one that recorded the Screen +
JOIN encounter enc ON enc.encounter_id = COALESCE((
        SELECT dg.encounter_id FROM obs dg
        WHERE dg.person_id = scr.person_id AND dg.voided = 0
          AND dg.concept_id = (SELECT concept_id FROM concept WHERE uuid = '594b4495-36dc-52a6-9810-15a9e2e2dcb9')
          AND NOT EXISTS (
              SELECT 1 FROM obs x
              WHERE x.obs_group_id = dg.obs_id AND x.voided = 0
                AND ((x.concept_id = (SELECT concept_id FROM concept WHERE uuid = 'b1b5d279-8013-5ae4-83a7-0f2bd9bc8457')
                      AND x.value_coded = (SELECT concept_id FROM concept WHERE uuid = '488b58ff-64f5-4f8a-8979-fa79940b1594'))
                  OR (x.concept_id = (SELECT concept_id FROM concept WHERE uuid = '97c025b2-f42c-5c53-8aaa-0506c8dd3774')
                      AND x.value_coded = (SELECT concept_id FROM concept WHERE uuid = 'af45fb2a-ed18-5beb-935e-8e4d92df2dac'))))
        ORDER BY dg.obs_datetime DESC, dg.obs_id DESC
        LIMIT 1
    ), scr.encounter_id)
LEFT JOIN form frm ON frm.form_id = enc.form_id

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

WHERE scr.voided = 0
    AND scr.concept_id = (SELECT concept_id FROM concept WHERE uuid = '82a5fbb5-038d-57b8-82d1-3fd0ac95673d')
    AND scr.value_coded = (SELECT concept_id FROM concept WHERE uuid = 'cf82933b-3f3f-45e7-a5ab-5d31aaee3da3')
    -- The patient's latest Screen + answer
    AND NOT EXISTS (
        SELECT 1 FROM obs later
        WHERE later.person_id = scr.person_id AND later.concept_id = scr.concept_id AND later.voided = 0
          AND (later.obs_datetime > scr.obs_datetime
               OR (later.obs_datetime = scr.obs_datetime AND later.obs_id > scr.obs_id))
    )
    -- No Diagnosis Details on the latest Diagnosis not answered inactive or secondary, the one the registry reads
    -- (as ACT 2.0 read the active primary diagnosis; unanswered counts, as those questions did not load before 2026-10-07)
    AND NOT EXISTS (
        SELECT 1 FROM obs det
        WHERE det.obs_group_id = (
            SELECT dg.obs_id FROM obs dg
            WHERE dg.person_id = scr.person_id AND dg.voided = 0
              AND dg.concept_id = (SELECT concept_id FROM concept WHERE uuid = '594b4495-36dc-52a6-9810-15a9e2e2dcb9')
              AND NOT EXISTS (
                  SELECT 1 FROM obs x
                  WHERE x.obs_group_id = dg.obs_id AND x.voided = 0
                    AND ((x.concept_id = (SELECT concept_id FROM concept WHERE uuid = 'b1b5d279-8013-5ae4-83a7-0f2bd9bc8457')
                          AND x.value_coded = (SELECT concept_id FROM concept WHERE uuid = '488b58ff-64f5-4f8a-8979-fa79940b1594'))
                      OR (x.concept_id = (SELECT concept_id FROM concept WHERE uuid = '97c025b2-f42c-5c53-8aaa-0506c8dd3774')
                          AND x.value_coded = (SELECT concept_id FROM concept WHERE uuid = 'af45fb2a-ed18-5beb-935e-8e4d92df2dac'))))
            ORDER BY dg.obs_datetime DESC, dg.obs_id DESC
            LIMIT 1
        )
          AND det.voided = 0 AND det.value_coded IS NOT NULL
          AND det.concept_id IN ((SELECT concept_id FROM concept WHERE uuid = 'cfe17bb5-4a76-5f3a-9e1c-7b1e9b84a8e3'),
                                 (SELECT concept_id FROM concept WHERE uuid = 'd3f6e1a2-9c5b-5e84-8f2d-1a6c7b9e0d4f'),
                                 (SELECT concept_id FROM concept WHERE uuid = 'b7a2c4d8-5e91-5f3a-8c6d-2b9e4f7a1c0d'),
                                 (SELECT concept_id FROM concept WHERE uuid = 'e9f3a5c7-6b82-5d4e-9f1a-3c7d8e2b5f4a'))
    )
    -- Enrolled in the RHD Registry now
    AND EXISTS (
        SELECT 1 FROM patient_program pp
        JOIN program pr ON pr.program_id = pp.program_id AND pr.uuid = '7d73e143-a550-5a9d-aecd-dd771add098d'
        WHERE pp.patient_id = p.person_id AND pp.voided = 0 AND pp.date_completed IS NULL
    )

GROUP BY scr.obs_id, scr.encounter_id, scr.obs_datetime, p.person_id, p.birthdate, p.gender, p.uuid, enc.uuid, frm.uuid

ORDER BY screen_date, rhd_id
