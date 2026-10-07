-- =============================================================================
-- RHD Patient List
-- One row per patient enrolled in the RHD Registry program, from their latest enrollment.
-- Parameters: @startDate, @endDate  (program enrollment date range)
-- =============================================================================
SELECT
    -- Identifiers
    MAX(rhd_id.identifier)                                          AS rhd_id,
    MAX(ext_id.identifier)                                          AS external_id,
    MAX(nat_id.identifier)                                          AS national_id,

    -- Demographics
    MAX(CONCAT(pn.given_name, ' ', pn.family_name))                 AS full_name,
    p.gender                                                        AS sex,
    p.birthdate                                                     AS date_of_birth,
    TIMESTAMPDIFF(YEAR, p.birthdate, CURDATE())                     AS age_years,
    MAX(pa_phone.value)                                             AS phone_number,
    MAX(pa_village.value)                                           AS village,

    -- Program enrollment
    DATE(pp.date_enrolled)                                          AS date_enrolled,
    DATE(pp.date_completed)                                         AS date_completed,
    CASE WHEN pp.date_completed IS NULL THEN 'Active' ELSE 'Completed' END AS enrollment_status,

    -- Current workflow state (program stage)
    (
        SELECT cn.name
        FROM patient_state ps2
        JOIN program_workflow_state pws2 ON pws2.program_workflow_state_id = ps2.state
        JOIN concept_name cn ON cn.concept_id = pws2.concept_id AND cn.locale = 'en' AND cn.locale_preferred = 1 AND cn.voided = 0
        WHERE ps2.patient_program_id = pp.patient_program_id
          AND ps2.voided = 0 AND ps2.end_date IS NULL
        LIMIT 1
    )                                                               AS current_state,

    -- Diagnosis category (most recent)
    (
        SELECT cn2.name
        FROM obs cat_obs
        JOIN concept_name cn2 ON cn2.concept_id = cat_obs.value_coded
            AND cn2.locale = 'en' AND cn2.locale_preferred = 1 AND cn2.voided = 0
        WHERE cat_obs.person_id = p.person_id
          AND cat_obs.voided = 0
          AND cat_obs.concept_id = (SELECT concept_id FROM concept WHERE uuid = '1a5aa050-661d-5e89-95d7-c1eba476df22')
        ORDER BY cat_obs.obs_datetime DESC, cat_obs.obs_id DESC
        LIMIT 1
    )                                                               AS diagnosis_category,

    -- Case detection method
    (
        SELECT cn3.name
        FROM obs det_obs
        JOIN concept_name cn3 ON cn3.concept_id = det_obs.value_coded
            AND cn3.locale = 'en' AND cn3.locale_preferred = 1 AND cn3.voided = 0
        WHERE det_obs.person_id = p.person_id
          AND det_obs.voided = 0
          AND det_obs.concept_id = (SELECT concept_id FROM concept WHERE uuid = '955632a1-82f7-5b84-a341-de85f95588d1')
        ORDER BY det_obs.obs_datetime ASC LIMIT 1
    )                                                               AS case_detected_by,

    -- Penicillin allergy
    (
        SELECT cn_pen.name
        FROM obs o_pen
        JOIN concept_name cn_pen ON cn_pen.concept_id = o_pen.value_coded
            AND cn_pen.locale = 'en' AND cn_pen.locale_preferred = 1 AND cn_pen.voided = 0
        WHERE o_pen.person_id = p.person_id AND o_pen.voided = 0
          AND o_pen.concept_id = (SELECT concept_id FROM concept WHERE uuid = '5cc3b707-b8ea-52ee-8e82-ea4de393a4a5')
        ORDER BY o_pen.obs_datetime DESC LIMIT 1
    )                                                               AS penicillin_allergy,

    -- Date of last consultation encounter
    (
        SELECT DATE(MAX(e_last.encounter_datetime))
        FROM encounter e_last
        JOIN encounter_type et_last ON et_last.encounter_type_id = e_last.encounter_type
            AND et_last.uuid = 'c2503561-c00d-5460-8157-43d594472b4a'
        WHERE e_last.patient_id = p.person_id AND e_last.voided = 0
    )                                                               AS last_consultation_date,

    -- Latest Secondary Antibiotic Prophylaxis answer
    (
        SELECT cn_sap.name
        FROM obs o_sap
        JOIN concept_name cn_sap ON cn_sap.concept_id = o_sap.value_coded
            AND cn_sap.locale = 'en' AND cn_sap.locale_preferred = 1 AND cn_sap.voided = 0
        WHERE o_sap.person_id = p.person_id AND o_sap.voided = 0
          AND o_sap.concept_id = (SELECT concept_id FROM concept WHERE uuid = '668e0221-8b41-5669-9ad8-78e193d42494')
        ORDER BY o_sap.obs_datetime DESC, o_sap.obs_id DESC LIMIT 1
    )                                                               AS prophylaxis_regimen,

    -- Next consultation: the latest Next Consultation Time Amount after its encounter's date, in the
    -- Time Period recorded on the same encounter
    (
        SELECT DATE(CASE per.uuid
            WHEN '1072AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' THEN DATE_ADD(e_amt.encounter_datetime, INTERVAL ROUND(amt.value_numeric) DAY)
            WHEN '1073AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' THEN DATE_ADD(e_amt.encounter_datetime, INTERVAL ROUND(amt.value_numeric) WEEK)
            WHEN '1074AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' THEN DATE_ADD(e_amt.encounter_datetime, INTERVAL ROUND(amt.value_numeric) MONTH)
            WHEN '1734AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA' THEN DATE_ADD(e_amt.encounter_datetime, INTERVAL ROUND(amt.value_numeric) YEAR)
        END)
        FROM obs amt
        JOIN encounter e_amt ON e_amt.encounter_id = amt.encounter_id AND e_amt.voided = 0
        JOIN obs o_per ON o_per.encounter_id = amt.encounter_id AND o_per.voided = 0
            AND o_per.concept_id = (SELECT concept_id FROM concept WHERE uuid = '1732AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA')
        JOIN concept per ON per.concept_id = o_per.value_coded
        WHERE amt.person_id = p.person_id AND amt.voided = 0
          AND amt.concept_id = (SELECT concept_id FROM concept WHERE uuid = 'ba4b8a87-2ce8-559e-9a30-45504a9b6c1f')
        ORDER BY e_amt.encounter_datetime DESC, amt.obs_id DESC LIMIT 1
    )                                                               AS next_consultation_date,

    -- No prescription also needs the latest consultation to prescribe none in force, as the table is rebuilt nightly.
    CASE
        WHEN (MAX(adh.injection_interval_days) IS NULL
              OR MAX(adh.regimen_concept_id) = (SELECT concept_id FROM concept WHERE uuid = '1107AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA'))
         AND NOT EXISTS (
                SELECT 1
                FROM obs o_rx
                WHERE o_rx.voided = 0
                  AND o_rx.concept_id = (SELECT concept_id FROM concept WHERE uuid = '668e0221-8b41-5669-9ad8-78e193d42494')
                  AND o_rx.value_coded <> (SELECT concept_id FROM concept WHERE uuid = '1107AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA')
                  AND o_rx.encounter_id = (
                        SELECT o_last.encounter_id
                        FROM obs o_last
                        WHERE o_last.person_id = p.person_id AND o_last.voided = 0 AND o_last.value_coded IS NOT NULL
                          AND o_last.concept_id = (SELECT concept_id FROM concept WHERE uuid = '668e0221-8b41-5669-9ad8-78e193d42494')
                        ORDER BY o_last.obs_datetime DESC, o_last.obs_id DESC LIMIT 1
                      )
                  -- Any Date Stopped, even a future one, stops the prescription, as ACT Core and ACT 2.0 count it.
                  AND NOT EXISTS (
                        SELECT 1 FROM obs o_stop
                        WHERE o_stop.obs_group_id = o_rx.obs_group_id AND o_stop.voided = 0
                          AND o_stop.concept_id = (SELECT concept_id FROM concept WHERE uuid = 'd75edc42-3213-5a06-9228-4e5735b9594b')
                      )
             ) THEN 'No prescription'
        -- ACT 2.0's BPG status chip counted whole days to a due date at midnight, so 7 days out is approaching.
        WHEN MAX(adh.injection_interval_days) > 0 AND MAX(adh.next_due) IS NOT NULL THEN
            CASE WHEN DATEDIFF(MAX(adh.next_due), CURDATE()) < 0 THEN 'Not covered'
                 WHEN DATEDIFF(MAX(adh.next_due), CURDATE()) <= 7 THEN 'Deadline approaching'
                 ELSE 'Covered' END
    END                                                             AS bpg_status,
    DATE(MAX(adh.last_given))                                       AS last_injection_date,
    DATE(MAX(adh.next_due))                                         AS next_due_date,
    DATEDIFF(MAX(adh.next_due), CURDATE())                          AS days_until_due,
    ROUND(MAX(adh.adherence) * 100)                                 AS adherence,

    -- Clinics, from the Assigned Cardiac Clinic and Health Center location attributes
    MAX(cardiac_loc.name)                                           AS cardiac_clinic,
    MAX(primary_loc.name)                                           AS primary_care_clinic,

    -- The flags whose patient lists the patient is on now; ACT Core gives each list its flag's uuid
    (
        SELECT GROUP_CONCAT(f.name ORDER BY f.name SEPARATOR '|')
        FROM cohort_member cm
        JOIN cohort c ON c.cohort_id = cm.cohort_id AND c.voided = 0
        JOIN patientflags_flag f ON f.uuid = c.uuid AND f.retired = 0
        WHERE cm.patient_id = p.person_id AND cm.voided = 0 AND cm.end_date IS NULL
    )                                                               AS rhd_flags,

    -- Each of those flags with the day the patient joined its list, as name=YYYY-MM-DD
    (
        SELECT GROUP_CONCAT(CONCAT(f.name, '=', DATE(cm.start_date)) ORDER BY f.name SEPARATOR '|')
        FROM cohort_member cm
        JOIN cohort c ON c.cohort_id = cm.cohort_id AND c.voided = 0
        JOIN patientflags_flag f ON f.uuid = c.uuid AND f.retired = 0
        WHERE cm.patient_id = p.person_id AND cm.voided = 0 AND cm.end_date IS NULL
    )                                                               AS rhd_flag_dates,

    -- Registry consent, from the Consent Given person attribute; empty when it was never recorded
    CASE MAX(consent_answer.uuid)
        WHEN 'cf82933b-3f3f-45e7-a5ab-5d31aaee3da3' THEN 'Yes'
        WHEN '488b58ff-64f5-4f8a-8979-fa79940b1594' THEN 'No'
    END                                                             AS consent_given,

    p.uuid                                                          AS patient_uuid

