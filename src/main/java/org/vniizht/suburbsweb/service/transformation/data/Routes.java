package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.misc.Route;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class Routes {
    @Autowired private JdbcTemplate jdbcTemplate;

    private final Map<String, List<Route>> cache = new HashMap<>();

    public List<Route> findPrigRoutes(String stationFrom, String stationTo, Date date) {
        String key = stationFrom + stationTo + date;
        if (!cache.containsKey(key)) {
            String query = "SELECT * FROM getfunction.estimate_km_suburb(?, ?, ?)";
//            String passQuery = "SELECT * FROM nsi.passkm_estimate_for_otd('0001Э','А', ?, ?, ?)";
//            jdbcTemplate.queryForList(prigQuery, stationFrom, stationTo, date);
            cache.put(key, jdbcTemplate.query(query, new RouteRowMapper(), date, stationFrom, stationTo));
        }
        return cache.get(key);
    }

    public void clearCache() {
        cache.clear();
    }

    private static class RouteRowMapper implements RowMapper<Route> {

        @Override
        public Route mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Route.builder()
                    .type(rs.getInt("narr"))
                    .stationBeg(rs.getString("st1"))
                    .stationEnd(rs.getString("st2"))
                    .matterStr(rs.getString("est_obj_chr"))
                    .matterInt(rs.getInt("est_obj_int"))
                    .mcd(rs.getInt("pr_mcd"))
                    .distance(rs.getInt("rst"))
                    .build();
        }
    }
}
