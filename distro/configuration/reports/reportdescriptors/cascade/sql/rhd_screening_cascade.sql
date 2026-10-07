-- =============================================================================
-- RHD Screening Cascade
-- ACT 2.0's screening cascade (dashboard_query.py): Active as the care cascade counts it, then the patients who
-- screened positive (Screen + is Yes), those of them diagnosed, and those diagnosed with heart disease.
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
-- Each patient's latest Diagnosis group by the end date that is not answered inactive or secondary, as ACT 2.0
-- took the active primary diagnosis (unanswered counts, as those questions did not load before 2026-10-07).
diagnosis AS (
    SELECT g.person_id, g.obs_id, g.obs_datetime
    FROM obs g
    WHERE g.voided = 0 AND g.obs_datetime <= @endDate
      AND g.concept_id = (SELECT concept_id FROM concept WHERE uuid = '594b4495-36dc-52a6-9810-15a9e2e2dcb9')
      AND NOT EXISTS (
          SELECT 1 FROM obs x
          WHERE x.obs_group_id = g.obs_id AND x.voided = 0
            AND ((x.concept_id = (SELECT concept_id FROM concept WHERE uuid = 'b1b5d279-8013-5ae4-83a7-0f2bd9bc8457')
                  AND x.value_coded = (SELECT concept_id FROM concept WHERE uuid = '488b58ff-64f5-4f8a-8979-fa79940b1594'))
              OR (x.concept_id = (SELECT concept_id FROM concept WHERE uuid = '97c025b2-f42c-5c53-8aaa-0506c8dd3774')
                  AND x.value_coded = (SELECT concept_id FROM concept WHERE uuid = 'af45fb2a-ed18-5beb-935e-8e4d92df2dac'))))
),
category AS (
    SELECT d.person_id, ans.uuid AS cat_uuid
    FROM diagnosis d
    JOIN obs cat ON cat.obs_group_id = d.obs_id AND cat.voided = 0 AND cat.value_coded IS NOT NULL
     AND cat.concept_id = (SELECT concept_id FROM concept WHERE uuid = '1a5aa050-661d-5e89-95d7-c1eba476df22')
    JOIN concept ans ON ans.concept_id = cat.value_coded
    WHERE NOT EXISTS (SELECT 1 FROM diagnosis later
                      WHERE later.person_id = d.person_id
                        AND (later.obs_datetime > d.obs_datetime OR (later.obs_datetime = d.obs_datetime AND later.obs_id > d.obs_id)))
),
-- The patient's latest Screen + answer by the end date is Yes.
screened_positive AS (
    SELECT a.patient_id
    FROM scr_active a
    JOIN obs s ON s.person_id = a.patient_id AND s.voided = 0 AND s.obs_datetime <= @endDate
     AND s.concept_id = (SELECT concept_id FROM concept WHERE uuid = '82a5fbb5-038d-57b8-82d1-3fd0ac95673d')
     AND s.value_coded = (SELECT concept_id FROM concept WHERE uuid = 'cf82933b-3f3f-45e7-a5ab-5d31aaee3da3')
    WHERE NOT EXISTS (SELECT 1 FROM obs later
                      WHERE later.person_id = s.person_id AND later.concept_id = s.concept_id AND later.voided = 0
                        AND later.obs_datetime <= @endDate
                        AND (later.obs_datetime > s.obs_datetime OR (later.obs_datetime = s.obs_datetime AND later.obs_id > s.obs_id)))
)
SELECT 1 AS step_order, 'Active' AS step, COUNT(*) AS patients
  FROM scr_active a JOIN category c ON c.person_id = a.patient_id
 WHERE c.cat_uuid = '4e529463-2036-5327-ad69-7cbd39df86a1'
UNION ALL
SELECT 2, 'Confirmatory Diagnosis', COUNT(*) FROM screened_positive
UNION ALL
SELECT 3, 'Diagnosed', COUNT(*) FROM screened_positive s JOIN category c ON c.person_id = s.patient_id
UNION ALL
SELECT 4, 'Heart Disease', COUNT(*) FROM screened_positive s JOIN category c ON c.person_id = s.patient_id
 WHERE c.cat_uuid IN ('4e529463-2036-5327-ad69-7cbd39df86a1','365e75bb-35bc-503f-b8db-53bea2528d1b',
                      '7c42e9be-5828-5d4c-8a6e-f713efe8a945')
ORDER BY step_order
