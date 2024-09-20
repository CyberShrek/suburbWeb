package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class Routes {
    @Autowired private JdbcTemplate jdbcTemplate;

    private final Map<String, String> cache = new HashMap<>();

    public void findRoutes(String stationFrom, String stationTo, Date date) {
        String key = stationFrom + stationTo + date;
        if (!cache.containsKey(key)) {
            String prigQuery = "SELECT * FROM getfunction.estimate_km_suburb(?, ?, ?)";
            String passQuery = "SELECT * FROM nsi.passkm_estimate_for_otd('0001Э','А', ?, ?, ?)";
//            jdbcTemplate.queryForList(prigQuery, stationFrom, stationTo, date);
            jdbcTemplate.queryForList(passQuery, date, stationFrom, stationTo);
            cache.put(key, "yes");
            System.out.println(cache.size());
        }
    }
}
