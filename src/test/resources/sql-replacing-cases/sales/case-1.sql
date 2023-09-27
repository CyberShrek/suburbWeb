WITH base as (
    SELECT row_number() over () as row_number
         ,par_name
         ,kol_pas
         ,plata
         ,poteri
         ,anal_vid_bil

    FROM l3_mes.prig_analit
    WHERE date         BETWEEN '01-01-2022' AND '01-01-2023'
      AND term_dor     LIKE ANY(ARRAY['О%'])
      AND chp          IS NOT NULL
),
     sales as (
         SELECT row_number, kol_pas, plata, poteri
         FROM base
         WHERE par_name = 'prod'
     ),
     fees as (
         SELECT row_number, plata
         FROM base
         WHERE anal_vid_bil LIKE '2.5%'
     ),
     bags as (
         SELECT row_number, plata
         FROM base
         WHERE par_name = 'bag'
     )
SELECT
    sum(sales.kol_pas)           as "Количество человек или перевозок",
    sum(sales.plata)             as "Доход",
    sum(sales.poteri)            as "Недополученный доход",
    sum(coalesce(fees.plata, 0)) as "Сумма сбора",
    sum(coalesce(bags.plata, 0)) as "Доход за провоз ручной клади"

FROM base
         JOIN sales USING (row_number)
         LEFT JOIN fees USING (row_number)
         LEFT JOIN bags USING (row_number)

GROUP BY TRUE
ORDER BY TRUE
