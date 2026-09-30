-- =============================================================================
-- RHD Screening Cascade
-- Equivalent to RhdScreeningCascadeReportManager (migrated from Java to YAML descriptor)
-- Parameter: @endDate
-- =============================================================================
WITH scr_active AS (
    SELECT DISTINCT pp.patient_id
    FROM patient_program pp
    -- Enrolled in the RHD Registry on the end date and not completed by then, as the registry report counts
    -- an enrolment active. O3 records no workflow state on enrolment, and entering a terminal state completes it.
    JOIN program pr ON pr.program_id = pp.program_id AND pr.uuid = '7d73e143-a550-5a9d-aecd-dd771add098d'
    WHERE pp.voided = 0
      AND DATE(pp.date_enrolled) <= DATE(@endDate)
      AND (pp.date_completed IS NULL OR DATE(pp.date_completed) > DATE(@endDate))
),
category_obs AS (
    SELECT o.person_id, o.obs_datetime, o.obs_id, ans.uuid AS cat_uuid
    FROM obs o
    JOIN concept ans ON ans.concept_id = o.value_coded
    JOIN scr_active a ON a.patient_id = o.person_id
    WHERE o.voided = 0
      AND o.concept_id = (SELECT concept_id FROM concept WHERE uuid = '1a5aa050-661d-5e89-95d7-c1eba476df22')
      AND o.obs_datetime <= @endDate
),
latest_category AS (
    SELECT person_id,
           SUBSTRING_INDEX(GROUP_CONCAT(cat_uuid ORDER BY obs_datetime DESC, obs_id DESC), ',', 1) AS cat_uuid
    FROM category_obs GROUP BY person_id
)
SELECT 1 AS step_order, 'Active' AS step, COUNT(*) AS patients FROM scr_active
UNION ALL
SELECT 2, 'Screened positive, echo pending', COUNT(DISTINCT person_id) FROM category_obs
 WHERE cat_uuid = '27f33ebe-77fb-575f-b737-00aa47dae6d8'
UNION ALL
SELECT 3, 'Confirmed (echo resulted)', COUNT(*) FROM latest_category
 WHERE cat_uuid <> '27f33ebe-77fb-575f-b737-00aa47dae6d8'
UNION ALL
SELECT 4, 'Heart disease', COUNT(*) FROM latest_category
 WHERE cat_uuid IN ('7cfaaf46-1939-5437-8d40-31095cb29812','2a648c80-594c-5442-bdfc-70e7472ef5b7',
                    '5458f2f7-9eba-5ba4-b2a8-6c736d574ca9','19fb7b40-528e-5b1c-a610-f2e624c98038',
                    'cbfcf051-bee3-549d-b233-e5be0e7d691a','365e75bb-35bc-503f-b8db-53bea2528d1b',
                    '7c42e9be-5828-5d4c-8a6e-f713efe8a945')
ORDER BY step_order
