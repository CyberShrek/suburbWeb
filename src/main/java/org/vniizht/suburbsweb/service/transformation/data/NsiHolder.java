package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.reference.*;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Scope("singleton")
public class NsiHolder {

    public Dor getDor(String kodd, String kodg) {
        return dorMap.get(kodd + kodg);
    }

    // String is used for codes
    private Map<String, Dor>    dorMap           = new HashMap<>();
    // Ranged are used for date ranges
    private Map<Ranged<String>, Stanv>  stanvMap = new HashMap<>();
    private Map<Ranged<String>, Site>   siteMap  = new HashMap<>();
    private Map<Ranged<String>, Plagn>  plagnMap = new HashMap<>();
    private Map<Ranged<Integer>, Sf>    sfMap    = new HashMap<>();
    private Map<Ranged<String>, Sublx>  sublxMap = new HashMap<>();

    @Autowired private DorRepository    dorRepo;
    @Autowired private StanvRepository  stanvRepo;
    @Autowired private SiteRepository   siteRepo;
    @Autowired private PlagnRepository  plagnRepo;
    @Autowired private SfRepository     sfRepo;
    @Autowired private SublxRepository  sublxRepo;

    @PostConstruct
    private void init() {
        java.util.Date startDate = new java.util.Date();
        System.out.println("Загружаю справочники в память...");
        dorRepo.findAll().forEach(dor -> dorMap
                .put(dor.getKodd() + dor.getKodg(), dor));
        stanvRepo.findAll().forEach(stanv -> stanvMap
                .put(new Ranged<>(stanv.getStan(), stanv.getDatani(), stanv.getDatakd()), stanv));
        siteRepo.findAll().forEach(site -> siteMap
                .put(new Ranged<>(site.getIdsite() + site.getGos(), site.getDatan(), site.getDatak()), site));
        plagnRepo.findAll().forEach(plagn -> plagnMap
                .put(new Ranged<>(plagn.getIdplagn() + plagn.getGos(), plagn.getDatan(), plagn.getDatak()), plagn));
        sfRepo.findAll().forEach(sf -> sfMap
                .put(new Ranged<>(sf.getVid(), sf.getDatan(), sf.getDatak()), sf));
        sublxRepo.findAll().forEach(sublx -> sublxMap
                .put(new Ranged<>(sublx.getLg(), sublx.getDatan(), sublx.getDatak()), sublx));
        System.out.println("Загрузка справочников завершена. Время загрузки: " + (new java.util.Date().getTime() - startDate.getTime()) + " мс.");
    }

    private static class Ranged<K> {

        public Ranged(K key, Date from, Date to) {
            this.key = key;
            this.from = from.getTime();
            this.to = to.getTime();
        }

        public K getKey() {
            return key;
        }

        public Long getFrom() {
            return from;
        }

        public Long getTo() {
            return to;
        }

        private final K    key;
        private final Long from;
        private final Long to;


    }
}
