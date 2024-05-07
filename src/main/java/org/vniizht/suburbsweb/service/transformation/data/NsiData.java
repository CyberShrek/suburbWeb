package org.vniizht.suburbsweb.service.transformation.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.nsi.Dor;
import org.vniizht.suburbsweb.model.transformation.nsi.Stanv;

import java.util.Date;

@Service
public class NsiData {

    @Autowired private DorRepository    dorRepo;
    @Autowired private SfRepository     sfRepo;
    @Autowired private StanvRepository  stanvRepo;

    public String getRoadByStationAndDate(String stationCode, Date date, boolean isVc) {
        Stanv stanv = stanvRepo
                .findFirstByStanAndDatandGreaterThanEqualAndDatakdLessThanEqual(stationCode, date, date);

        return (isVc
                ? dorRepo.findFirstByVcAndKodg(stanv.getDor(), stanv.getGos())
                : dorRepo.findFirstByKoddAndKodg(stanv.getDor(), stanv.getGos())
        ).getNomd3();
    }

    public String getRoadByStationAndDate(String stationCode, Date date) {
        return getRoadByStationAndDate(stationCode, date, false);
    }
}
