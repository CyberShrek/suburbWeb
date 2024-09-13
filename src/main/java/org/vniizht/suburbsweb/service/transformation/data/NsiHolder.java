package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.reference.*;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.*;

@Service
@Scope("singleton")
public class NsiHolder {

    public Dor findDor(Character kodd, String kodg) {
        return dorMap.get(kodd + kodg);
    }

    public Stanv findStanv(String stan, Date date) {
        if(date != null && stanvMap.containsKey(stan))
            for(Stanv stanv : stanvMap.get(stan))
                if (stanv != null && date.compareTo(stanv.getDatand()) >= 0 && date.compareTo(stanv.getDatakd()) <= 0)
                    return stanv;

        return null;
    }

    public Site findSite(String idsite, String gos, Date date) {
        if (date != null && siteMap.containsKey(idsite + gos))
            for(Site site : siteMap.get(idsite + gos))
                if (site != null && date.compareTo(site.getDatan()) >= 0 && date.compareTo(site.getDatak()) <= 0)
                    return site;

        return null;
    }

    public Plagn findPlagn(String idplagn, String gos, Date date) {
        if(date != null && plagnMap.containsKey(idplagn + gos))
            for(Plagn plagn : plagnMap.get(idplagn + gos))
                if (plagn != null && date.compareTo(plagn.getDatan()) >= 0 && date.compareTo(plagn.getDatak()) <= 0)
                    return plagn;

        return null;
    }

    public Sublx findSublx(String lg, Date date) {
        if(date != null && sublxMap.containsKey(lg))
            for(Sublx sublx : sublxMap.get(lg))
                if (sublx != null && date.compareTo(sublx.getDatan()) >= 0 && date.compareTo(sublx.getDatak()) <= 0)
                    return sublx;

        return null;
    }

    public Sf findSf(Integer vid, Date date) {
        if(date != null && sfMap.containsKey(vid))
            for(Sf sf : sfMap.get(vid))
                if (sf != null && date.compareTo(sf.getDatan()) >= 0 && date.compareTo(sf.getDatak()) <= 0)
                    return sf;

        return null;
    }

    // Key is used for codes. Multiple codes will be concatenated
    private Map<String, Dor>    dorMap           = new HashMap<>();
    // List is used to hold range of days as indices to provide quick access by date in the range
    private Map<String,  List<Stanv>> stanvMap = new HashMap<>();
    private Map<String,  List<Site>>  siteMap  = new HashMap<>();
    private Map<String,  List<Plagn>> plagnMap = new HashMap<>();
    private Map<String,  List<Sublx>> sublxMap = new HashMap<>();
    private Map<Integer, List<Sf>>    sfMap    = new HashMap<>();

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

        List<Dor>   dorList   = dorRepo  .findAll();
        List<Stanv> stanvList = stanvRepo.findAllByOrderByDatandDesc();
        List<Site>  siteList  = siteRepo .findAllByOrderByDatanDesc();
        List<Plagn> plagnList = plagnRepo.findAllByOrderByDatanDesc();
        List<Sublx> sublxList = sublxRepo.findAllByOrderByDatanDesc();
        List<Sf>    sfList    = sfRepo   .findAllByOrderByDatanDesc();

        System.out.println("Получены списки справочников. Заполняю память...");

        dorList  .forEach(dor   -> dorMap  .put(dor.getKodd() + dor.getKodg(),       dor));
        System.out.println("Загружено " + dorMap.size() + " множеств dor");
        stanvList.forEach(stanv -> {
            List<Stanv> list = Optional.ofNullable(stanvMap.get(stanv.getStan())).orElse(new ArrayList<>());
            list.add(stanv);
            stanvMap.put(stanv.getStan(), list);
        });
        System.out.println("Загружено " + stanvMap.size() + " множеств stanv");
        siteList.forEach(site  -> {
            String key = site.getIdsite() + site.getGos();
            List<Site> list = Optional.ofNullable(siteMap.get(site.getIdsite() + site.getGos())).orElse(new ArrayList<>());
            list.add(site);
            siteMap.put(key, list);
        });
        System.out.println("Загружено " + siteMap.size() + " множеств site");
        plagnList.forEach(plagn -> {
            String key = plagn.getIdplagn() + plagn.getGos();
            List<Plagn> list = Optional.ofNullable(plagnMap.get(plagn.getIdplagn() + plagn.getGos())).orElse(new ArrayList<>());
            list.add(plagn);
            plagnMap.put(key, list);
        });
        System.out.println("Загружено " + plagnMap.size() + " множеств plagn");
        sublxList.forEach(sublx -> {
            List<Sublx> list = Optional.ofNullable(sublxMap.get(sublx.getLg())).orElse(new ArrayList<>());
            list.add(sublx);
            sublxMap.put(sublx.getLg(), list);
        });
        System.out.println("Загружено " + sublxMap.size() + " множеств sublx");
        sfList   .forEach(sf    -> {
            List<Sf> list = Optional.ofNullable(sfMap.get(sf.getVid())).orElse(new ArrayList<>());
            list.add(sf);
            sfMap.put(sf.getVid(), list);
        });

        System.out.println("Загрузка справочников завершена. Время загрузки: " + (new java.util.Date().getTime() - startDate.getTime()) + " мс.");
    }
}
