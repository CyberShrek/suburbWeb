package org.vniizht.suburbsweb.service.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.T1;
import org.vniizht.suburbsweb.service.data.entities.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.data.repository.level3.LgotRepo;
import org.vniizht.suburbsweb.service.data.repository.level3.T1Repo;

import java.util.Date;

@Service
public class Level3Dao {

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

    public void deleteForDate(Date date){
        jdbcTemplate.update("DELETE FROM prigl3.co22_t1 WHERE request_date = ?", date);
        jdbcTemplate.update("DELETE FROM prigl3.lgot WHERE request_date = ?", date);
    }

    public void save(T1 t1){
//        System.out.println("t1: " + t1.getKey());
        jdbcTemplate.update("INSERT INTO prigl3.co22_t1 VALUES (" +
                "?::integer, ?::date, " +
                "?::char(4)    , default       , ?::char(4)    , ?::char(2)    , ?::char(3)    ,\n" +
                "?::char(3)    , ?::char(3)    , ?::char(9)    , ?::char(9)    , ?::char(2)    , " +
                "?::char(5)    , ?::char(4)    , ?::char(3)    , ?::char(2)    , ?::char(7)    , " +
                "?::char(2)    , ?::char(5)    , ?::char(3)    , ?::char       , ?::char(2)    , " +
                "?::char       , ?::char       , ?::char       , ?::char(4)    , ?::char       , " +
                "?::char(2)    , ?::char(3)    , ?::char(2)    , ?::char(2)    , ?::char(5)    , " +
                "?::char(3)    , ?::integer    , ?::numeric(9) , ?::numeric(11), ?::numeric(11), " +
                "?::numeric(11), ?::numeric(11), ?::numeric(11), ?::numeric(11), ?::numeric(11), " +
                "?::numeric(11), ?::numeric(11), ?::numeric(11), ?::numeric(11), ?::numeric(11), " +
                "?::numeric(11), ?::numeric(11), ?::numeric(11), ?::numeric(11), ?::numeric(11), " +
                "?::numeric(9) , ?::char       , ?::char(4)    , ?::char(7)    , ?::char       , " +
                "?::char(3)    , ?::char       , ?::char       , ?::char       , ?::char(3)    , " +
                "?::char       , ?::integer    , ?::char       )",
                t1.getKey().getYyyymm(), t1.getKey().getRequestDate(),
                t1.getKey().getP1(),                t1.getKey().getP3(), t1.getKey().getP4(), t1.getKey().getP5(), t1.getKey().getP6(), t1.getKey().getP7(), t1.getKey().getP8(), t1.getKey().getP9(), t1.getKey().getP10(),
                t1.getKey().getP11(), t1.getKey().getP12(), t1.getKey().getP13(), t1.getKey().getP14(), t1.getKey().getP15(), t1.getKey().getP16(), t1.getKey().getP17(), t1.getKey().getP18(), t1.getKey().getP19(), t1.getKey().getP20(),
                t1.getKey().getP21(), t1.getKey().getP22(), t1.getKey().getP23(), t1.getKey().getP24(), t1.getKey().getP25(), t1.getKey().getP26(), t1.getKey().getP27(), t1.getKey().getP28(), t1.getKey().getP29(), t1.getKey().getP30(),
                t1.getKey().getP31(), t1.getKey().getP32(), t1.getP33(), t1.getP34(), t1.getP35(), t1.getP36(), t1.getP37(), t1.getP38(), t1.getP39(), t1.getP40(),
                t1.getP41(), t1.getP42(), t1.getP43(), t1.getP44(), t1.getP45(), t1.getP46(), t1.getP47(), t1.getP48(), t1.getP49(), t1.getP50(),
                t1.getP51(), t1.getKey().getP52(), t1.getKey().getP53(), t1.getKey().getP54(), t1.getKey().getP55(), t1.getKey().getP56(), t1.getKey().getP57(), t1.getKey().getP58(), t1.getKey().getP59(), t1.getKey().getP60(),
                t1.getKey().getP61(), t1.getKey().getP62(), t1.getKey().getP63());
    }

    public void save(Lgot lgot){
//        System.out.println("lgot: " + lgot);
        jdbcTemplate.update("INSERT INTO prigl3.lgot VALUES (" +
                "?::integer    , ?::char(5)    , " +
                "default       , ?::char(3)    , ?::char(2)    , ?::char       , ?::char       , " +
                "?::char       , ?::char(4)    , ?::char(4)    , ?::char(5)    , ?::char(14)   , " +
                "?::char(5)    , ?::char(10)   , ?::char       , ?::char(45)   , ?::char(20)   , " +
                "?::numeric(3) , ?::boolean    , ?::numeric(3) , ?::integer    , ?::char       , " +
                "?::numeric(2) , ?::date       , ?::date       , ?::char(8)    , ?::char(7)    , " +
                "?::char(7)    , ?::real       , ?::real       , ?::char(7)    , ?::char(10)   ," +
                "?::char(7)    , ?::char(11)   , ?::real       , " +
                "?::date       )",
                lgot.getKey().getYyyymm(), lgot.getKey().getList(),
                                        lgot.getKey().getP2(),  lgot.getKey().getP3(),  lgot.getKey().getP4(),  lgot.getKey().getP5(),  lgot.getKey().getP6(),  lgot.getKey().getP7(),  lgot.getKey().getP8(),  lgot.getKey().getP9(), lgot.getKey().getP10(),
                lgot.getKey().getP11(), lgot.getKey().getP12(), lgot.getKey().getP13(), lgot.getKey().getP14(), lgot.getKey().getP15(), lgot.getKey().getP16(), lgot.getKey().getP17(), lgot.getKey().getP18(),         lgot.getP19(), lgot.getKey().getP20(),
                lgot.getKey().getP21(), lgot.getKey().getP22(), lgot.getKey().getP23(), lgot.getKey().getP24(), lgot.getKey().getP25(), lgot.getKey().getP26(),          lgot.getP27(),          lgot.getP28(), lgot.getKey().getP29(), lgot.getKey().getP30(),
                lgot.getKey().getP31(), lgot.getKey().getP32(), lgot.getP33(),
                lgot.getKey().getRequestDate());
    }
}
