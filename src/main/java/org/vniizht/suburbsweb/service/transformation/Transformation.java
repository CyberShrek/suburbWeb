package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level3.tables_co22.*;
import org.vniizht.suburbsweb.model.transformation.level3.tables_lgot.Reestr;
import org.vniizht.suburbsweb.model.transformation.level3.tables_lgot.Stat;
import org.vniizht.suburbsweb.service.Logger;
import org.vniizht.suburbsweb.service.transformation.data.Level2Data;
import org.vniizht.suburbsweb.service.transformation.data.Level3Data;
import org.vniizht.suburbsweb.service.transformation.data.NsiData;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
@Scope("singleton")
public class Transformation {

    @Autowired private Logger     logger;
    @Autowired private NsiData    nsiData;
    @Autowired private Level2Data level2Data;
    @Autowired private Level3Data level3Data;

    @PostConstruct
    public synchronized void run() {
        logger.log("Запуск трансформации данных по пригороду со второго уровня в третий");
        Long lastLevel3Id = level3Data.getLastId();

        logger.log("Последний индекс третьего уровня: " + lastLevel3Id + "; ищу записи второго уровня с индексом больше этого значения");
        Map<Long, Level2Data.Record> records = level2Data.getRecordsByIdGreaterThan(lastLevel3Id);

        logger.log("Найдено записей: " + records.size());
        records.forEach(this::transform);

        logger.log("Конец трансформации");
    }

    private void transform(Long id, Level2Data.Record l2Record) {
        logger.log("Трансформация " + id);

        logger.log("\tЦО22");
        T1 t1 = createT1(l2Record, 1);
        T2 t2 = createT2(l2Record, t1);
        T3 t3 = createT3(l2Record, t1);
        T4 t4 = createT4(l2Record, t1);
        T5 t5 = createT5(l2Record, t1, t2, t3);
        T6 t6 = createT6(l2Record, t1);

        logger.log("\tЛьготные");
        Reestr reestr = createReestr(l2Record);
        Stat   stat   = createStat  (l2Record);
    }

    private T1 createT1(Level2Data.Record l2, int serial) {
        logger.log("\t\tprig_co22_t1");
        return T1.builder()
                .idnum(l2.main.getIdnum())
                .request_num(l2.main.getRequest_num())
                .yymm(Util.yyyymm2yymm(l2.main.getYyyymm()))
                .p1("tab1")
                .p2(serial)
                .p3(Util.date2yyyy(l2.main.getOperation_date()))
                .p4(Util.date2mm(l2.main.getOperation_date()))
                .p5(nsiData.getRoadByStationAndDate(l2.main.getDeparture_station(), l2.main.getOperation_date(), true)) // ??
                .p6(nsiData.getRoadByStationAndDate(l2.main.getDeparture_station(), l2.main.getOperation_date())) // ??
                .p7(nsiData.getRoadByStationAndDate(l2.main.getDeparture_station(), l2.main.getOperation_date())) // ??
//                .p8()
//                .p9()
//                .p10()
//                .p11()
//                .p12()
//                .p13()
//                .p14()
//                .p15()
//                .p16()
//                .p17()
//                .p18()
//                .p19()
//                .p20()
//                .p21()
//                .p22()
//                .p23()
//                .p24()
//                .p25()
//                .p26()
//                .p27()
//                .p28()
//                .p29()
//                .p30()
//                .p31()
//                .p32()
//                .p33()
//                .p34()
//                .p35()
//                .p36()
//                .p37()
//                .p38()
//                .p39()
//                .p40()
//                .p41()
//                .p42()
//                .p43()
//                .p44()
//                .p45()
//                .p46()
//                .p47()
//                .p48()
//                .p49()
//                .p50()
//                .p51()
//                .p52()
//                .p53()
//                .p54()
//                .p55()
//                .p56()
//                .p57()
//                .p58()
//                .p59()
//                .p60()
//                .p61()
//                .p62()
//                .p63()
                .build();
    }

    private T2 createT2(Level2Data.Record record, T1 t1) {
        logger.log("\t\tprig_co22_t2");
        return T2.builder()
                .yymm(t1.getYymm())
                .p1("tab2")
                .build();
    }

    private T3 createT3(Level2Data.Record record, T1 t1) {
        logger.log("\t\tprig_co22_t3");
        return T3.builder()
                .yymm(t1.getYymm())
                .p1("tab3")
                .build();
    }

    private T4 createT4(Level2Data.Record record, T1 t1) {
        logger.log("\t\tprig_co22_t4");
        return T4.builder()
                .yymm(t1.getYymm())
                .p1("tab4")

                .build();
    }

    private T5 createT5(Level2Data.Record record, T1 t1, T2 t2, T3 t3) {
        logger.log("\t\tprig_co22_t5");
        return T5.builder()
                .yymm(t1.getYymm())
                .p1("tab5")
                .build();
    }

    private T6 createT6(Level2Data.Record record, T1 t1) {
        logger.log("\t\tprig_co22_t6");
        return T6.builder()
                .yymm(t1.getYymm())
                .p1("tab6")
                .build();
    }
    
    private Reestr createReestr(Level2Data.Record record) {
        logger.log("\t\tprig_lgot_reestr");
        return Reestr.builder()
                .build();
    }
    
    private Stat createStat(Level2Data.Record record) {
        logger.log("\t\tprig_lgot_stat");
        return Stat.builder()
                .build();
    }

    @PostConstruct
    private void showSome(){

        Long lastLevel3Id = level3Data.getLastId();
        logger.log("Last level 3 id: " + lastLevel3Id);
        Map<Long, Level2Data.Record> records = level2Data.getRecordsByIdGreaterThan(lastLevel3Id);
        logger.log("Level 2 data with id greater than " + lastLevel3Id + " size: " + records.size());
        logger.log("The data:");
        records.forEach((id, record) -> logger.log(id + ":\n" + record.toString()));
    }
}
