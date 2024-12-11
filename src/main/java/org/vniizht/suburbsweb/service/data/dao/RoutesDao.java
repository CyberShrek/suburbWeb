package org.vniizht.suburbsweb.service.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.data.entities.routes.*;
import org.vniizht.suburbsweb.service.handbook.Handbook;

import java.util.*;

@Service
public class RoutesDao {
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private Handbook handbook;

    private final Map<String, RouteGroup> routesCache = new HashMap<>();

    public RouteGroup getRoutes(Short routeNum,
                                String depStation,
                                String arrStation,
                                Date date) {
        String key = depStation + arrStation + date;
        if (!routesCache.containsKey(key)) {
            RouteGroup routeGroup = new RouteGroup(routeNum);

            SqlRowSet prigRS = jdbcTemplate.queryForRowSet("SELECT * FROM getfunction.estimate_km_suburb(?, ?, ?, ?)",
                    routeNum, date, depStation, arrStation);

            while (prigRS.next()){
                int    narr     = prigRS.getInt("narr");
                String st1      = prigRS.getString("sto");
                String st2      = prigRS.getString("stn");
                String obj_chr  = prigRS.getString("obj_chr");
                int    obj_int  = prigRS.getInt("obj_int");
                int    pr_mcd   = prigRS.getInt("pr_mcd");
                short  rst      = prigRS.getShort("rst_p");

                boolean isStart = st1 != null && st1.equals(depStation);
                boolean isEnd   = st2 != null && st2.equals(arrStation);

                switch (narr) {
                    case 1: routeGroup.addRegionRoute((RegionRoute) RegionRoute.builder()
                            .regionStart(isStart ? obj_chr : null)
                            .regionEnd(isEnd ? obj_chr : null)
                            .region(obj_chr)
                            .okato(handbook.getOkatoByRegion(obj_chr, date))
                            .distance(rst)
                            .build());
                    break;
                    case 2: routeGroup.addRoadRoute((RoadRoute) RoadRoute.builder()
                            .roadStart(isStart ? obj_chr : null)
                            .roadEnd(isEnd ? obj_chr : null)
                            .road(obj_chr)
                            .distance(rst)
                            .build());
                    break;
                    case 3: routeGroup.addDepartmentRoute((DepartmentRoute) DepartmentRoute.builder()
                            .road(String.valueOf(obj_int))
                            .department(obj_chr)
                            .departmentStart(isStart ? obj_chr : null)
                            .departmentEnd(isEnd ? obj_chr : null)
                            .distance(rst)
                            .build());
                    break;
                    case 4: routeGroup.addDcsRoute((DcsRoute) DcsRoute.builder()
                            .road(String.valueOf(obj_int))
                            .dcs(obj_chr)
                            .distance(rst)
                            .build());
                    break;
                    case 5:
                        boolean isMcd = obj_chr != null && obj_chr.trim().equals("1");
                        routeGroup.addMcdRoute((McdRoute) McdRoute.builder()
                            .code(isMcd ? '1' : '0')
                            .distance(isMcd ? rst : 0)
                            .build());
                    break;
                    case 6: routeGroup.addFollowRoute((FollowRoute) FollowRoute.builder()
                            .road(String.valueOf(obj_int))
                            .okato(handbook.getOkatoByRegion(obj_chr, date))
                            .distance(rst)
                            .build());
                }
            }
            routesCache.put(key, routeGroup);
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
