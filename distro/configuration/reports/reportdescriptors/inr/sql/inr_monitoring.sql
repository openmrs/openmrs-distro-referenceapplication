-- =============================================================================
-- RHD INR Monitoring Export
--
-- Encounter type: RHD INR Monitoring (b4bb88a3-9a04-5142-85bd-bb63c270f632)
-- Concepts used:
--   Indication:    6e8ed53f-15e6-5b00-9ed0-44b11bd2d638
--   INR Group:     0778ce12-ddba-5e22-b5fd-92424e8747b4  (obsGroup)
--   INR Target:    0778ce12-ddba-5e22-b5fd-92424e8747b4  (coded answer in group)
--     Answer 1.5-2.5: 2822a113-1cd3-510e-b5bf-95b9279b1a4f
--     Answer 2.0-3.0: d894d89a-04bd-5fc0-91a0-5dc3ddd8802b
--     Answer 2.5-3.5: ea841a3a-f9cb-5761-aac6-939ffb158d94
--   Date Updated:  be9041b1-3948-5de0-a8b1-974e84b95715
--   INR Value:     161482AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
-- Parameters:     @startDate, @endDate
-- =============================================================================

SELECT
    pid.identifier                                          AS rhd_id,
    CONCAT(pn.given_name, ' ', pn.family_name)             AS patient_name,
    p.gender                                               AS sex,
    TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) AS age_at_visit,
    l.name                                                 AS location,
    DATE(e.encounter_datetime)                             AS encounter_date,

    -- Indication for anticoagulation (free text)
    indication_obs.value_text                              AS indication,

    -- INR reading date (from obs group)
    DATE(inr_date_obs.value_datetime)                      AS inr_date,

    -- INR target range (coded)
    CASE inr_target_obs.value_coded
        WHEN (SELECT concept_id FROM concept WHERE uuid = '2822a113-1cd3-510e-b5bf-95b9279b1a4f') THEN '1.5-2.5'
        WHEN (SELECT concept_id FROM concept WHERE uuid = 'd894d89a-04bd-5fc0-91a0-5dc3ddd8802b') THEN '2.0-3.0'
        WHEN (SELECT concept_id FROM concept WHERE uuid = 'ea841a3a-f9cb-5761-aac6-939ffb158d94') THEN '2.5-3.5'
        ELSE NULL
    END                                                    AS inr_target,

    -- INR numeric result
    inr_value_obs.value_numeric                            AS inr_value,

    -- In-range flag: 1=therapeutic, 0=subtherapeutic/supratherapeutic
    CASE
        WHEN inr_target_obs.value_coded = (SELECT concept_id FROM concept WHERE uuid = '2822a113-1cd3-510e-b5bf-95b9279b1a4f')
             AND inr_value_obs.value_numeric BETWEEN 1.5 AND 2.5 THEN 1
        WHEN inr_target_obs.value_coded = (SELECT concept_id FROM concept WHERE uuid = 'd894d89a-04bd-5fc0-91a0-5dc3ddd8802b')
             AND inr_value_obs.value_numeric BETWEEN 2.0 AND 3.0 THEN 1
        WHEN inr_target_obs.value_coded = (SELECT concept_id FROM concept WHERE uuid = 'ea841a3a-f9cb-5761-aac6-939ffb158d94')
             AND inr_value_obs.value_numeric BETWEEN 2.5 AND 3.5 THEN 1
        ELSE 0
    END                                                    AS in_therapeutic_range,

    e.uuid                                                 AS encounter_uuid

FROM encounter e

-- Encounter type filter: RHD INR Monitoring
INNER JOIN encounter_type et
    ON et.encounter_type_id = e.encounter_type
    AND et.uuid = 'b4bb88a3-9a04-5142-85bd-bb63c270f632'
    AND et.retired = 0

INNER JOIN patient pat ON pat.patient_id = e.patient_id AND pat.voided = 0
INNER JOIN person p    ON p.person_id = e.patient_id    AND p.voided = 0

-- Patient name
LEFT JOIN person_name pn
    ON pn.person_id = e.patient_id
    AND pn.voided = 0
    AND pn.preferred = 1

-- RHD ID (primary identifier)
LEFT JOIN patient_identifier pid
    ON pid.patient_id = e.patient_id
    AND pid.voided = 0
    AND pid.identifier_type = (
        SELECT patient_identifier_type_id
        FROM patient_identifier_type
        WHERE uuid = '240f85fa-46e1-540e-9234-2796c623f7ea'
    )

-- Location
LEFT JOIN location l ON l.location_id = e.location_id

-- Indication (simple obs on encounter, not in group)
LEFT JOIN obs indication_obs
    ON indication_obs.encounter_id = e.encounter_id
    AND indication_obs.voided = 0
    AND indication_obs.concept_id = (SELECT concept_id FROM concept WHERE uuid = '6e8ed53f-15e6-5b00-9ed0-44b11bd2d638')
    AND indication_obs.obs_group_id IS NULL

-- INR obs group (repeating)
LEFT JOIN obs inr_group
    ON inr_group.encounter_id = e.encounter_id
    AND inr_group.voided = 0
    AND inr_group.concept_id = (SELECT concept_id FROM concept WHERE uuid = '0778ce12-ddba-5e22-b5fd-92424e8747b4')
    AND inr_group.obs_group_id IS NULL

-- INR Target (coded, child of group)
LEFT JOIN obs inr_target_obs
    ON inr_target_obs.obs_group_id = inr_group.obs_id
    AND inr_target_obs.voided = 0
    AND inr_target_obs.concept_id = (SELECT concept_id FROM concept WHERE uuid = '0778ce12-ddba-5e22-b5fd-92424e8747b4')

-- INR Date (child of group)
LEFT JOIN obs inr_date_obs
    ON inr_date_obs.obs_group_id = inr_group.obs_id
    AND inr_date_obs.voided = 0
    AND inr_date_obs.concept_id = (SELECT concept_id FROM concept WHERE uuid = 'be9041b1-3948-5de0-a8b1-974e84b95715')

-- INR Numeric Value (child of group)
LEFT JOIN obs inr_value_obs
    ON inr_value_obs.obs_group_id = inr_group.obs_id
    AND inr_value_obs.voided = 0
    AND inr_value_obs.concept_id = (SELECT concept_id FROM concept WHERE uuid = '161482AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA')

WHERE
    e.voided = 0
    AND e.encounter_datetime >= @startDate
    AND e.encounter_datetime < DATE_ADD(@endDate, INTERVAL 1 DAY)

ORDER BY
    pid.identifier,
    e.encounter_datetime,
    inr_group.obs_id
;
