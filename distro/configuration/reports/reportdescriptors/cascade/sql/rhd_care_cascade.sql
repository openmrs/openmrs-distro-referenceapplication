-- =============================================================================
-- RHD Care Cascade
-- Step names are the ACT Registry prototype's; ACT home's widget matches them by name.
-- Parameter: @endDate
-- =============================================================================
WITH rhd_active AS (
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
latest_diagnosis AS (
    SELECT d.person_id, d.obs_id FROM diagnosis d
    WHERE NOT EXISTS (SELECT 1 FROM diagnosis later
                      WHERE later.person_id = d.person_id
                        AND (later.obs_datetime > d.obs_datetime OR (later.obs_datetime = d.obs_datetime AND later.obs_id > d.obs_id)))
),
-- Active, as ACT 2.0 counted it: enrolled, with Rheumatic Heart Disease/Rheumatic Fever as the diagnosis's category.
rhd AS (
    SELECT a.patient_id FROM rhd_active a
    JOIN latest_diagnosis ld ON ld.person_id = a.patient_id
    JOIN obs cat ON cat.obs_group_id = ld.obs_id AND cat.voided = 0
     AND cat.concept_id = (SELECT concept_id FROM concept WHERE uuid = '1a5aa050-661d-5e89-95d7-c1eba476df22')
     AND cat.value_coded = (SELECT concept_id FROM concept WHERE uuid = '4e529463-2036-5327-ad69-7cbd39df86a1')
),
-- The regimens the latest consultation recording one prescribes, less those with a Date Stopped in their group,
-- as ACT Core and the registry count them.
sap_encounter AS (
    SELECT o.person_id,
           SUBSTRING_INDEX(GROUP_CONCAT(o.encounter_id ORDER BY o.obs_datetime DESC, o.obs_id DESC), ',', 1) AS encounter_id
    FROM obs o
    JOIN rhd r ON r.patient_id = o.person_id
    WHERE o.voided = 0 AND o.value_coded IS NOT NULL AND o.obs_datetime <= @endDate
      AND o.concept_id = (SELECT concept_id FROM concept WHERE uuid = '668e0221-8b41-5669-9ad8-78e193d42494')
    GROUP BY o.person_id
),
sap_in_force AS (
    SELECT o.person_id, ans.uuid AS regimen_uuid
    FROM sap_encounter s
    JOIN obs o ON o.person_id = s.person_id AND o.encounter_id = s.encounter_id AND o.voided = 0 AND o.value_coded IS NOT NULL
     AND o.concept_id = (SELECT concept_id FROM concept WHERE uuid = '668e0221-8b41-5669-9ad8-78e193d42494')
    JOIN concept ans ON ans.concept_id = o.value_coded
    WHERE NOT EXISTS (SELECT 1 FROM obs st WHERE st.obs_group_id = o.obs_group_id AND st.voided = 0
                        AND st.concept_id = (SELECT concept_id FROM concept WHERE uuid = 'd75edc42-3213-5a06-9228-4e5735b9594b'))
),
prescribed AS (
    SELECT DISTINCT person_id, regimen_uuid FROM sap_in_force
    WHERE regimen_uuid IN ('50be4b26-6c5b-5aaa-9254-3bfd313b4522','2f3ee632-dd14-51b0-a4ec-de10e7958019',
                           '91230c88-6a90-5d45-af50-fa6155fe5dd7','b9884219-358a-594b-94f5-f8a8863a25f3',
                           'f2e06eb2-5c25-5e53-9e01-246112d05976','fb8b6676-689b-5daa-83f7-1456210c587f',
                           'f6f25d63-bd1a-51cb-9596-e74a19759429','1af922b8-acee-56c8-b184-eaef4e58e23a')
),
bpg AS (
    SELECT DISTINCT person_id FROM prescribed
    WHERE regimen_uuid IN ('50be4b26-6c5b-5aaa-9254-3bfd313b4522','2f3ee632-dd14-51b0-a4ec-de10e7958019',
                           '91230c88-6a90-5d45-af50-fa6155fe5dd7')
),
-- Initiated, as ACT 2.0 counted it: a BPG injection recorded.
initiated AS (
    SELECT b.person_id FROM bpg b
    WHERE EXISTS (SELECT 1 FROM obs d WHERE d.person_id = b.person_id AND d.voided = 0
                    AND d.concept_id = (SELECT concept_id FROM concept WHERE uuid = '183fb30e-b861-5b7c-806f-7118a40f2b51')
                    AND d.value_datetime IS NOT NULL AND d.obs_datetime <= @endDate)
),
-- ACT Core's adherence as of its last run, not the end date.
adherence AS (
    SELECT b.person_id, a.adherence, a.next_due
    FROM bpg b
    JOIN actcore_prophylaxis_adherence a ON a.patient_id = b.person_id
)
SELECT 1 AS step_order, 'Active' AS step, COUNT(*) AS patients FROM rhd
UNION ALL
SELECT 2, 'Prescribed', COUNT(DISTINCT person_id) FROM prescribed
UNION ALL
SELECT 3, 'Oral', COUNT(DISTINCT person_id) FROM prescribed
 WHERE regimen_uuid IN ('b9884219-358a-594b-94f5-f8a8863a25f3','f2e06eb2-5c25-5e53-9e01-246112d05976',
                        'fb8b6676-689b-5daa-83f7-1456210c587f','f6f25d63-bd1a-51cb-9596-e74a19759429',
                        '1af922b8-acee-56c8-b184-eaef4e58e23a')
UNION ALL
SELECT 4, 'BPG', COUNT(*) FROM bpg
UNION ALL
SELECT 5, 'Initiated', COUNT(*) FROM initiated
UNION ALL
SELECT 6, 'Covered today', COUNT(*) FROM initiated i
 JOIN adherence a ON a.person_id = i.person_id
 WHERE DATE(a.next_due) >= DATE(@endDate)
UNION ALL
SELECT 7, 'Adherent (80%+)', COUNT(*) FROM adherence WHERE adherence >= 0.8
ORDER BY step_order
