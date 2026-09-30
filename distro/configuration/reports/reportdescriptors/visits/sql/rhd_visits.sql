-- =============================================================================
-- RHD Visits Report
-- One row per visit (OpenMRS visit). Aggregates encounter types seen per visit.
-- Parameters: @startDate, @endDate
-- =============================================================================
SELECT
    rhd_id.identifier                                                   AS rhd_id,
    CONCAT(pn.given_name, ' ', pn.family_name)                          AS patient_name,
    p.gender                                                            AS sex,
    TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started)                    AS age_at_visit,
    DATE(v.date_started)                                                AS visit_date,
    DATE(v.date_stopped)                                                AS visit_end_date,
    vt.name                                                             AS visit_type,
    l.name                                                              AS location,

    -- Comma-separated list of encounter types seen in this visit
    GROUP_CONCAT(DISTINCT et.name ORDER BY e.encounter_datetime SEPARATOR '; ')
                                                                        AS encounter_types,
    COUNT(DISTINCT e.encounter_id)                                      AS encounter_count,

    -- Key clinical observations from any encounter in this visit
    (SELECT cn.name FROM obs o
     JOIN concept_name cn ON cn.concept_id = o.value_coded
         AND cn.locale = 'en' AND cn.locale_preferred = 1 AND cn.voided = 0
     WHERE o.voided = 0 AND o.encounter_id IN (
         SELECT encounter_id FROM encounter WHERE visit_id = v.visit_id AND voided = 0)
       AND o.concept_id = (SELECT concept_id FROM concept WHERE uuid = '2f51b894-383c-53bb-9997-583e9761d132')
     ORDER BY o.obs_datetime DESC LIMIT 1)                              AS nyha_class,

    (SELECT cn2.name FROM obs o2
     JOIN concept_name cn2 ON cn2.concept_id = o2.value_coded
         AND cn2.locale = 'en' AND cn2.locale_preferred = 1 AND cn2.voided = 0
     WHERE o2.voided = 0 AND o2.encounter_id IN (
         SELECT encounter_id FROM encounter WHERE visit_id = v.visit_id AND voided = 0)
       AND o2.concept_id = (SELECT concept_id FROM concept WHERE uuid = '668e0221-8b41-5669-9ad8-78e193d42494')
     ORDER BY o2.obs_datetime DESC LIMIT 1)                             AS prophylaxis_regimen,

    v.uuid                                                              AS visit_uuid

FROM visit v

JOIN visit_type vt ON vt.visit_type_id = v.visit_type_id

JOIN patient pat ON pat.patient_id = v.patient_id AND pat.voided = 0
JOIN person p    ON p.person_id    = v.patient_id  AND p.voided = 0

LEFT JOIN person_name pn ON pn.person_id = p.person_id AND pn.voided = 0 AND pn.preferred = 1
LEFT JOIN location l     ON l.location_id = v.location_id

LEFT JOIN patient_identifier rhd_id
    ON rhd_id.patient_id = v.patient_id AND rhd_id.voided = 0
    AND rhd_id.identifier_type = (SELECT patient_identifier_type_id FROM patient_identifier_type
                                   WHERE uuid = '240f85fa-46e1-540e-9234-2796c623f7ea')

-- Only visits that contain at least one RHD encounter type
JOIN encounter e ON e.visit_id = v.visit_id AND e.voided = 0
JOIN encounter_type et ON et.encounter_type_id = e.encounter_type AND et.retired = 0
    AND et.uuid IN (
        'c2503561-c00d-5460-8157-43d594472b4a',  -- RHD Consultation Visit
        '04cf03db-3b8e-5020-84b0-50b06338767a',  -- RHD BPG Delivery
        '730f5ec2-7102-55d0-8602-2d792844f245',  -- RHD Echocardiogram
        '64c3f35f-a3ec-59d6-8178-0ca9f068cda8',  -- RHD Electrocardiogram
        '181e0106-35d3-537a-a25e-18d3ebf61883',  -- RHD Hospital Admission
        'ce6b111e-9beb-5a91-91ad-ec5043976fd5',  -- RHD Pregnancy
        '55271793-ef37-58da-9d86-1d9092a5a809',  -- RHD Oral Adherence
        'c9b87090-8768-50be-987e-8ca0a8983429',  -- RHD Interventions and Outcomes
        'b4bb88a3-9a04-5142-85bd-bb63c270f632'   -- RHD INR Monitoring
    )

WHERE
    v.voided = 0
    AND DATE(v.date_started) >= @startDate
    AND DATE(v.date_started) <= @endDate

GROUP BY v.visit_id

ORDER BY DATE(v.date_started) DESC, rhd_id.identifier
