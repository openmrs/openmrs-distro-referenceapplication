-- =============================================================================
-- Procedural Waiting List
-- One row per open interventional recommendation in each patient's latest RHD Consultation Visit,
-- for patients enrolled in the RHD Registry program now. A recommendation is open unless its
-- Completed answer is the concept.true concept.
-- =============================================================================
SELECT
    MAX(rhd_id.identifier)                                          AS rhd_id,
    p.gender                                                        AS sex,
    TIMESTAMPDIFF(YEAR, p.birthdate, CURDATE())                     AS age_years,
    MAX(type_name.name)                                             AS procedure_type,
    MAX(proc_name.name)                                             AS procedure_name,
    MAX(urg_name.name)                                              AS urgency,
    MAX(urg_concept.uuid)                                           AS urgency_concept,

    -- The first consultation since the procedure was last completed that recommended it, still open.
    DATE(COALESCE((
        SELECT MIN(e_first.encounter_datetime)
        FROM obs g_first
        JOIN encounter e_first ON e_first.encounter_id = g_first.encounter_id AND e_first.voided = 0
            AND e_first.patient_id = p.person_id
            AND e_first.form_id = e.form_id AND e_first.encounter_datetime <= e.encounter_datetime
        JOIN obs proc_first ON proc_first.obs_group_id = g_first.obs_id AND proc_first.voided = 0
            AND proc_first.person_id = p.person_id
            AND proc_first.concept_id = g.concept_id AND proc_first.value_coded = proc.value_coded
        WHERE g_first.person_id = p.person_id AND g_first.voided = 0
          AND g_first.concept_id = g.concept_id AND g_first.obs_group_id IS NULL
          AND e_first.encounter_datetime > COALESCE((
              SELECT MAX(e_done.encounter_datetime)
              FROM obs proc_done
              JOIN obs done ON done.obs_group_id = proc_done.obs_group_id AND done.voided = 0
                  AND done.concept_id = (SELECT concept_id FROM concept WHERE uuid = '1632f8dc-195d-5d67-a52e-6c7a057cb536')
                  AND done.value_coded = (SELECT CAST(property_value AS UNSIGNED) FROM global_property WHERE property = 'concept.true')
              JOIN encounter e_done ON e_done.encounter_id = proc_done.encounter_id AND e_done.voided = 0
                  AND e_done.patient_id = p.person_id AND e_done.form_id = e.form_id
              WHERE proc_done.person_id = p.person_id AND proc_done.voided = 0
                AND proc_done.concept_id = g.concept_id AND proc_done.obs_group_id IS NOT NULL
                AND proc_done.value_coded = proc.value_coded
          ), '1900-01-01')
    ), e.encounter_datetime))                                       AS date_added,

    MAX(addr.state_province)                                        AS district,
    -- The form's Yes and No answers are core's True and False concepts
    MAX(CASE contra.value_coded
        WHEN (SELECT CAST(property_value AS UNSIGNED) FROM global_property WHERE property = 'concept.true') THEN 'Yes'
        WHEN (SELECT CAST(property_value AS UNSIGNED) FROM global_property WHERE property = 'concept.false') THEN 'No'
        ELSE contra_name.name
    END)                                                            AS contraindications,
    -- From the latest RHD Echocardiogram; blank when that echo did not answer it
    (
        SELECT cn_echo.name
        FROM encounter e_echo
        JOIN form f_echo ON f_echo.form_id = e_echo.form_id AND f_echo.uuid = '88e54fb0-1243-3f7a-b925-f64648ca6635'
        LEFT JOIN obs o_echo ON o_echo.encounter_id = e_echo.encounter_id AND o_echo.voided = 0
            AND o_echo.concept_id = (SELECT concept_id FROM concept WHERE uuid = '8d6e9ea5-aa27-57d6-bf43-198aca63a951')
        LEFT JOIN concept_name cn_echo ON cn_echo.concept_id = o_echo.value_coded
            AND cn_echo.locale = 'en' AND cn_echo.locale_preferred = 1 AND cn_echo.voided = 0
        WHERE e_echo.patient_id = p.person_id AND e_echo.voided = 0
        ORDER BY e_echo.encounter_datetime DESC, e_echo.encounter_id DESC LIMIT 1
    )                                                               AS suitable_for_repair,

    MAX(cardiac_loc.name)                                           AS cardiac_clinic,
    MAX(primary_loc.name)                                           AS primary_care_clinic,
    g.uuid                                                          AS recommendation_uuid,
    p.uuid                                                          AS patient_uuid,
    e.uuid                                                          AS encounter_uuid,
    f.uuid                                                          AS form_uuid

