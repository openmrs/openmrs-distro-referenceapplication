WITH categories AS (
  SELECT
    c.concept_id,
    cn.name AS category_name
  FROM concept c
  JOIN concept_class cc ON cc.concept_class_id = c.class_id
  JOIN concept_name cn  ON cn.concept_id = c.concept_id
                        AND cn.locale = 'en'
                        AND cn.concept_name_type = 'FULLY_SPECIFIED'
                        AND cn.voided = 0
    AND c.retired = 0
),
test_map AS (
  SELECT
    cat.category_name AS category,
    cat.concept_id AS category_concept_id,
    test_cn.name AS test_label,
    cs.concept_set AS test_concept_id
  FROM categories cat
  JOIN concept_set cs ON cs.concept_id = cat.concept_id
  JOIN concept_name test_cn ON test_cn.concept_id = cs.concept_set
                            AND test_cn.locale = 'en'
                            AND test_cn.concept_name_type = 'FULLY_SPECIFIED'
                            AND test_cn.voided = 0
),
qualifying_orders AS (
  SELECT
    o.concept_id,
    p.gender,
    CASE
      WHEN TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 0  AND 4  THEN '0-4'
      WHEN TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 5  AND 14 THEN '5-14'
      WHEN TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 15 AND 18 THEN '15-18'
      WHEN TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 19 AND 49 THEN '19-49'
      WHEN TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 50 AND 65 THEN '50-65'
      ELSE '65+'
    END AS age_group
  FROM orders o
  JOIN test_order tord ON tord.order_id = o.order_id
  JOIN encounter e     ON e.encounter_id = o.encounter_id
  JOIN person p        ON p.person_id = o.patient_id
  WHERE o.voided = 0
    AND (:startDate IS NULL OR e.encounter_datetime >= :startDate)
    AND (:endDate IS NULL OR e.encounter_datetime < DATE_ADD(:endDate, INTERVAL 1 DAY))
)
SELECT
  tm.category_concept_id AS categoryConceptId,
  tm.category             AS category,
  tm.test_concept_id      AS testConceptId,
  tm.test_label           AS testLabel,
  COUNT(qo.age_group)      AS totalTests,
  SUM(CASE WHEN qo.age_group = '0-4'   AND qo.gender = 'M' THEN 1 ELSE 0 END) AS age_0_4_male,
  SUM(CASE WHEN qo.age_group = '0-4'   AND qo.gender = 'F' THEN 1 ELSE 0 END) AS age_0_4_female,
  SUM(CASE WHEN qo.age_group = '5-14'  AND qo.gender = 'M' THEN 1 ELSE 0 END) AS age_5_14_male,
  SUM(CASE WHEN qo.age_group = '5-14'  AND qo.gender = 'F' THEN 1 ELSE 0 END) AS age_5_14_female,
  SUM(CASE WHEN qo.age_group = '15-18' AND qo.gender = 'M' THEN 1 ELSE 0 END) AS age_15_18_male,
  SUM(CASE WHEN qo.age_group = '15-18' AND qo.gender = 'F' THEN 1 ELSE 0 END) AS age_15_18_female,
  SUM(CASE WHEN qo.age_group = '19-49' AND qo.gender = 'M' THEN 1 ELSE 0 END) AS age_19_49_male,
  SUM(CASE WHEN qo.age_group = '19-49' AND qo.gender = 'F' THEN 1 ELSE 0 END) AS age_19_49_female,
  SUM(CASE WHEN qo.age_group = '50-65' AND qo.gender = 'M' THEN 1 ELSE 0 END) AS age_50_65_male,
  SUM(CASE WHEN qo.age_group = '50-65' AND qo.gender = 'F' THEN 1 ELSE 0 END) AS age_50_65_female,
  SUM(CASE WHEN qo.age_group = '65+'   AND qo.gender = 'M' THEN 1 ELSE 0 END) AS age_65_plus_male,
  SUM(CASE WHEN qo.age_group = '65+'   AND qo.gender = 'F' THEN 1 ELSE 0 END) AS age_65_plus_female,
  COUNT(qo.age_group)      AS total
FROM test_map tm
LEFT JOIN qualifying_orders qo ON qo.concept_id = tm.test_concept_id
GROUP BY tm.category_concept_id, tm.category, tm.test_concept_id, tm.test_label
ORDER BY tm.category, tm.test_label
