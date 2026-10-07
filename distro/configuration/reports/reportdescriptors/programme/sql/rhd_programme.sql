-- =============================================================================
-- RHD Programme Report
-- One row per primary care clinic with an active patient in scope or an injection in the period, for the
-- Reports and Facility reports pages, whose tiles are the column totals; "No primary care clinic" for patients
-- without one. A clinic with neither is left out rather than shown as an empty, complete row.
-- Active: living patients whose latest RHD Registry enrolment is open, as the due list counts them.
-- Scope: an optional cardiac clinic and an optional primary care clinic, each a location uuid, as ACT 2.0's
-- dashboard filtered on each; both blank means every clinic.
-- Due this week: next due date from today to 7 days ahead, the window of the registry's "Deadline approaching",
-- over the due list's patients, so an oral regimen counts as it does in Overdue; the registry's chip is BPG only.
-- Overdue: next due date before today. Both with the due list's regimens, from ACT Core's adherence table.
-- BPG on time and timed: injections from startDate to endDate, timed by ACT Core as the chart times them, of
-- every patient in scope whatever their status now, so a past period's rate does not change as patients leave.
-- Adherence: the mean of ACT Core's adherence, as of its last run.
-- Data tag: Duplicates when a patient shares their name and birth date with another active patient at any
-- clinic, else Review when a patient is on a list of a flag of priority RHD Data Quality, else Complete.
-- =============================================================================
WITH active AS (
    SELECT
        p.person_id,
        (SELECT MAX(pa.value) FROM person_attribute pa
         WHERE pa.person_id = p.person_id AND pa.voided = 0
           AND pa.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type WHERE name = 'Health Center' LIMIT 1))                 AS facility_id,
        CASE WHEN a.next_due BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 7 DAY) AND (regimen.uuid IS NULL OR regimen.uuid IN (
                 '50be4b26-6c5b-5aaa-9254-3bfd313b4522','2f3ee632-dd14-51b0-a4ec-de10e7958019',
                 '91230c88-6a90-5d45-af50-fa6155fe5dd7',
                 'b9884219-358a-594b-94f5-f8a8863a25f3','f2e06eb2-5c25-5e53-9e01-246112d05976',
                 'fb8b6676-689b-5daa-83f7-1456210c587f','f6f25d63-bd1a-51cb-9596-e74a19759429',
                 '1af922b8-acee-56c8-b184-eaef4e58e23a'))
             THEN 1 ELSE 0 END                                      AS due_this_week,
        CASE WHEN a.next_due < CURDATE() AND (regimen.uuid IS NULL OR regimen.uuid IN (
                 '50be4b26-6c5b-5aaa-9254-3bfd313b4522','2f3ee632-dd14-51b0-a4ec-de10e7958019',
                 '91230c88-6a90-5d45-af50-fa6155fe5dd7',
                 'b9884219-358a-594b-94f5-f8a8863a25f3','f2e06eb2-5c25-5e53-9e01-246112d05976',
                 'fb8b6676-689b-5daa-83f7-1456210c587f','f6f25d63-bd1a-51cb-9596-e74a19759429',
                 '1af922b8-acee-56c8-b184-eaef4e58e23a'))
             THEN 1 ELSE 0 END                                      AS overdue,
        a.adherence                                                 AS adherence,
        CASE WHEN EXISTS (
            SELECT 1 FROM cohort_member cm
            JOIN cohort c ON c.cohort_id = cm.cohort_id AND c.voided = 0
            JOIN patientflags_flag f ON f.uuid = c.uuid AND f.retired = 0
            JOIN patientflags_priority fp ON fp.priority_id = f.priority_id AND fp.name = 'RHD Data Quality'
            WHERE cm.patient_id = p.person_id AND cm.voided = 0 AND cm.end_date IS NULL
        ) THEN 1 ELSE 0 END                                         AS review,
        CASE WHEN p.birthdate IS NOT NULL AND EXISTS (
            SELECT 1 FROM person p2
            JOIN person_name n2 ON n2.person_id = p2.person_id AND n2.voided = 0 AND n2.preferred = 1
            JOIN person_name n1 ON n1.person_id = p.person_id AND n1.voided = 0 AND n1.preferred = 1
            JOIN patient_program pp2 ON pp2.patient_id = p2.person_id AND pp2.program_id = pp.program_id
                AND pp2.voided = 0 AND pp2.date_completed IS NULL
            WHERE p2.person_id <> p.person_id AND p2.voided = 0 AND p2.dead = 0 AND p2.birthdate = p.birthdate
              AND LOWER(n2.given_name) = LOWER(n1.given_name) AND LOWER(n2.family_name) = LOWER(n1.family_name)
        ) THEN 1 ELSE 0 END                                         AS duplicate
    FROM patient_program pp
    JOIN program pw ON pw.program_id = pp.program_id AND pw.retired = 0
        AND pw.uuid = '7d73e143-a550-5a9d-aecd-dd771add098d'
    JOIN patient pat ON pat.patient_id = pp.patient_id AND pat.voided = 0
    JOIN person p    ON p.person_id    = pp.patient_id  AND p.voided = 0 AND p.dead = 0
    LEFT JOIN actcore_prophylaxis_adherence a ON a.patient_id = p.person_id
    LEFT JOIN concept regimen ON regimen.concept_id = a.regimen_concept_id
    WHERE pp.voided = 0
        AND pp.date_completed IS NULL
        -- The patient's latest enrolment, as the registry report takes it
        AND NOT EXISTS (
            SELECT 1 FROM patient_program later
            WHERE later.patient_id = pp.patient_id AND later.program_id = pp.program_id AND later.voided = 0
              AND (later.date_enrolled > pp.date_enrolled
                   OR (later.date_enrolled = pp.date_enrolled AND later.patient_program_id > pp.patient_program_id))
        )
        AND (@cardiacClinic IS NULL OR @cardiacClinic = '' OR EXISTS (
            SELECT 1 FROM person_attribute pa
            JOIN location l ON l.location_id = pa.value AND l.uuid = @cardiacClinic
            WHERE pa.person_id = p.person_id AND pa.voided = 0
              AND pa.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type
                                                  WHERE uuid = 'fe261119-2911-5b36-be40-8f9827826987')
        ))
        AND (@primaryCareClinic IS NULL OR @primaryCareClinic = '' OR EXISTS (
            SELECT 1 FROM person_attribute pa
            JOIN location l ON l.location_id = pa.value AND l.uuid = @primaryCareClinic
            WHERE pa.person_id = p.person_id AND pa.voided = 0
              AND pa.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type WHERE name = 'Health Center' LIMIT 1)
        ))
),
-- Injections in the period of any patient in scope, by the clinic recorded for them now
timed AS (
    SELECT
        (SELECT MAX(pa.value) FROM person_attribute pa
         WHERE pa.person_id = t.patient_id AND pa.voided = 0
           AND pa.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type WHERE name = 'Health Center' LIMIT 1))                 AS facility_id,
        SUM(t.on_time)                                              AS bpg_on_time,
        COUNT(*)                                                    AS bpg_timed
    FROM actcore_injection_timing t
    JOIN person p ON p.person_id = t.patient_id AND p.voided = 0
    WHERE t.injection_date BETWEEN DATE(@startDate) AND DATE(@endDate)
        AND (@cardiacClinic IS NULL OR @cardiacClinic = '' OR EXISTS (
            SELECT 1 FROM person_attribute pa
            JOIN location l ON l.location_id = pa.value AND l.uuid = @cardiacClinic
            WHERE pa.person_id = t.patient_id AND pa.voided = 0
              AND pa.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type
                                                  WHERE uuid = 'fe261119-2911-5b36-be40-8f9827826987')
        ))
        AND (@primaryCareClinic IS NULL OR @primaryCareClinic = '' OR EXISTS (
            SELECT 1 FROM person_attribute pa
            JOIN location l ON l.location_id = pa.value AND l.uuid = @primaryCareClinic
            WHERE pa.person_id = t.patient_id AND pa.voided = 0
              AND pa.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type WHERE name = 'Health Center' LIMIT 1)
        ))
    GROUP BY facility_id
),
facilities AS (
    SELECT facility_id FROM active
    UNION SELECT facility_id FROM timed
)
SELECT
    COALESCE(fac.name, 'No primary care clinic')                    AS facility,
    fac.uuid                                                        AS facility_uuid,
    (SELECT COUNT(*) FROM active s WHERE s.facility_id <=> f.facility_id) AS active_patients,
    (SELECT COALESCE(SUM(s.due_this_week), 0) FROM active s WHERE s.facility_id <=> f.facility_id) AS due_this_week,
    (SELECT COALESCE(SUM(s.overdue), 0) FROM active s WHERE s.facility_id <=> f.facility_id) AS overdue,
    COALESCE(tm.bpg_on_time, 0)                                     AS bpg_on_time,
    COALESCE(tm.bpg_timed, 0)                                       AS bpg_timed,
    (SELECT ROUND(AVG(s.adherence) * 100) FROM active s WHERE s.facility_id <=> f.facility_id) AS adherence,
    CASE WHEN EXISTS (SELECT 1 FROM active s WHERE s.facility_id <=> f.facility_id AND s.duplicate = 1) THEN 'Duplicates'
         WHEN EXISTS (SELECT 1 FROM active s WHERE s.facility_id <=> f.facility_id AND s.review = 1) THEN 'Review'
         ELSE 'Complete' END                                        AS data_tag
FROM facilities f
LEFT JOIN location fac ON fac.location_id = f.facility_id
LEFT JOIN timed tm ON tm.facility_id <=> f.facility_id

ORDER BY fac.name IS NULL, fac.name
