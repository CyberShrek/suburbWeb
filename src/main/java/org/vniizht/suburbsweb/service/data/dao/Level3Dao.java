package org.vniizht.suburbsweb.service.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.*;
import org.vniizht.suburbsweb.service.data.entities.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.data.repository.level3.LgotRepo;
import org.vniizht.suburbsweb.service.data.repository.level3.T1Repo;
import org.vniizht.suburbsweb.websocket.LogWS;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Level3Dao {

    private static final String T1_TABLE   = "prigl3.co22_t1";
    private static final String T2_TABLE   = "prigl3.co22_t2";
    private static final String T3_TABLE   = "prigl3.co22_t3";
    private static final String T4_TABLE   = "prigl3.co22_t4";
    private static final String T5_TABLE   = "prigl3.co22_t5";
    private static final String T6_TABLE   = "prigl3.co22_t6";
    private static final String LGOT_TABLE = "prigl3.lgot";
    private static final int BATCH_SIZE    = 500;

    @Autowired private   T1Repo   t1Repo;
    @Autowired private LgotRepo lgotRepo;
    @Autowired private JdbcTemplate jdbcTemplate;


    public Date getNextRequestDate(){
        T1 t1 = t1Repo.findByOrderByKeyRequestDateDesc();
        if(t1 == null || t1.getKey() == null) return null;
        Date lastRequestDate = t1.getKey().getRequestDate();

        // + 1 day
        return new Date(lastRequestDate.getTime() + 24*60*60*1000);
    }

    public Long getLatestT1P2() {
        return jdbcTemplate.queryForObject("SELECT coalesce(max(p2), 0) FROM " + T1_TABLE, Long.class);
    }

    public void deleteForDate(Date date){
        jdbcTemplate.update("DELETE FROM prigl3.co22_t1 WHERE request_date = ?", date);
        jdbcTemplate.update("DELETE FROM prigl3.co22_t2 WHERE request_date = ?", date);
        jdbcTemplate.update("DELETE FROM prigl3.co22_t3 WHERE request_date = ?", date);
        jdbcTemplate.update("DELETE FROM prigl3.co22_t4 WHERE request_date = ?", date);
        jdbcTemplate.update("DELETE FROM prigl3.co22_t6 WHERE request_date = ?", date);
        jdbcTemplate.update("DELETE FROM prigl3.lgot    WHERE request_date = ?", date);
    }

    public void saveT1s(Set<T1> t1Set){
        insertT1s(new ArrayList<>(t1Set));
    }
    public void saveT2s(Set<T2> t2Set){
        insertT2s(new ArrayList<>(t2Set));
    }
    public void saveT3s(Set<T3> t3Set){
        insertT3s(new ArrayList<>(t3Set));
    }
    public void saveT4s(Set<T4> t4Set){
        insertT4s(new ArrayList<>(t4Set));
    }
    public void saveT6s(Set<T6> t6Set){
        insertT6s(new ArrayList<>(t6Set));
    }
    public void saveLgots(Set<Lgot> lgotSet){
        insertLgots(new ArrayList<>(lgotSet));
    }

    private void createTempTableLikeOrig(String tempTable, String origTable) {
        jdbcTemplate.execute("CREATE TEMPORARY TABLE " + tempTable + "(LIKE " + origTable + ")");
        // Remove all not null constraints
        jdbcTemplate.execute("ALTER TABLE " + tempTable + " ALTER COLUMN p1 DROP NOT NULL");
        jdbcTemplate.execute("ALTER TABLE " + tempTable + " ALTER COLUMN p2 DROP NOT NULL");
    }

    private void insertTempTableToOrig(String tempTable, String origTable) {
        jdbcTemplate.execute("INSERT INTO " + origTable + " SELECT * FROM " + tempTable);
    }

    private void dropTempTable(String tempTable) {
        jdbcTemplate.execute("DROP TABLE IF EXISTS " + tempTable);
    }

    private void insertT1s(List<T1> t1List) {
        AtomicInteger progress = new AtomicInteger();
        jdbcTemplate.batchUpdate("INSERT INTO " + T1_TABLE + " VALUES (\n" +
                "?::integer    , ?::date       ,\n" +
                "?::char(4)    , ?::bigint     , ?::char(4)    , ?::char(2)    , ?::char(3)    ,\n" +
                "?::char(3)    , ?::char(3)    , ?::char(9)    , ?::char(9)    , ?::char(2)    ,\n" +
                "?::char(5)    , ?::char(4)    , ?::char(3)    , ?::char(2)    , ?::char(7)    ,\n" +
                "?::char(2)    , ?::char(5)    , ?::char(3)    , ?::char       , ?::char(2)    ,\n" +
                "?::char       , ?::char       , ?::char       , ?::char(4)    , ?::char       ,\n" +
                "?::char(2)    , ?::char(3)    , ?::char(2)    , ?::char(2)    , ?::char(5)    ,\n" +
                "?::char(3)    , ?::integer    , ?::numeric(9) , ?::numeric(11), ?::numeric(11),\n" +
                "?::numeric(11), ?::numeric(11), ?::numeric(11), ?::numeric(11), ?::numeric(11),\n" +
                "?::numeric(11), ?::numeric(11), ?::numeric(11), ?::numeric(11), ?::numeric(11),\n" +
                "?::numeric(11), ?::numeric(11), ?::numeric(11), ?::numeric(11), ?::numeric(11),\n" +
                "?::numeric(9) , ?::char       , ?::char(4)    , ?::char(7)    , ?::char       ,\n" +
                "?::char(3)    , ?::char       , ?::char       , ?::char       , ?::char(3)    ,\n" +
                "?::char       , ?::integer    , ?::char       , ?::smallint[] , ?::bigint[])",
                t1List,
                BATCH_SIZE,
                (ps, t1) -> {
                    //                    t1.getKey().getYyyymm(), t1.getKey().getRequestDate(),
//                            t1.getKey().getP1(), t1.getKey().getP3(), t1.getKey().getP4(), t1.getKey().getP5(), t1.getKey().getP6(), t1.getKey().getP7(), t1.getKey().getP8(), t1.getKey().getP9(), t1.getKey().getP10(),
//                            t1.getKey().getP11(), t1.getKey().getP12(), t1.getKey().getP13(), t1.getKey().getP14(), t1.getKey().getP15(), t1.getKey().getP16(), t1.getKey().getP17(), t1.getKey().getP18(), t1.getKey().getP19(), t1.getKey().getP20(),
//                            t1.getKey().getP21(), t1.getKey().getP22(), t1.getKey().getP23(), t1.getKey().getP24(), t1.getKey().getP25(), t1.getKey().getP26(), t1.getKey().getP27(), t1.getKey().getP28(), t1.getKey().getP29(), t1.getKey().getP30(),
//                            t1.getKey().getP31(), t1.getKey().getP32(), t1.getP33(), t1.getP34(), t1.getP35(), t1.getP36(), t1.getP37(), t1.getP38(), t1.getP39(), t1.getP40(),
//                            t1.getP41(), t1.getP42(), t1.getP43(), t1.getP44(), t1.getP45(), t1.getP46(), t1.getP47(), t1.getP48(), t1.getP49(), t1.getP50(),
//                            t1.getP51(), t1.getKey().getP52(), t1.getKey().getP53(), t1.getKey().getP54(), t1.getKey().getP55(), t1.getKey().getP56(), t1.getKey().getP57(), t1.getKey().getP58(), t1.getKey().getP59(), t1.getKey().getP60(),
//                            t1.getKey().getP61(), t1.getKey().getP62(), t1.getKey().getP63()
                    ps.setObject(1, t1.getKey().getYyyymm());
                    ps.setObject(2, t1.getKey().getRequestDate());
                    ps.setObject(3, t1.getKey().getP1());
                    ps.setObject(4, t1.getKey().getP2());
                    ps.setObject(5, t1.getKey().getP3());
                    ps.setObject(6, t1.getKey().getP4());
                    ps.setObject(7, t1.getKey().getP5());
                    ps.setObject(8, t1.getKey().getP6());
                    ps.setObject(9, t1.getKey().getP7());
                    ps.setObject(10, t1.getKey().getP8());
                    ps.setObject(11, t1.getKey().getP9());
                    ps.setObject(12, t1.getKey().getP10());
                    ps.setObject(13, t1.getKey().getP11());
                    ps.setObject(14, t1.getKey().getP12());
                    ps.setObject(15, t1.getKey().getP13());
                    ps.setObject(16, t1.getKey().getP14());
                    ps.setObject(17, t1.getKey().getP15());
                    ps.setObject(18, t1.getKey().getP16());
                    ps.setObject(19, t1.getKey().getP17());
                    ps.setObject(20, t1.getKey().getP18());
                    ps.setObject(21, t1.getKey().getP19());
                    ps.setObject(22, t1.getKey().getP20());
                    ps.setObject(23, t1.getKey().getP21());
                    ps.setObject(24, t1.getKey().getP22());
                    ps.setObject(25, t1.getKey().getP23());
                    ps.setObject(26, t1.getKey().getP24());
                    ps.setObject(27, t1.getKey().getP25());
                    ps.setObject(28, t1.getKey().getP26());
                    ps.setObject(29, t1.getKey().getP27());
                    ps.setObject(30, t1.getKey().getP28());
                    ps.setObject(31, t1.getKey().getP29());
                    ps.setObject(32, t1.getKey().getP30());
                    ps.setObject(33, t1.getKey().getP31());
                    ps.setObject(34, t1.getKey().getP32());
                    ps.setObject(35, t1.getP33());
                    ps.setObject(36, t1.getP34());
                    ps.setObject(37, t1.getP35());
                    ps.setObject(38, t1.getP36());
                    ps.setObject(39, t1.getP37());
                    ps.setObject(40, t1.getP38());
                    ps.setObject(41, t1.getP39());
                    ps.setObject(42, t1.getP40());
                    ps.setObject(43, t1.getP41());
                    ps.setObject(44, t1.getP42());
                    ps.setObject(45, t1.getP43());
                    ps.setObject(46, t1.getP44());
                    ps.setObject(47, t1.getP45());
                    ps.setObject(48, t1.getP46());
                    ps.setObject(49, t1.getP47());
                    ps.setObject(50, t1.getP48());
                    ps.setObject(51, t1.getP49());
                    ps.setObject(52, t1.getP50());
                    ps.setObject(53, t1.getP51());
                    ps.setObject(54, t1.getKey().getP52());
                    ps.setObject(55, t1.getKey().getP53());
                    ps.setObject(56, t1.getKey().getP54());
                    ps.setObject(57, t1.getKey().getP55());
                    ps.setObject(58, t1.getKey().getP56());
                    ps.setObject(59, t1.getKey().getP57());
                    ps.setObject(60, t1.getKey().getP58());
                    ps.setObject(61, t1.getKey().getP59());
                    ps.setObject(62, t1.getKey().getP60());
                    ps.setObject(63, t1.getKey().getP61());
                    ps.setObject(64, t1.getKey().getP62());
                    ps.setObject(65, t1.getKey().getP63());
                    ps.setObject(66, t1.getKey().getRoutes());
                    ps.setObject(67, t1.getIdnums());
                    progress.getAndIncrement();
                    LogWS.spreadProgress((int) (((float) progress.get()) / t1List.size() * 100));
                }
        );
    }

    private void insertT2s(List<T2> t2List){
        AtomicInteger progress = new AtomicInteger();
        jdbcTemplate.batchUpdate("INSERT INTO " + T2_TABLE + " VALUES (\ndefault," +
                "?::char(4), ?::char(3), ?::bigint, ?::int, ?::char(3), ?::char(2), ?::int, ?::date)",
                t2List,
                BATCH_SIZE,
                (ps, t2) -> {
                    ps.setObject(1, t2.getP1());
                    ps.setObject(2, t2.getP2());
                    ps.setObject(3, t2.getP3());
                    ps.setObject(4, t2.getP4());
                    ps.setObject(5, t2.getP5());
                    ps.setObject(6, t2.getP6());
                    ps.setObject(7, t2.getP7());
                    ps.setObject(8, t2.getRequestDate());
                    progress.getAndIncrement();
                    LogWS.spreadProgress((int) (((float) progress.get()) / t2List.size() * 100));
                });
    }

    private void insertT3s(List<T3> t3List){
        AtomicInteger progress = new AtomicInteger();
        jdbcTemplate.batchUpdate("INSERT INTO " + T3_TABLE + " VALUES (\ndefault," +
                "?::char(4), ?::char(3), ?::bigint, ?::int, ?::char(2), ?::char(5), ?::int, ?::date)",
                t3List,
                BATCH_SIZE,
                (ps, t3) -> {
                    ps.setObject(1, t3.getP1());
                    ps.setObject(2, t3.getP2());
                    ps.setObject(3, t3.getP3());
                    ps.setObject(4, t3.getP4());
                    ps.setObject(5, t3.getP5());
                    ps.setObject(6, t3.getP6());
                    ps.setObject(7, t3.getP7());
                    ps.setObject(8, t3.getRequestDate());
                    progress.getAndIncrement();
                    LogWS.spreadProgress((int) (((float) progress.get()) / t3List.size() * 100));
                });
    }

    private void insertT4s(List<T4> t4List){
        AtomicInteger progress = new AtomicInteger();
        jdbcTemplate.batchUpdate("INSERT INTO " + T4_TABLE + " VALUES (\ndefault," +
                "?::char(4), ?::char(3), ?::bigint, ?::int, ?::char(3), ?::char(5), ?::numeric(11), ?::numeric(11), ?::int, ?::date)",
                t4List,
                BATCH_SIZE,
                (ps, t4) -> {
                    ps.setObject(1, t4.getP1());
                    ps.setObject(2, t4.getP2());
                    ps.setObject(3, t4.getP3());
                    ps.setObject(4, t4.getP4());
                    ps.setObject(5, t4.getP5());
                    ps.setObject(6, t4.getP6());
                    ps.setObject(7, t4.getP7());
                    ps.setObject(8, t4.getP8());
                    ps.setObject(9, t4.getP9());
                    ps.setObject(10, t4.getRequestDate());
                    progress.getAndIncrement();
                    LogWS.spreadProgress((int) (((float) progress.get()) / t4List.size() * 100));
                });
    }

    private void insertT5s(List<T5> t5List){}

    private void insertT6s(List<T6> t6List){
        AtomicInteger progress = new AtomicInteger();
        jdbcTemplate.batchUpdate("INSERT INTO " + T6_TABLE + " VALUES (\ndefault," +
                        "?::char(4), ?::char(3), ?::bigint, ?::char(6), ?::char(2), ?::int, ?::int, ?::date)",
                t6List,
                BATCH_SIZE,
                (ps, t3) -> {
                    ps.setObject(1, t3.getP1());
                    ps.setObject(2, t3.getP2());
                    ps.setObject(3, t3.getP3());
                    ps.setObject(4, t3.getP4());
                    ps.setObject(5, t3.getP5());
                    ps.setObject(6, t3.getP6());
                    ps.setObject(7, t3.getP7());
                    ps.setObject(8, t3.getRequestDate());
                    progress.getAndIncrement();
                    LogWS.spreadProgress((int) (((float) progress.get()) / t6List.size() * 100));
                });
    }

    private void insertLgots(List<Lgot> lgotList){
        AtomicInteger progress = new AtomicInteger();
        jdbcTemplate.batchUpdate("INSERT INTO " + LGOT_TABLE + " VALUES (\n" +
                "?::integer    , ?::char(5)    , " +
                "default       , ?::char(3)    , ?::char(2)    , ?::char       , ?::char       ,\n" +
                "?::char       , ?::char(4)    , ?::char(4)    , ?::char(5)    , ?::char(14)   ,\n" +
                "?::char(5)    , ?::char(10)   , ?::char       , ?::char(45)   , ?::char(20)   ,\n" +
                "?::numeric(3) , ?::boolean    , ?::numeric(3) , ?::integer    , ?::char       ,\n" +
                "?::numeric(2) , ?::date       , ?::date       , ?::char(8)    , ?::char(7)    ,\n" +
                "?::char(7)    , ?::real       , ?::real       , ?::char(7)    , ?::char(10)   ,\n" +
                "?::char(7)    , ?::char(11)   , ?::real       ,\n" +
                "?::date       )",
                lgotList,
                BATCH_SIZE,
                (ps, lgot) -> {
                    ps.setObject(1, lgot.getKey().getYyyymm());
                    ps.setObject(2, lgot.getKey().getList());
                    ps.setObject(3, lgot.getKey().getP2());
                    ps.setObject(4, lgot.getKey().getP3());
                    ps.setObject(5, lgot.getKey().getP4());
                    ps.setObject(6, lgot.getKey().getP5());
                    ps.setObject(7, lgot.getKey().getP6());
                    ps.setObject(8, lgot.getKey().getP7());
                    ps.setObject(9, lgot.getKey().getP8());
                    ps.setObject(10, lgot.getKey().getP9());
                    ps.setObject(11, lgot.getKey().getP10());
                    ps.setObject(12, lgot.getKey().getP11());
                    ps.setObject(13, lgot.getKey().getP12());
                    ps.setObject(14, lgot.getKey().getP13());
                    ps.setObject(15, lgot.getKey().getP14());
                    ps.setObject(16, lgot.getKey().getP15());
                    ps.setObject(17, lgot.getKey().getP16());
                    ps.setObject(18, lgot.getKey().getP17());
                    ps.setObject(19, lgot.getKey().getP18());
                    ps.setObject(20, lgot.getP19());
                    ps.setObject(21, lgot.getKey().getP20());
                    ps.setObject(22, lgot.getKey().getP21());
                    ps.setObject(23, lgot.getKey().getP22());
                    ps.setObject(24, lgot.getKey().getP23());
                    ps.setObject(25, lgot.getKey().getP24());
                    ps.setObject(26, lgot.getKey().getP25());
                    ps.setObject(27, lgot.getKey().getP26());
                    ps.setObject(28, lgot.getP27());
                    ps.setObject(29, lgot.getP28());
                    ps.setObject(30, lgot.getKey().getP29());
                    ps.setObject(31, lgot.getKey().getP30());
                    ps.setObject(32, lgot.getKey().getP31());
                    ps.setObject(33, lgot.getKey().getP32());
                    ps.setObject(34, lgot.getP33());
                    ps.setObject(35, lgot.getKey().getRequestDate());
                    progress.getAndIncrement();
                    LogWS.spreadProgress((int) (((float) progress.get()) / lgotList.size() * 100));
                });
    }
}