FROM patient_program pp

-- Only the RHD Registry program
JOIN program pw ON pw.program_id = pp.program_id AND pw.retired = 0
    AND pw.uuid = '7d73e143-a550-5a9d-aecd-dd771add098d'

JOIN patient pat ON pat.patient_id = pp.patient_id AND pat.voided = 0
JOIN person p    ON p.person_id    = pp.patient_id  AND p.voided = 0

LEFT JOIN person_name pn ON pn.person_id = p.person_id AND pn.voided = 0 AND pn.preferred = 1

-- RHD ID
LEFT JOIN patient_identifier rhd_id
    ON rhd_id.patient_id = p.person_id AND rhd_id.voided = 0
    AND rhd_id.identifier_type = (SELECT patient_identifier_type_id FROM patient_identifier_type
                                   WHERE uuid = '240f85fa-46e1-540e-9234-2796c623f7ea')
-- External ID
LEFT JOIN patient_identifier ext_id
    ON ext_id.patient_id = p.person_id AND ext_id.voided = 0
    AND ext_id.identifier_type = (SELECT patient_identifier_type_id FROM patient_identifier_type
                                   WHERE uuid = '810bfaee-85de-5a81-b79c-954774076594')
-- National ID
LEFT JOIN patient_identifier nat_id
    ON nat_id.patient_id = p.person_id AND nat_id.voided = 0
    AND nat_id.identifier_type = (SELECT patient_identifier_type_id FROM patient_identifier_type
                                   WHERE uuid = 'fdd6f720-743b-57fc-915a-87c0f571097b')

