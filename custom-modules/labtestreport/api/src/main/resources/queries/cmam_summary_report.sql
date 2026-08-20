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
SELECT 'currentDiagnosis' AS dimension, c.concept_id AS categoryConceptId, cn.name AS category, COUNT(*) AS total
FROM latest_encounters le
JOIN obs o ON o.encounter_id = le.encounter_id
  AND o.concept_id = (SELECT concept_id FROM concept WHERE uuid = '51d873b5-3394-4780-87d3-5bfaf5cf0eb8')
  AND o.voided = 0
JOIN concept c ON c.concept_id = o.value_coded
JOIN concept_name cn ON cn.concept_id = c.concept_id AND cn.locale = 'en' AND cn.locale_preferred = 1
GROUP BY c.concept_id, cn.name

UNION ALL

SELECT 'childLastStatus', c.concept_id, cn.name, COUNT(*)
FROM latest_encounters le
JOIN obs o ON o.encounter_id = le.encounter_id
  AND o.concept_id = (SELECT concept_id FROM concept WHERE uuid = '524fea02-d6e8-47c0-84ee-e7b889f08d4c')
  AND o.voided = 0
JOIN concept c ON c.concept_id = o.value_coded
JOIN concept_name cn ON cn.concept_id = c.concept_id AND cn.locale = 'en' AND cn.locale_preferred = 1
GROUP BY c.concept_id, cn.name

UNION ALL

-- Alert Status is computed from the child's actual Appointments-module follow-up appointment
-- (not the independently recorded, often-stale Alert Status answer, and not the CMAM form's own
-- "next visit date" field - a doctor schedules the real follow-up as an Appointment): overdue if
-- that appointment date has passed, due soon if within 7 days, OK otherwise. A child with no
-- appointment scheduled at all falls into its own bucket - there's no date to compare, so it must
-- not be silently counted as OK. That bucket has no backing concept, so it's given the sentinel
-- categoryConceptId -1 (the drilldown query below special-cases it the same way).
SELECT
  'alertStatus',
  COALESCE(
    (SELECT c.concept_id FROM concept alertQuestion
       JOIN concept_answer ca ON ca.concept_id = alertQuestion.concept_id
       JOIN concept c ON c.concept_id = ca.answer_concept
       JOIN concept_name cn ON cn.concept_id = c.concept_id AND cn.locale = 'en' AND cn.locale_preferred = 1
     WHERE alertQuestion.uuid = '47266119-f616-4e8a-b094-518b4c2d660b'
       AND cn.name = computed.computedAlertStatus
     LIMIT 1),
    -1
  ),
  COALESCE(computed.computedAlertStatus, 'No Follow-up Scheduled'),
  COUNT(*)
FROM (
  SELECT le.patient_id,
    CASE
      WHEN nextAppt.nextApptDate IS NULL THEN NULL
      WHEN DATEDIFF(nextAppt.nextApptDate, CURDATE()) < 0 THEN 'OVERDUE'
      WHEN DATEDIFF(nextAppt.nextApptDate, CURDATE()) <= 7 THEN 'DUE SOON'
      ELSE 'OK'
    END AS computedAlertStatus
  FROM latest_encounters le
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
) computed
GROUP BY computed.computedAlertStatus

ORDER BY dimension, category
