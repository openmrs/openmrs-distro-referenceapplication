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

SELECT 'alertStatus', c.concept_id, cn.name, COUNT(*)
FROM latest_encounters le
JOIN obs o ON o.encounter_id = le.encounter_id
  AND o.concept_id = (SELECT concept_id FROM concept WHERE uuid = '47266119-f616-4e8a-b094-518b4c2d660b')
  AND o.voided = 0
JOIN concept c ON c.concept_id = o.value_coded
JOIN concept_name cn ON cn.concept_id = c.concept_id AND cn.locale = 'en' AND cn.locale_preferred = 1
GROUP BY c.concept_id, cn.name

ORDER BY dimension, category
