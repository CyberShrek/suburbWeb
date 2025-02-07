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

    // Проверка существования t1
    abstract protected boolean   t1Exists();

    // Проверка существования льгот
    abstract protected boolean lgotExists();

    abstract protected T1        getT1();
    abstract protected Lgot      getLgot();
    abstract protected CO22Meta  getMeta();

    // Мультипликатор
    abstract protected Set<T1> multiplyT1(T1 t1);

    // Маршруты
    abstract protected RouteGroup getRouteGroup();

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
        arrangeResult();
        roundTimes();
    }

    private void transformRecord(L2_RECORD record) {
        next(record);
        if(t1Exists()) {
            AtomicReference<RouteGroup> routeGroup = new AtomicReference<>();
            routesSearchTime   += Util.measureTime(() -> routeGroup.set(getRouteGroup()));
            transformationTime += Util.measureTime(() -> {
                T1 t1           = getT1();
                t1.setRoutes(routeGroup.get());
                multiplyT1(t1).forEach(t1Copy -> {
                    String key = t1Copy.getKey().toString();
                    if(co22Result.containsKey(key))
                        co22Result.get(key).merge(t1Copy);
                    else
                        co22Result.put(key, new CO22(t1Copy, routeGroup.get()));
                });
            });
        }
        if(lgotExists()) {
            transformationTime += Util.measureTime(() -> lgotResult.add(getLgot()));
        }
    }

    private void arrangeResult() {
        co22Result.values().forEach(co22 -> {
            co22.assignSerials();
            co22.arrangeCosts();
        });
    }

    private void roundTimes() {
        transformationTime = (float) Math.round(transformationTime * 100) / 100;
        routesSearchTime   = (float) Math.round(routesSearchTime   * 100) / 100;
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
            Date requestDate = t1.getKey().getRequestDate();
            this.t1 = t1;
            routeGroup.getDepartmentRoutes().forEach(route  -> t2.add(new T2(requestDate, route)));
            routeGroup.getRegionRoutes().forEach(route      -> t3.add(new T3(requestDate, route)));
            routeGroup.getFollowRoutes().forEach(route      -> t4.add(new T4(requestDate, route,
                    (long) getRegionIncomePerKm(route.getRegion()),
                    (long) getRegionOutcomePerKm(route.getRegion()))));
            routeGroup.getDcsRoutes().forEach(route         -> t6.add(new T6(requestDate, route)));
            metas.add(getMeta());
        }

        void merge(T1 t1) {
            this.t1.merge(t1);
            metas.add(getMeta());
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

        void arrangeCosts() {
            if (t4.isEmpty()) return;
            float incomeDelta  = (float) (t1.getP36() - t4.stream().mapToDouble(T4::getP7).sum());
            float outcomeDelta = (float) (t1.getP44() - t4.stream().mapToDouble(T4::getP8).sum());
            T4 edgeT4 = t1.getKey().getP21() == '6' ? t4.get(0) : t4.get(t4.size() - 1);
            edgeT4.setP7(edgeT4.getP7() + incomeDelta);
            edgeT4.setP8(edgeT4.getP8() + outcomeDelta);
        }
    }
}