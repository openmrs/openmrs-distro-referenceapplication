WITH cmam_encounters AS (
  SELECT e.encounter_id, e.patient_id, e.encounter_datetime,
    ROW_NUMBER() OVER (PARTITION BY e.patient_id ORDER BY e.encounter_datetime DESC, e.encounter_id DESC) AS rn
  FROM encounter e
  JOIN form f ON f.form_id = e.form_id
  WHERE e.voided = 0
    AND f.uuid = '6cb24522-aef4-42a7-a4b8-f3e8b4415416'
    AND (:startDate IS NULL OR e.encounter_datetime >= :startDate)
    AND (:endDate IS NULL OR e.encounter_datetime < DATE_ADD(:endDate, INTERVAL 1 DAY))
),
latest_encounters AS (
  SELECT encounter_id, patient_id FROM cmam_encounters WHERE rn = 1
)
SELECT DISTINCT
  p.person_id                  AS patientId,
  p.uuid                       AS patientUuid,
  COALESCE(pn.given_name, '')  AS givenName,
  COALESCE(pn.family_name, '') AS familyName,
  pi.identifier                AS identifier,
  p.gender                     AS sex,
  pi_nid.identifier            AS nationalId,
  pa_phone.value                AS phoneNumber,
  cd.name                      AS currentDiagnosis,
  cls.name                     AS childLastStatus,
  CASE
    WHEN nextAppt.nextApptDate IS NULL THEN 'No Follow-up Scheduled'
    WHEN DATEDIFF(nextAppt.nextApptDate, CURDATE()) < 0 THEN 'OVERDUE'
    WHEN DATEDIFF(nextAppt.nextApptDate, CURDATE()) <= 7 THEN 'DUE SOON'
    ELSE 'OK'
  END                          AS alertStatus,
  nvd.value_datetime            AS nextVisitDate
FROM latest_encounters le
-- Alert Status is matched against the computed category (see cmam_summary_report.sql) rather
-- than the raw recorded obs, so this stays consistent with the summary counts above it (matched
-- in the WHERE clause below via nvd, already joined for the nextVisitDate column). The other two
-- dimensions still match the literal recorded answer via matchDim.
LEFT JOIN obs matchDim ON matchDim.encounter_id = le.encounter_id
  AND matchDim.concept_id = (SELECT concept_id FROM concept WHERE uuid = :dimensionConceptUuid)
  AND matchDim.value_coded = :categoryConceptId AND matchDim.voided = 0
JOIN person p   ON p.person_id = le.patient_id
JOIN patient pt ON pt.patient_id = p.person_id
LEFT JOIN person_name pn ON pn.person_id = p.person_id AND pn.voided = 0 AND pn.preferred = 1
LEFT JOIN patient_identifier pi ON pi.patient_id = pt.patient_id AND pi.voided = 0 AND pi.preferred = 1
LEFT JOIN patient_identifier pi_nid
  ON pi_nid.patient_id = pt.patient_id AND pi_nid.voided = 0
  AND pi_nid.identifier_type = (SELECT patient_identifier_type_id FROM patient_identifier_type WHERE name = 'National ID')
LEFT JOIN person_attribute pa_phone
  ON pa_phone.person_id = p.person_id AND pa_phone.voided = 0
  AND pa_phone.person_attribute_type_id = (SELECT person_attribute_type_id FROM person_attribute_type WHERE name = 'Phone Number')
LEFT JOIN obs ocd ON ocd.encounter_id = le.encounter_id
  AND ocd.concept_id = (SELECT concept_id FROM concept WHERE uuid = '51d873b5-3394-4780-87d3-5bfaf5cf0eb8') AND ocd.voided = 0
LEFT JOIN concept_name cd ON cd.concept_id = ocd.value_coded AND cd.locale = 'en' AND cd.locale_preferred = 1
LEFT JOIN obs ocls ON ocls.encounter_id = le.encounter_id
  AND ocls.concept_id = (SELECT concept_id FROM concept WHERE uuid = '524fea02-d6e8-47c0-84ee-e7b889f08d4c') AND ocls.voided = 0
LEFT JOIN concept_name cls ON cls.concept_id = ocls.value_coded AND cls.locale = 'en' AND cls.locale_preferred = 1
LEFT JOIN obs nvd ON nvd.encounter_id = le.encounter_id
  AND nvd.concept_id = (SELECT concept_id FROM concept WHERE uuid = 'bff5c735-7f07-44e5-8cd3-13c03a77037f') AND nvd.voided = 0
LEFT JOIN (
  SELECT patient_id,
    COALESCE(
      MIN(CASE WHEN start_date_time >= CURDATE() THEN start_date_time END),
      MAX(CASE WHEN start_date_time < CURDATE() THEN start_date_time END)
    ) AS nextApptDate
  FROM patient_appointment
  WHERE voided = 0
  GROUP BY patient_id
) nextAppt ON nextAppt.patient_id = le.patient_id
WHERE
  (:dimensionConceptUuid <> '47266119-f616-4e8a-b094-518b4c2d660b' AND matchDim.encounter_id IS NOT NULL)
  OR (
    :dimensionConceptUuid = '47266119-f616-4e8a-b094-518b4c2d660b'
    AND (
      (
        :categoryConceptId = -1 AND nextAppt.nextApptDate IS NULL
      )
      OR (
        :categoryConceptId <> -1
        AND nextAppt.nextApptDate IS NOT NULL
        AND (
          CASE
            WHEN DATEDIFF(nextAppt.nextApptDate, CURDATE()) < 0 THEN 'OVERDUE'
            WHEN DATEDIFF(nextAppt.nextApptDate, CURDATE()) <= 7 THEN 'DUE SOON'
            ELSE 'OK'
          END
        ) = (SELECT name FROM concept_name WHERE concept_id = :categoryConceptId AND locale = 'en' AND locale_preferred = 1)
      )
    )
  )
ORDER BY familyName, givenName
