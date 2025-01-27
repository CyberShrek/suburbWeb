package org.vniizht.suburbsweb.service.result;

import lombok.Getter;
import org.vniizht.suburbsweb.service.data.entities.level3.co22.*;
import org.vniizht.suburbsweb.service.data.entities.level3.lgot.Lgot;
import org.vniizht.suburbsweb.service.data.entities.level3.meta.CO22Meta;
import org.vniizht.suburbsweb.service.data.entities.routes.*;
import org.vniizht.suburbsweb.service.handbook.Handbook;
import org.vniizht.suburbsweb.service.data.dao.Level2Dao;
import org.vniizht.suburbsweb.service.data.dao.RoutesDao;
import org.vniizht.suburbsweb.util.Util;
import org.vniizht.suburbsweb.websocket.LogWS;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

abstract public class Level3 <L2_RECORD extends Level2Dao.Record> {

    // Конечные результаты - трансформированные записи второго уровня, готовые к записи в базу данных
    @Getter private final Map<String, CO22> co22Result = new HashMap<>();
    @Getter private final List<Lgot>        lgotResult = new ArrayList<>();

    // Подсчитанное время на сопутствующие операции (с)
    @Getter private float transformationTime   = 0;
    @Getter private float routesSearchTime     = 0;

    // Функция обработки каждой записи второго уровня
    abstract protected void next(L2_RECORD record);

    abstract protected T1        getT1();
    abstract protected Lgot      getLgot();

    // Мультипликатор
    abstract protected Set<T1> multiplyT1(T1 t1);

    // Детали общие и метаданные
    abstract protected RouteGroup getRouteGroup();
    abstract protected CO22Meta getMeta();

    // Проверка существования t1
    abstract protected boolean   t1Exists();

    // Компоненты T1
             protected String    getT1P13(RoadRoute route)       {return route != null ? route.getRoad() : null;}
             protected String    getT1P14(DepartmentRoute route) {return route != null ? route.getDepartment() : null;}
             protected String    getT1P16(RegionRoute route)     {return route != null ? route.getRegion() : null;}
             protected String    getT1P27(RoadRoute route)       {return route != null ? route.getRoad() : null;}
             protected String    getT1P28(DepartmentRoute route) {return route != null ? route.getDepartment() : null;}
             protected String    getT1P29(RegionRoute route)     {return route != null ? route.getRegion() : null;}
             protected Short     getT1P62(List<McdRoute> routes) {
                 return (short) routes.stream().mapToInt(McdRoute::getDistance).sum();
             }
             protected Character getT1P63(List<McdRoute> routes) {
                 if (routes.stream().anyMatch(mcdRoute -> mcdRoute.getCode() == '1')) {
                     switch (routes.size()) {
                         case 1:  return '1';
                         case 2:  return routes.get(0).getCode() == 0 ? '2' : '3';
                         default: return '4';
                     }
                 }
                 return '0';
             }

    // Проверка существования льготы
    abstract protected boolean lgotExists();

    // Подсчёт средних стоимостей на километр по регионам
    abstract protected double getRegionIncomePerKm(String region);
    abstract protected double getRegionOutcomePerKm(String region);

    protected final Set<L2_RECORD> records;
    protected final Handbook handbook;
    protected final RoutesDao routesDao;
    private Long t1Serial;

    protected Level3(
            Set<L2_RECORD> records,
            Handbook handbook,
            RoutesDao routesDao,
            Long initialT1Serial) {
        this.records   = records;
        this.handbook  = handbook;
        this.routesDao = routesDao;
        this.t1Serial  = initialT1Serial;
    }

    protected void transform() {
        int progress = 0;
        for (L2_RECORD record : records) {
            transformRecord(record);
            progress++;
            LogWS.spreadProgress((int) ((float) progress / records.size() * 100));
        }
        assignSerials();
        roundTimes();
    }

