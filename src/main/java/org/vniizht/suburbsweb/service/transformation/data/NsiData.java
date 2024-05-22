package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.nsi.Plagn;
import org.vniizht.suburbsweb.model.transformation.nsi.Site;
import org.vniizht.suburbsweb.model.transformation.nsi.Stanv;

import java.util.Date;
import java.util.Optional;

@Service
public class NsiData {

    @Autowired private DorRepository    dorRepo;
    @Autowired private StanvRepository  stanvRepo;
    @Autowired private SiteRepository   siteRepo;
    @Autowired private PlagnRepository  plagnRepo;

    public String getRoad(String stationCode, Date date) {
        Stanv stanv = findStanv(stationCode, date);
        return dorRepo
                .findFirstByKoddAndKodg(stanv.getDor(), stanv.getGos())
                .getNomd3();
    }

    public String getDepartment(String stationCode, Date date) {
        return findStanv(stationCode, date).getOtd();
    }

    public String getRegion(String stationCode, Date date) {
        return Optional.ofNullable(findStanv(stationCode, date).getSf()).orElse("00");
    }

    public String getOkato(String stationCode, Date date) {
        return Optional.ofNullable(findStanv(stationCode, date).getKodokato()).orElse("00000");
    }

    public String getArea(String stationCode, Date date) {
        return findStanv(stationCode, date).getNopr(); // ??
    }

    public String getTSite(String siteId, String countryCode, Date date){
        Site site = findSite(siteId, countryCode, date);
        return site == null ? "  " : site.getTsite();
    }

    public String getPlagnVr(String plagnId, String countryCode, Date date){
        Plagn plagn = findPlagn(plagnId, countryCode, date);
        return plagn == null ? "  " : plagn.getVr();
    }

    private Stanv findStanv(String stationCode, Date date){
        return stanvRepo
                .findFirstByStanAndDataniLessThanEqualAndDatakdGreaterThanEqual(stationCode, date, date);
    }

    private Site findSite(String siteId, String countryCode, Date date){
        return siteRepo
                .getFirstByIdsiteAndGosAndDatanLessThanEqualAndDatakGreaterThanEqual(siteId, countryCode, date, date);
    }

    private Plagn findPlagn(String plagnId, String countryCode, Date date){
        return plagnRepo
                .getFirstByIdplagnAndGosAndDatanLessThanEqualAndDatakGreaterThanEqual(plagnId, countryCode, date, date);
    }
}
