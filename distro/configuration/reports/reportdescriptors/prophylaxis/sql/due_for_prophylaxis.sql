-- =============================================================================
-- Due for Prophylaxis
-- One row per living patient enrolled in the RHD Registry now whose next dose, in ACT Core's
-- actcore_prophylaxis_adherence, is due today or overdue. That table is rebuilt by ACT Core's daily
-- adherence refresh, so a dose recorded today shows after the next run.
-- =============================================================================
SELECT
    p.uuid                                                          AS patient_uuid,
    MAX(CONCAT(pn.given_name, ' ', pn.family_name))                 AS full_name,
    MAX(rhd_id.identifier)                                          AS rhd_id,
    -- Typed as ACT Core's chart types it: an injection interval means BPG
    CASE WHEN a.injection_interval_days > 0 THEN 'BPG' ELSE 'Oral' END AS prophylaxis_type,
    a.last_given                                                    AS last_given,
    a.next_due                                                      AS next_due,
    CASE WHEN a.next_due = CURDATE() THEN 'due_today' ELSE 'overdue' END AS status,
    MAX(primary_loc.name)                                           AS primary_care_clinic

FROM patient_program pp
JOIN program pw ON pw.program_id = pp.program_id AND pw.retired = 0
    AND pw.uuid = '7d73e143-a550-5a9d-aecd-dd771add098d'
JOIN patient pat ON pat.patient_id = pp.patient_id AND pat.voided = 0
JOIN person p    ON p.person_id    = pp.patient_id  AND p.voided = 0 AND p.dead = 0
JOIN actcore_prophylaxis_adherence a ON a.patient_id = p.person_id
LEFT JOIN concept regimen ON regimen.concept_id = a.regimen_concept_id

LEFT JOIN person_name pn ON pn.person_id = p.person_id AND pn.voided = 0 AND pn.preferred = 1
LEFT JOIN patient_identifier rhd_id
    ON rhd_id.patient_id = p.person_id AND rhd_id.voided = 0
    AND rhd_id.identifier_type = (SELECT patient_identifier_type_id FROM patient_identifier_type
                                   WHERE uuid = '240f85fa-46e1-540e-9234-2796c623f7ea')
-- The primary care clinic as the registry report reads it, from the Health Center location attribute
LEFT JOIN person_attribute pa_primary
    ON pa_primary.person_id = p.person_id AND pa_primary.voided = 0
    AND pa_primary.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type
                                                WHERE name = 'Health Center' LIMIT 1)
LEFT JOIN location primary_loc ON primary_loc.location_id = pa_primary.value

WHERE pp.voided = 0
    AND pp.date_completed IS NULL
    -- The patient's latest enrolment, as the registry report takes it
    AND NOT EXISTS (
        SELECT 1 FROM patient_program later
        WHERE later.patient_id = pp.patient_id AND later.program_id = pp.program_id AND later.voided = 0
          AND (later.date_enrolled > pp.date_enrolled
               OR (later.date_enrolled = pp.date_enrolled AND later.patient_program_id > pp.patient_program_id))
    )
    AND a.next_due <= CURDATE()
    -- BPG, oral or no regimen: None, Other and any regimen the cascade does not count are not due
    AND (regimen.uuid IS NULL
         OR regimen.uuid IN ('50be4b26-6c5b-5aaa-9254-3bfd313b4522','2f3ee632-dd14-51b0-a4ec-de10e7958019',
                             '91230c88-6a90-5d45-af50-fa6155fe5dd7',
                             'b9884219-358a-594b-94f5-f8a8863a25f3','f2e06eb2-5c25-5e53-9e01-246112d05976',
                             'fb8b6676-689b-5daa-83f7-1456210c587f','f6f25d63-bd1a-51cb-9596-e74a19759429',
                             '1af922b8-acee-56c8-b184-eaef4e58e23a'))

GROUP BY pp.patient_program_id, p.uuid, a.injection_interval_days, a.last_given, a.next_due

ORDER BY a.next_due, full_name