FROM encounter e
JOIN form f ON f.form_id = e.form_id AND f.uuid = '4b063fc7-996f-3001-8500-8940e201be8f'
-- A patient who has died is not waiting, although the RHD Registry enrolment stays open
JOIN person p ON p.person_id = e.patient_id AND p.voided = 0 AND p.dead = 0
JOIN patient pat ON pat.patient_id = p.person_id AND pat.voided = 0

-- Each recommendation is an obs group of Interventional Recommendation(s); its procedure member has the same concept
JOIN obs g ON g.encounter_id = e.encounter_id AND g.voided = 0 AND g.obs_group_id IS NULL
    AND g.concept_id = (SELECT concept_id FROM concept WHERE uuid = 'ce93d393-1df5-587f-bf11-c373eac2ccf4')
LEFT JOIN obs proc ON proc.obs_group_id = g.obs_id AND proc.voided = 0 AND proc.concept_id = g.concept_id
LEFT JOIN concept_name proc_name ON proc_name.concept_id = proc.value_coded
    AND proc_name.locale = 'en' AND proc_name.locale_preferred = 1 AND proc_name.voided = 0
LEFT JOIN obs typ ON typ.obs_group_id = g.obs_id AND typ.voided = 0
    AND typ.concept_id = (SELECT concept_id FROM concept WHERE uuid = '3a576b80-744a-59ee-9515-d9fdca06f3d7')
LEFT JOIN concept_name type_name ON type_name.concept_id = typ.value_coded
    AND type_name.locale = 'en' AND type_name.locale_preferred = 1 AND type_name.voided = 0
LEFT JOIN obs urg ON urg.obs_group_id = g.obs_id AND urg.voided = 0
    AND urg.concept_id = (SELECT concept_id FROM concept WHERE uuid = '7b8eda07-34b6-55f2-ab6c-1b295f41918b')
LEFT JOIN concept urg_concept ON urg_concept.concept_id = urg.value_coded
LEFT JOIN concept_name urg_name ON urg_name.concept_id = urg.value_coded
    AND urg_name.locale = 'en' AND urg_name.locale_preferred = 1 AND urg_name.voided = 0

-- Contraindications for Mechanical Valve, from the same consultation
LEFT JOIN obs contra ON contra.encounter_id = e.encounter_id AND contra.voided = 0
    AND contra.concept_id = (SELECT concept_id FROM concept WHERE uuid = 'b393b00a-8641-5013-bcc7-dec1334a1252')
LEFT JOIN concept_name contra_name ON contra_name.concept_id = contra.value_coded
    AND contra_name.locale = 'en' AND contra_name.locale_preferred = 1 AND contra_name.voided = 0

LEFT JOIN patient_identifier rhd_id
    ON rhd_id.patient_id = p.person_id AND rhd_id.voided = 0
    AND rhd_id.identifier_type = (SELECT patient_identifier_type_id FROM patient_identifier_type
                                   WHERE uuid = '240f85fa-46e1-540e-9234-2796c623f7ea')
-- The address hierarchy keeps the district in state_province
LEFT JOIN person_address addr ON addr.person_id = p.person_id AND addr.voided = 0 AND addr.preferred = 1
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

WHERE e.voided = 0
    -- The patient's latest RHD Consultation Visit
    AND NOT EXISTS (
        SELECT 1 FROM encounter later
        WHERE later.patient_id = e.patient_id AND later.form_id = e.form_id AND later.voided = 0
          AND (later.encounter_datetime > e.encounter_datetime
               OR (later.encounter_datetime = e.encounter_datetime AND later.encounter_id > e.encounter_id))
    )
    -- Enrolled in the RHD Registry now
    AND EXISTS (
        SELECT 1 FROM patient_program pp
        JOIN program pr ON pr.program_id = pp.program_id AND pr.uuid = '7d73e143-a550-5a9d-aecd-dd771add098d'
        WHERE pp.patient_id = p.person_id AND pp.voided = 0 AND pp.date_completed IS NULL
    )
    -- Not completed
    AND NOT EXISTS (
        SELECT 1 FROM obs done
        WHERE done.obs_group_id = g.obs_id AND done.voided = 0
          AND done.concept_id = (SELECT concept_id FROM concept WHERE uuid = '1632f8dc-195d-5d67-a52e-6c7a057cb536')
          AND done.value_coded = (SELECT CAST(property_value AS UNSIGNED) FROM global_property WHERE property = 'concept.true')
    )

GROUP BY g.obs_id, g.uuid, g.concept_id, proc.value_coded, e.encounter_id, e.uuid, e.form_id, e.encounter_datetime, f.uuid, p.person_id, p.gender, p.birthdate, p.uuid

ORDER BY date_added, rhd_id
