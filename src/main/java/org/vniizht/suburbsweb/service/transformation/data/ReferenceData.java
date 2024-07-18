package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.reference.*;

import java.util.Date;
import java.util.Optional;

@Service
public class ReferenceData {

    @Autowired private DorRepository    dorRepo;
    @Autowired private StanvRepository  stanvRepo;
    @Autowired private SiteRepository   siteRepo;
    @Autowired private PlagnRepository  plagnRepo;
    @Autowired private SfRepository     sfRepo;
    @Autowired private SublxRepository lgotsRepo;

    public String getRoad(String stationCode, Date date) {
        Stanv stanv = findStanv(stationCode, date);
        return findDor(stanv.getDor(), stanv.getGos())
                .getNomd3();
    }

    public String getRegion(String stationCode, Date date) {
        return Optional.ofNullable(findStanv(stationCode, date).getSf()).orElse("00");
    }

    public String getOkato(String stationCode, Date date) {
        Sf sf = findSf(getRegion(stationCode, date), date);
        if (sf == null) {
            return "00000";
        }
        return Optional.ofNullable(
                sf.getOkato()
                ).orElse("00000");
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

    public String getGvc(String benefitGroupCode, String benefitCode, Date date){
        Sublx sublx = findSublx(benefitGroupCode, benefitCode, date);
        if (sublx == null || sublx.getGvc() == null) {
            return null;
        }
        return String.valueOf(sublx.getGvc());
    }


    /* Accessors */

    private Dor findDor(Character roadCode, String countryCode){
        return dorRepo
                .findFirstByKoddAndKodg(roadCode, countryCode);
    }

    private Stanv findStanv(String stationCode, Date date){
        return stanvRepo
                .findFirstByStanAndDataniLessThanEqualAndDatakdGreaterThanEqual(stationCode, date, date);
    }

    private Site findSite(String siteId, String countryCode, Date date){
        return siteRepo
                .findFirstByIdsiteAndGosAndDatanLessThanEqualAndDatakGreaterThanEqual(siteId, countryCode, date, date);
    }

    private Plagn findPlagn(String plagnId, String countryCode, Date date){
        return plagnRepo
                .findFirstByIdplagnAndGosAndDatanLessThanEqualAndDatakGreaterThanEqual(plagnId, countryCode, date, date);
    }

    private Sf findSf(Integer regionCode, Date date){
        return sfRepo
                .findFirstByVidAndDatanLessThanEqualAndDatakGreaterThanEqual(regionCode, date, date);
    }
    private Sf findSf(String regionCode, Date date){
        return findSf(Integer.parseInt(regionCode), date);
    }

    private Sublx findSublx(String benefitGroupCode, String benefitCode, Date date){
        return lgotsRepo
                .findFirstByLgAndDatanLessThanEqualAndDatakGreaterThanEqual(benefitGroupCode + benefitCode, date, date);
    }
}
