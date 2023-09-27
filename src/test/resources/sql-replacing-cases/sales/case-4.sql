WITH base as (
    SELECT row_number() over () as row_number
         ,par_name
         ,date          -- $date
         ,kol_pas
         ,plata
         ,poteri
         ,anal_rasch    -- $calculation_type
         ,anal_vid_bil
         ,anal_oper     -- $operation_type

    FROM l3_mes.prig_analit
    WHERE date         BETWEEN '01-01-2022' AND '01-01-2023'
      AND term_dor     LIKE ANY(ARRAY['О%'])
      AND chp          IS NOT NULL
      AND reg          = ANY(ARRAY[78, 77, 64])          -- $region_code
      AND anal_rasch   LIKE ANY(ARRAY['1-n%', '2-b%', '3-b%'])     -- $calculation_type
      AND anal_vid_bil LIKE ANY(ARRAY['1.2%', '1.4%', '1.7%']) -- $document_type
      AND anal_oper    LIKE ANY(ARRAY['1%', '2%'])       -- $operation_type
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
    date                         as "Дата продажи",  -- $date
    substr(anal_rasch, 1, 3)     as "Вид расчёта",   -- $calculation_type
    substr(anal_vid_bil, 1, 3)   as "Вид документа", -- $document_type
    anal_oper                    as "Вид операции",  -- $operation_type
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
       ,"Дата продажи"          -- $date
       ,"Вид расчёта"    -- $calculation_type
       ,"Вид документа"  -- $document_type
       ,"Вид операции"     -- $operation_type
ORDER BY TRUE
       ,"Дата продажи"          -- $date
       ,"Вид расчёта"    -- $calculation_type
       ,"Вид документа"  -- $document_type
       ,"Вид операции"     -- $operation_type
