package org.vniizht.suburbsweb.service.data.entities.routes;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RouteGroup {

    private List<RoadRoute>       roadRoutes;
    private List<DepartmentRoute> departmentRoutes;
    private List<RegionRoute>     regionRoutes;
    private List<DcsRoute>        dcsRoutes;
    private List<McdRoute>        mcdRoutes;
    private List<FollowRoute>     followRoutes;
    
    public void addRoadRoute(RoadRoute route) {
        roadRoutes = collectRoute(route, roadRoutes);
    }

    public void addDepartmentRoute(DepartmentRoute route) {
        departmentRoutes = collectRoute(route, departmentRoutes);
    }

    public void addRegionRoute(RegionRoute route) {
        regionRoutes = collectRoute(route, regionRoutes);
    }

    public void addDcsRoute(DcsRoute route) {
        dcsRoutes = collectRoute(route, dcsRoutes);
    }

    public void addMcdRoute(McdRoute route) {
        mcdRoutes = collectRoute(route, mcdRoutes);
    }

    public void addFollowRoute(FollowRoute route) {
        followRoutes = collectRoute(route, followRoutes);
    }

    public RoadRoute getFirstRoadRoute() {
        return getFirstRouteOrNull(roadRoutes);
    }
    public RoadRoute getLastRoadRoute() {
        return getLastRouteOrNull(roadRoutes);
    }

    public DepartmentRoute getFirstDepartmentRoute() {
        return getFirstRouteOrNull(departmentRoutes);
    }
    public DepartmentRoute getLastDepartmentRoute() {
        return getLastRouteOrNull(departmentRoutes);
    }

    public RegionRoute getFirstRegionRoute() {
        return getFirstRouteOrNull(regionRoutes);
    }
    public RegionRoute getLastRegionRoute() {
        return getLastRouteOrNull(regionRoutes);
    }

    public DcsRoute getFirstDcsRoute() {
        return getFirstRouteOrNull(dcsRoutes);
    }
    public DcsRoute getLastDcsRoute() {
        return getLastRouteOrNull(dcsRoutes);
    }

    public McdRoute getFirstMcdRoute() {
        return getFirstRouteOrNull(mcdRoutes);
    }
    public McdRoute getLastMcdRoute() {
        return getLastRouteOrNull(mcdRoutes);
    }

    public FollowRoute getFirstFollowRoute() {
        return getFirstRouteOrNull(followRoutes);
    }
    public FollowRoute getLastFollowRoute() {
        return getLastRouteOrNull(followRoutes);
    }
    
    public void merge(RouteGroup group) {
        group.getRegionRoutes().forEach(this::addRegionRoute);
        group.getRoadRoutes().forEach(this::addRoadRoute);
        group.getDepartmentRoutes().forEach(this::addDepartmentRoute);
        group.getDcsRoutes().forEach(this::addDcsRoute);
        group.getMcdRoutes().forEach(this::addMcdRoute);
        group.getFollowRoutes().forEach(this::addFollowRoute);
    }

    private <T extends Route> List<T> collectRoute(T route, List<T> collector) {
        if(collector == null)
            collector = new ArrayList<>();

        collector.add(route);
        route.serial = (short) collector.size();
        return collector;
    }

    private <T extends Route> T getFirstRouteOrNull(List<T> routes) {
        return routes == null || routes.isEmpty() ? null : routes.get(0);
    }
    private <T extends Route> T getLastRouteOrNull(List<T> routes) {
        return routes == null || routes.isEmpty() ? null : routes.get(routes.size() - 1);
    }
}