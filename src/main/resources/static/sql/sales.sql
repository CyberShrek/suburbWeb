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
    WHERE date         BETWEEN '$start_date' AND '$end_date'
      AND term_dor     LIKE ANY(ARRAY[$road_codes])
      AND chp          IS NOT NULL
      AND reg          = ANY(ARRAY[$region_codes:numeric])          -- $region_code
      AND anal_rasch   LIKE ANY(ARRAY[$calculation_types])     -- $calculation_type
      AND anal_vid_bil LIKE ANY(ARRAY[$document_types])        -- $document_type
      AND anal_oper    LIKE ANY(ARRAY[$operation_types])       -- $operation_type
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
