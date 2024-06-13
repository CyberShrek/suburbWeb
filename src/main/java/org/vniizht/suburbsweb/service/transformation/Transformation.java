package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.Logger;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.transformation.data.Level3Data;

import java.util.Map;

@Service
@Scope("singleton")
public class Transformation {

    @Autowired private Conversion conversion;
    @Autowired private Aggregation aggregation;
    @Autowired private Logger logger;
    @Autowired private Level2Data level2Data;
    @Autowired private Level3Data level3Data;

    public void run() {
        logger.log("Запуск трансформации данных по пригороду со второго уровня в третий");
        Long lastLevel3Id = level3Data.getLastId();

        logger.log("Последний индекс третьего уровня: " + lastLevel3Id + "; ищу записи второго уровня с индексом больше этого значения");
        Map<Long, Level2Data.Record> records = level2Data.getRecordsByIdGreaterThan(lastLevel3Id);

        logger.log("Найдено записей: " + records.size());
//        records.forEach(this::transform);

        logger.log("Конец трансформации");
    }

    public Conversion.Converted getConvertedByIdnum(Long idnum) {
        return conversion.convert(level2Data.getRecordByIdnum(idnum));
    }
}
