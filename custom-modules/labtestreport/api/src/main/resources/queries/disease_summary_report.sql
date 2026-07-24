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
  WHERE cc.name = 'DiseaseCategorySet'
    AND c.retired = 0
),
diagnosis_map AS (
  SELECT
    cat.category_name AS category,
    cat.concept_id AS category_concept_id,
    diag_cn.name AS diagnosis_label,
    cs.concept_set AS diagnosis_concept_id
  FROM categories cat
  JOIN concept_set cs ON cs.concept_id = cat.concept_id
  JOIN concept_name diag_cn ON diag_cn.concept_id = cs.concept_set
                            AND diag_cn.locale = 'en'
                            AND diag_cn.concept_name_type = 'FULLY_SPECIFIED'
                            AND diag_cn.voided = 0
),
qualifying_diagnoses AS (
  SELECT
    ed.diagnosis_coded AS concept_id,
    p.gender,
    CASE
      WHEN TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 0  AND 4  THEN '0-4'
      WHEN TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 5  AND 14 THEN '5-14'
      WHEN TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 15 AND 18 THEN '15-18'
      WHEN TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 19 AND 49 THEN '19-49'
      WHEN TIMESTAMPDIFF(YEAR, p.birthdate, e.encounter_datetime) BETWEEN 50 AND 65 THEN '50-65'
      ELSE '65+'
    END AS age_group
  FROM encounter_diagnosis ed
  JOIN encounter e ON e.encounter_id = ed.encounter_id
  JOIN person p    ON p.person_id = ed.patient_id
  WHERE ed.voided = 0
    AND (:startDate IS NULL OR e.encounter_datetime >= :startDate)
    AND (:endDate IS NULL OR e.encounter_datetime < DATE_ADD(:endDate, INTERVAL 1 DAY))
)
SELECT
  dm.category_concept_id   AS categoryConceptId,
  dm.category               AS category,
  dm.diagnosis_concept_id   AS diagnosisConceptId,
  dm.diagnosis_label        AS diagnosisLabel,
  COUNT(qd.age_group)       AS totalCases,
  SUM(CASE WHEN qd.age_group = '0-4'   AND qd.gender = 'M' THEN 1 ELSE 0 END) AS age_0_4_male,
  SUM(CASE WHEN qd.age_group = '0-4'   AND qd.gender = 'F' THEN 1 ELSE 0 END) AS age_0_4_female,
  SUM(CASE WHEN qd.age_group = '5-14'  AND qd.gender = 'M' THEN 1 ELSE 0 END) AS age_5_14_male,
  SUM(CASE WHEN qd.age_group = '5-14'  AND qd.gender = 'F' THEN 1 ELSE 0 END) AS age_5_14_female,
  SUM(CASE WHEN qd.age_group = '15-18' AND qd.gender = 'M' THEN 1 ELSE 0 END) AS age_15_18_male,
  SUM(CASE WHEN qd.age_group = '15-18' AND qd.gender = 'F' THEN 1 ELSE 0 END) AS age_15_18_female,
  SUM(CASE WHEN qd.age_group = '19-49' AND qd.gender = 'M' THEN 1 ELSE 0 END) AS age_19_49_male,
  SUM(CASE WHEN qd.age_group = '19-49' AND qd.gender = 'F' THEN 1 ELSE 0 END) AS age_19_49_female,
  SUM(CASE WHEN qd.age_group = '50-65' AND qd.gender = 'M' THEN 1 ELSE 0 END) AS age_50_65_male,
  SUM(CASE WHEN qd.age_group = '50-65' AND qd.gender = 'F' THEN 1 ELSE 0 END) AS age_50_65_female,
  SUM(CASE WHEN qd.age_group = '65+'   AND qd.gender = 'M' THEN 1 ELSE 0 END) AS age_65_plus_male,
  SUM(CASE WHEN qd.age_group = '65+'   AND qd.gender = 'F' THEN 1 ELSE 0 END) AS age_65_plus_female,
  COUNT(qd.age_group)       AS total
FROM diagnosis_map dm
LEFT JOIN qualifying_diagnoses qd ON qd.concept_id = dm.diagnosis_concept_id
GROUP BY dm.category_concept_id, dm.category, dm.diagnosis_concept_id, dm.diagnosis_label
ORDER BY dm.category, dm.diagnosis_label
