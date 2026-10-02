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
      AND EXISTS (
          SELECT 1 FROM obs o
          JOIN concept ans ON ans.concept_id = o.value_coded
          WHERE o.person_id = pp.patient_id AND o.voided = 0
            AND o.concept_id = (SELECT concept_id FROM concept WHERE uuid = '1a5aa050-661d-5e89-95d7-c1eba476df22')
            AND o.obs_datetime <= @endDate
            AND ans.uuid IN ('7cfaaf46-1939-5437-8d40-31095cb29812','2a648c80-594c-5442-bdfc-70e7472ef5b7',
                             '5458f2f7-9eba-5ba4-b2a8-6c736d574ca9','19fb7b40-528e-5b1c-a610-f2e624c98038',
                             'cbfcf051-bee3-549d-b233-e5be0e7d691a'))
),
sap_latest AS (
    SELECT o.person_id,
           SUBSTRING_INDEX(GROUP_CONCAT(ans.uuid ORDER BY o.obs_datetime DESC, o.obs_id DESC), ',', 1) AS regimen_uuid
    FROM obs o
    JOIN concept ans ON ans.concept_id = o.value_coded
    JOIN rhd_active a ON a.patient_id = o.person_id
    WHERE o.voided = 0
      AND o.concept_id = (SELECT concept_id FROM concept WHERE uuid = '668e0221-8b41-5669-9ad8-78e193d42494')
      AND o.obs_datetime <= @endDate
    GROUP BY o.person_id
),
prescribed AS (
    SELECT person_id, regimen_uuid FROM sap_latest
    WHERE regimen_uuid IN ('50be4b26-6c5b-5aaa-9254-3bfd313b4522','2f3ee632-dd14-51b0-a4ec-de10e7958019',
                           '91230c88-6a90-5d45-af50-fa6155fe5dd7','b9884219-358a-594b-94f5-f8a8863a25f3',
                           'f2e06eb2-5c25-5e53-9e01-246112d05976','fb8b6676-689b-5daa-83f7-1456210c587f',
                           'f6f25d63-bd1a-51cb-9596-e74a19759429','1af922b8-acee-56c8-b184-eaef4e58e23a')
),
bpg AS (
    SELECT person_id FROM prescribed
    WHERE regimen_uuid IN ('50be4b26-6c5b-5aaa-9254-3bfd313b4522','2f3ee632-dd14-51b0-a4ec-de10e7958019',
                           '91230c88-6a90-5d45-af50-fa6155fe5dd7')
),
initiated AS (
    SELECT b.person_id FROM bpg b
    WHERE EXISTS (SELECT 1 FROM obs d WHERE d.person_id = b.person_id AND d.voided = 0
                    AND d.concept_id = (SELECT concept_id FROM concept WHERE uuid = '5bcc7d12-b279-5955-815c-090a1f392071')
                    AND d.value_datetime IS NOT NULL AND d.obs_datetime <= @endDate)
),
-- ACT Core's adherence as of its last run, not the end date.
adherence AS (
    SELECT b.person_id, a.adherence, a.next_due
    FROM bpg b
    JOIN actcore_prophylaxis_adherence a ON a.patient_id = b.person_id
)
SELECT 1 AS step_order, 'Active' AS step, COUNT(*) AS patients FROM rhd_active
UNION ALL
SELECT 2, 'Prescribed', COUNT(*) FROM prescribed
UNION ALL
SELECT 3, 'Oral', COUNT(*) FROM prescribed
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