-- Person attributes
LEFT JOIN person_attribute pa_phone
    ON pa_phone.person_id = p.person_id AND pa_phone.voided = 0
    AND pa_phone.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type
                                              WHERE name = 'Telephone Number' LIMIT 1)
LEFT JOIN person_attribute pa_village
    ON pa_village.person_id = p.person_id AND pa_village.voided = 0
    AND pa_village.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type
                                                WHERE name = 'Health Center' LIMIT 1)
LEFT JOIN person_attribute pa_cardiac
    ON pa_cardiac.person_id = p.person_id AND pa_cardiac.voided = 0
    AND pa_cardiac.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type
                                                WHERE uuid = 'fe261119-2911-5b36-be40-8f9827826987')
LEFT JOIN location cardiac_loc ON cardiac_loc.location_id = pa_cardiac.value
LEFT JOIN location primary_loc ON primary_loc.location_id = pa_village.value
LEFT JOIN person_attribute pa_consent
    ON pa_consent.person_id = p.person_id AND pa_consent.voided = 0
    AND pa_consent.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type
                                                WHERE uuid = 'e15bf9b7-249e-5d75-907d-937b8d9b0c46')
LEFT JOIN concept consent_answer ON consent_answer.concept_id = pa_consent.value
LEFT JOIN actcore_prophylaxis_adherence adh ON adh.patient_id = p.person_id

WHERE
    pp.voided = 0
    AND NOT EXISTS (
        SELECT 1 FROM patient_program later
        WHERE later.patient_id = pp.patient_id AND later.program_id = pp.program_id AND later.voided = 0
          AND (later.date_enrolled > pp.date_enrolled
               OR (later.date_enrolled = pp.date_enrolled AND later.patient_program_id > pp.patient_program_id))
    )
    AND DATE(pp.date_enrolled) >= @startDate
    AND DATE(pp.date_enrolled) <= @endDate

GROUP BY pp.patient_program_id, pp.date_enrolled, pp.date_completed, p.person_id, p.gender, p.birthdate, p.uuid

ORDER BY rhd_id, full_name