    private void transformRecord(L2_RECORD record) {
        next(record);
        if(t1Exists()) {
            AtomicReference<RouteGroup> routeGroup = new AtomicReference<>();
            routesSearchTime   += Util.measureTime(() -> routeGroup.set(getRouteGroup()));
            transformationTime += Util.measureTime(() -> {
                Set<T1> t1Set = multiplyT1(buildT1(routeGroup.get()));
                t1Set.forEach(t1 -> {
                    String key = t1.getKey().toString();
                    CO22 co22 = new CO22(t1, routeGroup.get());
                    if(co22Result.containsKey(key))
                        co22Result.get(key).merge(co22);
                    else
                        co22Result.put(key, co22);
                });
            });
        }
        if(lgotExists()) {
            transformationTime += Util.measureTime(() -> lgotResult.add(buildLgot()));
        }
    }

    private void assignSerials() {
        co22Result.values().forEach(CO22::assignSerials);
    }

    private void roundTimes() {
        transformationTime = (float) Math.round(transformationTime * 100) / 100;
        routesSearchTime   = (float) Math.round(routesSearchTime   * 100) / 100;
    }

    private T1 buildT1(RouteGroup routeGroup) {
        T1 t1 = getT1();

        t1.setKey(t1.getKey().toBuilder()
                .p13(getT1P13(routeGroup.getFirstRoadRoute()))
                .p14(getT1P14(routeGroup.getFirstDepartmentRoute()))
                .p16(getT1P16(routeGroup.getFirstRegionRoute()))
                .p27(getT1P27(routeGroup.getLastRoadRoute()))
                .p28(getT1P28(routeGroup.getLastDepartmentRoute()))
                .p29(getT1P29(routeGroup.getLastRegionRoute()))
                .p62(getT1P62(routeGroup.getMcdRoutes()))
                .p63(getT1P63(routeGroup.getMcdRoutes()))
                .build());

        return t1;
    }

    private Lgot buildLgot() {
        return getLgot();
    }

    // ЦО22 включая все дочерние записи
    @Getter public class CO22 {
        private final T1 t1;
        private final List<T2>       t2 = new ArrayList<>();
        private final List<T3>       t3 = new ArrayList<>();
        private final List<T4>       t4 = new ArrayList<>();
        private final List<T6>       t6 = new ArrayList<>();
        private final Set<CO22Meta> metas = new HashSet<>();

        CO22(T1 t1, RouteGroup routeGroup) {
            boolean hasCosts = String.valueOf(t1.getKey().getYyyymm()).equals(t1.getKey().getP3() + t1.getKey().getP4());
            Date requestDate = t1.getKey().getRequestDate();
            this.t1 = t1;
            routeGroup.getDepartmentRoutes().forEach(route  -> t2.add(new T2(requestDate, route)));
            routeGroup.getRegionRoutes().forEach(route      -> t3.add(new T3(requestDate, route)));
            routeGroup.getFollowRoutes().forEach(route      -> t4.add(new T4(requestDate, route,
                    hasCosts ? (long) getRegionIncomePerKm(route.getRegion()) : 0,
                    hasCosts ? (long) getRegionOutcomePerKm(route.getRegion()) : 0)));
            routeGroup.getDcsRoutes().forEach(route         -> t6.add(new T6(requestDate, route)));
            metas.add(getMeta());

            if(hasCosts)
                arrangeCosts();
        }

        public void merge(CO22 co22) {
            // Агрегация
            t1.merge(co22.t1);
            t2.addAll(co22.t2);
            t3.addAll(co22.t3);
            t4.addAll(co22.t4);
            t6.addAll(co22.t6);
            metas.addAll(co22.metas);
        }

        void assignSerials() {
            t1.getKey().setP2(t1Serial);
            t2.forEach(t -> t.setP3(t1Serial));
            t3.forEach(t -> t.setP3(t1Serial));
            t4.forEach(t -> t.setP3(t1Serial));
            t6.forEach(t -> t.setP3(t1Serial));
            metas.forEach(t -> t.setT1id(t1Serial));
            t1Serial++;
        }

        private void arrangeCosts() {
            if (t4.isEmpty()) return;
            long incomeDelta = t1.getP36() - t4.stream().mapToLong(T4::getP7).sum();
            long outcomeDelta = t1.getP44() - t4.stream().mapToLong(T4::getP8).sum();
            T4 lastT4 = t4.get(t4.size() - 1);
            lastT4.setP7(lastT4.getP7() + incomeDelta);
            lastT4.setP8(lastT4.getP8() + outcomeDelta);
        }
    }
}