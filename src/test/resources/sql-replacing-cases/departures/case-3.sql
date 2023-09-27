SELECT
    date                         as "Дата продажи",     -- $date
    train_category               as "Категория поезда", -- $train_category
    sum(kol_pas)                 as "Количество пассажиров",
    ''                           as "Средняя дальность",
    sum(pass_km)                 as "Пассажиро-км"

FROM l3_mes.prig_analit
WHERE date           BETWEEN '01-01-2022' AND '01-01-2023'
  AND par_name       = 'otpr'
  AND term_dor       LIKE ANY(ARRAY['О%'])
  AND chp            IS NOT NULL
  AND train_category LIKE ANY(ARRAY['О%', 'Я%', 'Л%']) -- $train_category

GROUP BY TRUE
       ,date           -- $date
       ,train_category -- $train_category
ORDER BY TRUE
       ,date            -- $date
       ,train_category  -- $train_category