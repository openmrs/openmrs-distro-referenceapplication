-- =============================================================================
-- RHD Encounters Export
-- One row per encounter across all RHD encounter types.
-- Parameters: @startDate, @endDate
-- =============================================================================
SELECT
    rhd_id.identifier                                                   AS rhd_id,
    CONCAT(pn.given_name, ' ', pn.family_name)                          AS patient_name,
    p.gender                                                            AS sex,
    TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime)              AS age_at_encounter,
    et.name                                                             AS encounter_type,
    DATE(e.encounter_datetime)                                          AS encounter_date,
    DATE_FORMAT(e.encounter_datetime, '%H:%i')                          AS encounter_time,
    l.name                                                              AS location,

    -- Provider (first listed)
    (
        SELECT CONCAT(pvn.given_name, ' ', pvn.family_name)
        FROM encounter_provider ep
        JOIN provider pv ON pv.provider_id = ep.provider_id AND pv.retired = 0
        JOIN person pvp ON pvp.person_id = pv.person_id AND pvp.voided = 0
        JOIN person_name pvn ON pvn.person_id = pvp.person_id AND pvn.voided = 0 AND pvn.preferred = 1
        WHERE ep.encounter_id = e.encounter_id AND ep.voided = 0
        LIMIT 1
    )                                                                   AS provider,

    -- Visit info
    v.uuid                                                              AS visit_uuid,
    DATE(v.date_started)                                                AS visit_date,

    -- Encounter-type-specific key fields
    -- Consultation: NYHA class
    CASE WHEN et.uuid = 'c2503561-c00d-5460-8157-43d594472b4a' THEN
        (SELECT cn_nyha.name FROM obs o_nyha
         JOIN concept_name cn_nyha ON cn_nyha.concept_id = o_nyha.value_coded
             AND cn_nyha.locale = 'en' AND cn_nyha.locale_preferred = 1 AND cn_nyha.voided = 0
         WHERE o_nyha.encounter_id = e.encounter_id AND o_nyha.voided = 0
           AND o_nyha.concept_id = (SELECT concept_id FROM concept WHERE uuid = '2f51b894-383c-53bb-9997-583e9761d132')
         LIMIT 1)
    END                                                                 AS nyha_class,

    -- INR monitoring: INR value
    CASE WHEN et.uuid = 'b4bb88a3-9a04-5142-85bd-bb63c270f632' THEN
        (SELECT o_inr.value_numeric FROM obs o_inr
         WHERE o_inr.encounter_id = e.encounter_id AND o_inr.voided = 0
           AND o_inr.concept_id = (SELECT concept_id FROM concept WHERE uuid = '161482AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA')
         ORDER BY o_inr.obs_datetime DESC LIMIT 1)
    END                                                                 AS latest_inr_value,

    -- BPG: any adverse reaction recorded
    CASE WHEN et.uuid = '04cf03db-3b8e-5020-84b0-50b06338767a' THEN
        (SELECT IF(COUNT(*) > 0, 'Yes', 'No') FROM obs o_rx
         WHERE o_rx.encounter_id = e.encounter_id AND o_rx.voided = 0
           AND o_rx.concept_id = (SELECT concept_id FROM concept WHERE uuid = '8200713e-8647-52f0-b93d-841e4e9687b7'))
    END                                                                 AS bpg_adverse_reaction,

    e.uuid                                                              AS encounter_uuid

FROM encounter e

JOIN encounter_type et ON et.encounter_type_id = e.encounter_type
    AND et.retired = 0
    AND et.uuid IN (
        'c2503561-c00d-5460-8157-43d594472b4a',
        '04cf03db-3b8e-5020-84b0-50b06338767a',
        '730f5ec2-7102-55d0-8602-2d792844f245',
        '64c3f35f-a3ec-59d6-8178-0ca9f068cda8',
        '181e0106-35d3-537a-a25e-18d3ebf61883',
        'ce6b111e-9beb-5a91-91ad-ec5043976fd5',
        '55271793-ef37-58da-9d86-1d9092a5a809',
        'c9b87090-8768-50be-987e-8ca0a8983429',
        'b4bb88a3-9a04-5142-85bd-bb63c270f632'
    )

JOIN patient pat ON pat.patient_id = e.patient_id AND pat.voided = 0
JOIN person p    ON p.person_id    = e.patient_id  AND p.voided = 0

LEFT JOIN person_name pn ON pn.person_id = p.person_id AND pn.voided = 0 AND pn.preferred = 1
LEFT JOIN location l     ON l.location_id = e.location_id
LEFT JOIN visit v        ON v.visit_id = e.visit_id AND v.voided = 0

LEFT JOIN patient_identifier rhd_id
    ON rhd_id.patient_id = e.patient_id AND rhd_id.voided = 0
    AND rhd_id.identifier_type = (SELECT patient_identifier_type_id FROM patient_identifier_type
                                   WHERE uuid = '240f85fa-46e1-540e-9234-2796c623f7ea')

WHERE
    e.voided = 0
    AND e.encounter_datetime >= @startDate
    AND e.encounter_datetime < DATE_ADD(@endDate, INTERVAL 1 DAY)

ORDER BY e.encounter_datetime DESC, rhd_id.identifier
