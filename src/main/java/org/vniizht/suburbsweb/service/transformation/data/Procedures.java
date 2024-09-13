package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Map;

@Service
public class Procedures {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void chTripsPerMes(String fGos, int fSeasonTickCode, int fPeriod, String fBegDate, String fEndDate) {
        String query = "SELECT god_mes, ch_trips FROM getFunction.ch_trips__per__mes('"+ fGos +"', "+ fSeasonTickCode +", "+ fPeriod +", '"+ fBegDate +"'::date, '"+ fEndDate +"'::date)";
        jdbcTemplate.execute(query);
    }
}
