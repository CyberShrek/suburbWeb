package org.vniizht.suburbsweb.service.handbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.data.entities.handbook.*;

import java.util.Date;
import java.util.Optional;

@Service
public class Handbook {

    @Autowired private HandbookCache cache;
    public void loadCache() {cache.load();}
    public void clearCache() {cache.clear();}

    public String getRoad2(String stationCode, Date date) {
        Stanv stanv = cache.findStanv(stationCode, date);
        Dor dor = stanv == null ? null : cache.findDor(stanv.getDor(), stanv.getGos());
        return dor == null ? null : dor.getNom2();
    }

    public String getRoad3(String stationCode, Date date) {
        Stanv stanv = cache.findStanv(stationCode, date);
        Dor dor = stanv == null ? null : cache.findDor(stanv.getDor(), stanv.getGos());
        return dor == null ? null : dor.getNom3();
    }

    public String getRegion(String stationCode, Date date) {
        Stanv stanv = cache.findStanv(stationCode, date);
        return stanv == null ? "00"
                : Optional.ofNullable(stanv.getSf()).orElse("00");
    }

    public String getDepartment(String stationCode, Date date) {
        Stanv stanv = cache.findStanv(stationCode, date);
        return stanv == null ? "000"
                : Optional.ofNullable(stanv.getOtd()).orElse("000").substring(1);
    }

    public String getOkatoByRegion(String regionCode, Date date) {
        if(regionCode == null) return "00000";
        Sf sf = cache.findSf(Integer.valueOf(regionCode), date);
        if (sf == null) {
            return "00000";
        }
        return Optional.ofNullable(
                sf.getOkato()
        ).orElse("00000")
                .substring(0, 5);
    }

    public String getOkatoByStation(String stationCode, Date date) {
        if(stationCode == null) return "00000";
        return getOkatoByRegion(getRegion(stationCode, date), date);
    }

    public String getArea(String stationCode, Date date) {
        Stanv stanv = cache.findStanv(stationCode, date);
        return stanv == null ? null : stanv.getNopr(); // ??
    }

    public String getTSite(String siteId, String countryCode, Date date){
        Site site = cache.findSite(siteId, countryCode, date);
        return site == null ? "  " : site.getTsite();
    }

    public String getPlagnVr(String plagnId, String countryCode, Date date){
        Plagn plagn = cache.findPlagn(plagnId, countryCode, date);
        return plagn == null ? "  " : plagn.getVr();
    }

    public String getGvc(String benefitGroupCode, String benefitCode, Date date){
        Sublx sublx = cache.findSublx(benefitGroupCode + benefitCode, date);
        if (sublx == null || sublx.getGvc() == null) {
            return null;
        }
        return String.valueOf(sublx.getGvc());
    }
}