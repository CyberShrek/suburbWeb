package org.vniizht.suburbsweb.service.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.data.entities.Route;

import java.util.*;

@Service
public class RoutesDao {
    @Autowired private JdbcTemplate jdbcTemplate;

    private final Map<String, Route> routesCache = new HashMap<>();

    public Route getRoute(Short routeNum, String depStation, String arrStation, Date date){
        String key = depStation + arrStation + date;
        if (!routesCache.containsKey(key)) {
            Route route = new Route();

            SqlRowSet prigRS = jdbcTemplate.queryForRowSet("SELECT * FROM getfunction.estimate_km_suburb(?, ?, ?, ?)",
                    routeNum, depStation, arrStation, date);

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
                        case 1: route.setRegionStart(est_obj_chr); break;
                        case 2: route.setRoadStart(est_obj_chr);   break;
                        case 3: route.setDepartmentStart(est_obj_chr);
                    }
                }
                else if (st2 != null && st2.equals(depStation)) {
                    switch (narr) {
                        case 1: route.setRegionEnd(est_obj_chr); break;
                        case 2: route.setRoadEnd(est_obj_chr);   break;
                        case 3: route.setDepartmentEnd(est_obj_chr);
                    }
                }
                if (narr == 5) {
                    mcdSet.add(pr_mcd);
                    if (est_obj_chr != null && est_obj_chr.trim().equals("1") && pr_mcd == 2)
                        route.setMcdDistance(rst);
                }
            }
            Character mcdType = null;
            switch (mcdSet.size()) {
                case 1: mcdType = mcdSet.contains(1) ? '0' : '1'; break;
                case 2: mcdType = mcdSet.contains(1) && mcdSet.contains(2) ? '2' : '3'; break;
                case 3: mcdType = mcdSet.contains(1) && mcdSet.contains(2) && mcdSet.contains(3) ? '4' : null;
            }
            route.setMcdType(mcdType);

            routesCache.put(key, route);
        }
        return routesCache.get(key);
    }

    public Route getRoute(String trainId, Character trainThread, Date trainDepartureDate,
                          String depStation, String arrStation) {
        String key = trainId + trainThread + trainDepartureDate + depStation + arrStation;
        if (!routesCache.containsKey(key)) {
            Route route = new Route();

            String queryForRoads       = "SELECT * FROM getfunction.passkm_estimate_for_gos_and_dor(?, ?, ?, 2, ?, ?)";
            String queryForDepartments = "SELECT * FROM getfunction.passkm_estimate_for_otd(?, ?, ?, ?, ?)";
            String queryForRegions     = "SELECT * FROM getfunction.passkm_estimate_for_stan_dcs_sf(?, ?, ?, 5, ?, ?)";

            SqlRowSet roadsRS       = jdbcTemplate.queryForRowSet(queryForRoads,       trainId, trainThread, trainDepartureDate, depStation, arrStation);
            SqlRowSet departmentsRS = jdbcTemplate.queryForRowSet(queryForDepartments, trainId, trainThread, trainDepartureDate, depStation, arrStation);
            SqlRowSet regionsRS     = jdbcTemplate.queryForRowSet(queryForRegions,     trainId, trainThread, trainDepartureDate, depStation, arrStation);

            while (roadsRS.next()) {
                String dor3 = roadsRS.getString("dor3");
                if (roadsRS.isFirst()) route.setRoadStart(dor3);
                else if (roadsRS.isLast()) route.setRoadEnd(dor3);
            }
            while (departmentsRS.next()) {
                String otd = departmentsRS.getString("otd");
                if (departmentsRS.isFirst()) route.setDepartmentStart(otd);
                else if (departmentsRS.isLast()) route.setDepartmentEnd(otd);
            }
            while (regionsRS.next()) {
                String sf = regionsRS.getString("sf");
                if (regionsRS.isFirst()) route.setRegionStart(sf);
                else if (regionsRS.isLast()) route.setRegionEnd(sf);
            }

            routesCache.put(key, route);
        }
        return routesCache.get(key);
    }

    public void clearCache() {
        routesCache.clear();
    }
}
