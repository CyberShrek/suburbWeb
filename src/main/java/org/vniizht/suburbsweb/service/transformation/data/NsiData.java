package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.reference.*;

import java.util.Date;
import java.util.Optional;

@Service
public class NsiData {

    @Autowired private NsiHolder holder;

    public String getRoad(String stationCode, Date date) {
        Stanv stanv = holder.findStanv(stationCode, date);
        return holder.findDor(stanv.getDor(), stanv.getGos())
                .getNomd3();
    }

    public String getRegion(String stationCode, Date date) {
        return Optional.ofNullable(holder.findStanv(stationCode, date).getSf()).orElse("00");
    }

    public String getOkato(String stationCode, Date date) {
        Sf sf = holder.findSf(Integer.valueOf(getRegion(stationCode, date)), date);
        if (sf == null) {
            return "00000";
        }
        return Optional.ofNullable(
                sf.getOkato()
                ).orElse("00000");
    }

    public String getArea(String stationCode, Date date) {
        return holder.findStanv(stationCode, date).getNopr(); // ??
    }

    public String getTSite(String siteId, String countryCode, Date date){
        Site site = holder.findSite(siteId, countryCode, date);
        return site == null ? "  " : site.getTsite();
    }

    public String getPlagnVr(String plagnId, String countryCode, Date date){
        Plagn plagn = holder.findPlagn(plagnId, countryCode, date);
        return plagn == null ? "  " : plagn.getVr();
    }

    public String getGvc(String benefitGroupCode, String benefitCode, Date date){
        Sublx sublx = holder.findSublx(benefitGroupCode + benefitCode, date);
        if (sublx == null || sublx.getGvc() == null) {
            return null;
        }
        return String.valueOf(sublx.getGvc());
    }
}
