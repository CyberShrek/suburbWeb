package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.route.PassRoute;
import org.vniizht.suburbsweb.model.route.PrigRoute;
import org.vniizht.suburbsweb.model.route.PrigRouteOld;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Service
public class Routes {
    @Autowired private JdbcTemplate jdbcTemplate;

    private final Map<String, PrigRoute> prigCache = new HashMap<>();
    private final Map<String, PassRoute> passCache = new HashMap<>();

    public PrigRoute getPrigRoute(String depStation, String arrStation, Date date){
        String key = depStation + arrStation + date;
        if (!prigCache.containsKey(key)) {
            PrigRoute prigRoute = new PrigRoute();

            SqlRowSet prigRS = jdbcTemplate.queryForRowSet("SELECT * FROM getfunction.estimate_km_suburb(?, ?, ?)",
                    depStation, arrStation, date);

            Set <Integer> mcdSet = new HashSet<>();

            while (prigRS.next()){
                int narr = prigRS.getInt("narr");
                String st1 = prigRS.getString("st1");
                String st2 = prigRS.getString("st2");
                String est_obj_chr = prigRS.getString("est_obj_chr");
                int pr_mcd = prigRS.getInt("pr_mcd");
                short rst = prigRS.getShort("rst");

                if(st1 != null && st1.equals(depStation)){
                    switch (narr) {
                        case 1 -> prigRoute.setRegionStart(est_obj_chr);
                        case 2 -> prigRoute.setRoadStart(est_obj_chr);
                        case 3 -> prigRoute.setDepartmentStart(est_obj_chr);
                    }
                }
                else if (st2 != null && st2.equals(depStation)) {
                    switch (narr) {
                        case 1 -> prigRoute.setRegionEnd(est_obj_chr);
                        case 2 -> prigRoute.setRoadEnd(est_obj_chr);
                        case 3 -> prigRoute.setDepartmentEnd(est_obj_chr);
                    }
                }
                if (narr == 5) {
                    mcdSet.add(pr_mcd);
                    if (est_obj_chr != null && est_obj_chr.trim().equals("1") && pr_mcd == 2)
                        prigRoute.setMcdDistance(rst);
                }
            }
            prigRoute.setMcdType(switch (mcdSet.size()) {
                case 1 -> mcdSet.contains(1) ? '0' : '1';
                case 2 -> mcdSet.contains(1) && mcdSet.contains(2) ? '2' : '3';
                case 3 -> mcdSet.contains(1) && mcdSet.contains(2) && mcdSet.contains(3) ? '4' : null;
                default -> null;
            });

            prigCache.put(key, prigRoute);
        }
        return prigCache.get(key);
    }

    public PassRoute getPassRoute(String trainId, Character trainThread, Date trainDepartureDate,
                                  String depStation, String arrStation) {
        String key = trainId + trainThread + trainDepartureDate + depStation + arrStation;
        if (!passCache.containsKey(key)) {
            PassRoute passRoute = new PassRoute();

            String queryForRoads       = "SELECT * FROM getfunction.passkm_estimate_for_gos_and_dor(?, ?, ?, 2, ?, ?)";
            String queryForDepartments = "SELECT * FROM nsi.passkm_estimate_for_otd(?, ?, ?, ?, ?)";
            String queryForRegions     = "SELECT * FROM nsi.passkm_estimate_for_stan_dcs_sf(?, ?, ?, 5, ?, ?)";

            SqlRowSet roadsRS       = jdbcTemplate.queryForRowSet(queryForRoads,       trainId, trainThread, trainDepartureDate, depStation, arrStation);
            SqlRowSet departmentsRS = jdbcTemplate.queryForRowSet(queryForDepartments, trainId, trainThread, trainDepartureDate, depStation, arrStation);
            SqlRowSet regionsRS     = jdbcTemplate.queryForRowSet(queryForRegions,     trainId, trainThread, trainDepartureDate, depStation, arrStation);

            while (roadsRS.next()) {
                String dor3 = roadsRS.getString("dor3");
                if (roadsRS.isFirst()) passRoute.setRoadStart(dor3);
                else if (roadsRS.isLast()) passRoute.setRoadEnd(dor3);
            }
            while (departmentsRS.next()) {
                String otd = departmentsRS.getString("otd");
                if (departmentsRS.isFirst()) passRoute.setDepartmentStart(otd);
                else if (departmentsRS.isLast()) passRoute.setDepartmentEnd(otd);
            }
            while (regionsRS.next()) {
                String sf = regionsRS.getString("sf");
                if (regionsRS.isFirst()) passRoute.setRegionStart(sf);
                else if (regionsRS.isLast()) passRoute.setRegionEnd(sf);
            }
            
            passCache.put(key, passRoute);
        }
        return passCache.get(key);
    }

    public void clearCache() {
        prigCache.clear();
    }

    private static class PrigRouteRowMapper implements RowMapper<PrigRouteOld> {

        @Override
        public PrigRouteOld mapRow(ResultSet rs, int rowNum) throws SQLException {
            return PrigRouteOld.builder()
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
