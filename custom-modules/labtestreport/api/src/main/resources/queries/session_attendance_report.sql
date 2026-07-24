SELECT
  DATE(v.date_started)                                                          AS sessionDate,
  CASE WHEN v.visit_type_id = 5 THEN 'Group Sessions' ELSE 'Individual Sessions' END AS sessionType,
  MAX(va.value_reference)                                                       AS sessionSubject,
  COUNT(DISTINCT v.patient_id)                                                  AS totalAttendees,
  SUM(CASE WHEN TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started) BETWEEN 0  AND 4  AND p.gender = 'M' THEN 1 ELSE 0 END) AS age_0_4_male,
  SUM(CASE WHEN TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started) BETWEEN 0  AND 4  AND p.gender = 'F' THEN 1 ELSE 0 END) AS age_0_4_female,
  SUM(CASE WHEN TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started) BETWEEN 5  AND 14 AND p.gender = 'M' THEN 1 ELSE 0 END) AS age_5_14_male,
  SUM(CASE WHEN TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started) BETWEEN 5  AND 14 AND p.gender = 'F' THEN 1 ELSE 0 END) AS age_5_14_female,
  SUM(CASE WHEN TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started) BETWEEN 15 AND 18 AND p.gender = 'M' THEN 1 ELSE 0 END) AS age_15_18_male,
  SUM(CASE WHEN TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started) BETWEEN 15 AND 18 AND p.gender = 'F' THEN 1 ELSE 0 END) AS age_15_18_female,
  SUM(CASE WHEN TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started) BETWEEN 19 AND 49 AND p.gender = 'M' THEN 1 ELSE 0 END) AS age_19_49_male,
  SUM(CASE WHEN TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started) BETWEEN 19 AND 49 AND p.gender = 'F' THEN 1 ELSE 0 END) AS age_19_49_female,
  SUM(CASE WHEN TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started) BETWEEN 50 AND 65 AND p.gender = 'M' THEN 1 ELSE 0 END) AS age_50_65_male,
  SUM(CASE WHEN TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started) BETWEEN 50 AND 65 AND p.gender = 'F' THEN 1 ELSE 0 END) AS age_50_65_female,
  SUM(CASE WHEN TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started) > 65             AND p.gender = 'M' THEN 1 ELSE 0 END) AS age_65_plus_male,
  SUM(CASE WHEN TIMESTAMPDIFF(YEAR, p.birthdate, v.date_started) > 65             AND p.gender = 'F' THEN 1 ELSE 0 END) AS age_65_plus_female,
  COUNT(DISTINCT v.patient_id)                                                  AS total
FROM visit v
JOIN person p ON p.person_id = v.patient_id
LEFT JOIN visit_attribute va
       ON va.visit_id = v.visit_id
      AND va.voided = 0
      AND va.attribute_type_id = (SELECT visit_attribute_type_id FROM visit_attribute_type WHERE name = 'Session Subject' LIMIT 1)
WHERE v.voided = 0
  AND (:startDate IS NULL OR v.date_started >= :startDate)
  AND (:endDate IS NULL OR v.date_started < DATE_ADD(:endDate, INTERVAL 1 DAY))
GROUP BY DATE(v.date_started), sessionType
ORDER BY sessionDate, sessionType
