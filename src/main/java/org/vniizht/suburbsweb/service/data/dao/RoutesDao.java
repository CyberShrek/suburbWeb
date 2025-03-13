package org.vniizht.suburbsweb.service.data.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.data.entities.routes.*;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.util.Log;

import java.util.*;

@Service
public class RoutesDao {
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private Handbook handbook;

    private final Map<String, RouteGroup> routesCache = new HashMap<>();

    public RouteGroup getRouteGroup(Short routeNum,
                                    String depStation,
                                    String arrStation,
                                    Date date) {
        String key = depStation + arrStation + date;
        if (!routesCache.containsKey(key)) {
            RouteGroup group = new RouteGroup();

             System.out.println("SELECT * FROM getfunction.estimate_km_suburb(" + routeNum + ", '" + date + "', '" + depStation + "', '" + arrStation + "')");


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

                switch (narr) {
                    case 1: group.addRegionRoute(RegionRoute.builder()
                            .region(obj_chr)
                            .okato(handbook.getOkatoByRegion(obj_chr, date))
                            .distance(rst)
                            .build());
                    break;
                    case 2: group.addRoadRoute(RoadRoute.builder()
                            .road(obj_chr)
                            .distance(rst)
                            .build());
                    break;
                    case 3: group.addDepartmentRoute(DepartmentRoute.builder()
                            .road(String.valueOf(obj_int))
                            .department(obj_chr)
                            .distance(rst)
                            .build());
                    break;
                    case 4: group.addDcsRoute(DcsRoute.builder()
                            .road(String.valueOf(obj_int))
                            .dcs(obj_chr)
                            .distance(rst)
                            .build());
                    break;
                    case 5:
                        boolean isMcd = obj_chr != null && obj_chr.trim().equals("1");
                        group.addMcdRoute(McdRoute.builder()
                            .code(isMcd ? '1' : '0')
                            .distance(isMcd ? rst : 0)
                            .build());
                    break;
                    case 6: group.addFollowRoute(FollowRoute.builder()
                            .road(String.valueOf(obj_int))
                            .region(obj_chr)
                            .okato(handbook.getOkatoByRegion(obj_chr, date))
                            .distance(rst)
                            .build());
                }
            }
            routesCache.put(key, group);
        }
        return routesCache.get(key);
    }

    public RouteGroup getRouteGroup(String trainId, Character trainThread, Date trainDepartureDate,
                                    String depStation, String arrStation) {
        String key = trainId + trainThread + trainDepartureDate + depStation + arrStation;
        if (!routesCache.containsKey(key)) {
            RouteGroup group = new RouteGroup();

            String roadsSql       = "SELECT * FROM getfunction.passkm_estimate_for_gos_and_dor(?, ?, ?, 2, ?, ?)";
            String DepartmentsSql = "SELECT * FROM getfunction.passkm_estimate_for_otd(?, ?, ?, ?, ?)";
            String miscSql        = "SELECT * FROM getfunction.passkm_estimate_for_stan_dcs_sf(?, ?, ?, ?, ?, ?)";

            SqlRowSet roadsRS       = jdbcTemplate.queryForRowSet(roadsSql,       trainId, trainThread, trainDepartureDate, depStation, arrStation);
            SqlRowSet departmentsRS = jdbcTemplate.queryForRowSet(DepartmentsSql, trainId, trainThread, trainDepartureDate, depStation, arrStation);
            SqlRowSet regionsRS     = jdbcTemplate.queryForRowSet(miscSql, trainId, trainThread, trainDepartureDate, 5, depStation, arrStation);
            SqlRowSet followsRS     = jdbcTemplate.queryForRowSet(miscSql, trainId, trainThread, trainDepartureDate, 7, depStation, arrStation);
            SqlRowSet dcsRS         = jdbcTemplate.queryForRowSet(miscSql, trainId, trainThread, trainDepartureDate, 4, depStation, arrStation);

            while (roadsRS.next()) {
                group.addRoadRoute(RoadRoute.builder()
                        .road(roadsRS.getString("dor3"))
                        .build());
            }
            while (departmentsRS.next()) {
                group.addDepartmentRoute(DepartmentRoute.builder()
                        .road(departmentsRS.getString("dor3"))
                        .department(departmentsRS.getString("otd"))
                        .distance(departmentsRS.getShort("km"))
                        .build());
            }
            while (regionsRS.next()) {
                String sf = regionsRS.getString("sf");
                group.addRegionRoute(RegionRoute.builder()
                        .region(sf)
                        .okato(handbook.getOkatoByRegion(sf, trainDepartureDate))
                        .distance(regionsRS.getShort("km"))
                        .build());
            }
            while (followsRS.next()) {
                group.addFollowRoute(FollowRoute.builder()
                        .road(followsRS.getString("dor3"))
                        .region(followsRS.getString("sf"))
                        .okato(handbook.getOkatoByRegion(followsRS.getString("sf"), trainDepartureDate))
                        .distance(followsRS.getShort("km"))
                        .build());
            }
            while (dcsRS.next()) {
                group.addDcsRoute(DcsRoute.builder()
                        .road(dcsRS.getString("dor3"))
                        .dcs(dcsRS.getString("dcs"))
                        .distance(dcsRS.getShort("km"))
                        .build());
            }

            routesCache.put(key, group);
        }
        return routesCache.get(key);
    }

    public void clearCache() {
        routesCache.clear();
    }
}
