package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level3.tables_co22.*;
import org.vniizht.suburbsweb.model.transformation.level3.tables_lgot.Reestr;
import org.vniizht.suburbsweb.model.transformation.level3.tables_lgot.Stat;
import org.vniizht.suburbsweb.service.Logger;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
@Scope("singleton")
public class Wizard {

    @Autowired private Logger     logger;
    @Autowired private Level2Data level2Data;
    @Autowired private Level3Data level3Data;

    @PostConstruct
    public synchronized void runTransformation() {
        logger.log("Запуск трансформации данных по пригороду со второго уровня в третий");
        Long lastLevel3Id = level3Data.getLastId();
        logger.log("Последний индекс третьего уровня: " + lastLevel3Id + "; ищу записи второго уровня с индексом больше этого значения");
        Map<Long, Level2Data.Record> records = level2Data.getRecordsByIdGreaterThan(lastLevel3Id);
        logger.log("Найдено записей: " + records.size());
        records.forEach(this::transform);
        logger.log("Конец трансформации");
    }

    private void transform(Long id, Level2Data.Record record) {
        logger.log("Трансформация " + id);
        recordToCO22(id, record);
        recordToLgot(id, record);
    }

    private void recordToCO22(Long id, Level2Data.Record record) {
        logger.log("\tЦО22");

        logger.log("\t\tprig_co22_t0");
        T0 t0 = T0.builder()
                .build();

        logger.log("\t\tprig_co22_t1");
        T1 t1 = T1.builder()
                .p1()
                .p2()
                .p3()
                .p4()
                .p5()
                .p6()
                .p7()
                .p8()
                .p9()
                .p10()
                .p11()
                .p12()
                .p13()
                .p14()
                .p15()
                .p16()
                .p17()
                .p18()
                .p19()
                .p20()
                .p21()
                .p22()
                .p23()
                .p24()
                .p25()
                .p26()
                .p27()
                .p28()
                .p29()
                .p30()
                .p31()
                .p32()
                .p33()
                .p34()
                .p35()
                .p36()
                .p37()
                .p38()
                .p39()
                .p40()
                .p41()
                .p42()
                .p43()
                .p44()
                .p45()
                .p46()
                .p47()
                .p48()
                .p49()
                .p50()
                .p51()
                .p52()
                .p53()
                .p54()
                .p55()
                .p56()
                .p57()
                .p58()
                .p59()
                .p60()
                .p61()
                .p62()
                .p63()
                .build();

        logger.log("\t\tprig_co22_t2");
        T2 t2 = T2.builder()
                .build();

        logger.log("\t\tprig_co22_t3");
        T3 t3 = T3.builder()
                .build();

        logger.log("\t\tprig_co22_t4");
        T4 t4 = T4.builder()
                .build();

        logger.log("\t\tprig_co22_t5");
        T5 t5 = T5.builder()
                .build();

        logger.log("\t\tprig_co22_t6");
        T6 t6 = T6.builder()
                .build();
    }

    private void recordToLgot(Long id, Level2Data.Record record) {
        logger.log("\tЛьготные");

        logger.log("\t\tprig_lgot_reestr");
        Reestr reestr = Reestr.builder()
                .build();

        logger.log("\t\tprig_lgot_stat");
        Stat stat = Stat.builder()
                .build();
    }

    @PostConstruct
    private void showSome(){

//        Long lastLevel3Id = level3Data.getLastId();
//        logger.log("Last level 3 id: " + lastLevel3Id);
//        Map<Long, Level2Data.Record> records = level2Data.getRecordsByIdGreaterThan(lastLevel3Id);
//        logger.log("Level 2 data with id greater than " + lastLevel3Id + " size: " + records.size());
//        logger.log("The data:");
//        records.forEach((id, record) -> logger.log(id + ":\n" + record.toString()));
    }
}
