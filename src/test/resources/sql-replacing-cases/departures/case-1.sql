SELECT
    sum(kol_pas)                 as "Количество пассажиров",
    ''                           as "Средняя дальность",
    sum(pass_km)                 as "Пассажиро-км"

FROM l3_mes.prig_analit
WHERE date           BETWEEN '01-01-2022' AND '01-01-2023'
  AND par_name       = 'otpr'
  AND term_dor       LIKE ANY(ARRAY['О%'])
  AND chp            IS NOT NULL

GROUP BY TRUE
ORDER BY TRUE