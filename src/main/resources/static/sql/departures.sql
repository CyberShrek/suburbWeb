SELECT
    date                         as "Дата продажи",     -- $date
    concat('20.', chp)           as "Перевозчик",       -- $carrier
    train_category               as "Категория поезда", -- $train_category
    sum(kol_pas)                 as "Количество пассажиров",
    ''                           as "Средняя дальность",
    sum(pass_km)                 as "Пассажиро-км"

FROM l3_mes.prig_analit
WHERE date           BETWEEN '$start_date' AND '$end_date'
  AND par_name       = 'otpr'
  AND term_dor       LIKE ANY(ARRAY[$road_codes])
  AND chp            = ANY(ARRAY[$carrier_codes:numeric])
  AND train_category LIKE ANY(ARRAY[$train_categories]) -- $train_category

GROUP BY TRUE
    ,"Дата продажи"     -- $date
    ,"Перевозчик"       -- $carrier
    ,"Категория поезда" -- $train_category
ORDER BY TRUE
    ,"Дата продажи"     -- $date
    ,"Перевозчик"       -- $carrier
    ,"Категория поезда" -- $train_category