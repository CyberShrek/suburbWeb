package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.nsi.Stanv;

import java.util.Date;
import java.util.Optional;

@Service
public class NsiData {

    @Autowired private DorRepository    dorRepo;
    @Autowired private StanvRepository  stanvRepo;

    public String getRoad(String stationCode, Date date, boolean isVc) {
        Stanv stanv = findStanv(stationCode, date);
        return (isVc
                ? dorRepo.findFirstByVcAndKodg(stanv.getDor(), stanv.getGos())
                : dorRepo.findFirstByKoddAndKodg(stanv.getDor(), stanv.getGos())
        ).getNomd3();
    }
    public String getRoad(String stationCode, Date date) {
        return getRoad(stationCode, date, false);
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

    private Stanv findStanv(String stationCode, Date date){
        return stanvRepo
                .findFirstByStanAndDataniGreaterThanEqualAndDatakdLessThanEqual(stationCode, date, date);
    }
}
