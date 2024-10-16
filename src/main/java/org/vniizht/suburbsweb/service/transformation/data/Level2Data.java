package org.vniizht.suburbsweb.service.transformation.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level2.*;
import org.vniizht.suburbsweb.service.Logger;

import java.util.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class Level2Data {

    @Autowired private Logger logger;

    @Autowired private PrigMainRepository prigMainRepo;
    @Autowired private PrigCostRepository prigCostRepo;
    @Autowired private PrigAdiRepository  prigAdiRepo;
    @Autowired private PassMainRepository passMainRepo;
    @Autowired private PassCostRepository passCostRepo;
    @Autowired private PassExRepository  passExRepo;

    public Map<Long, PrigRecord> findPrigRecords(Date requestDate) {
        Map<Long, PrigRecord> result = new LinkedHashMap<>();
        List<PrigMain> mainList = prigMainRepo.findAllByRequestDate(requestDate);
        List<PrigCost> costList = prigCostRepo.findAllByRequestDate(requestDate);
        List<PrigAdi> adiList = prigAdiRepo.findAllByRequestDate(requestDate);

        mainList.forEach(main -> result.put(main.getIdnum(), new PrigRecord(main, new ArrayList<>(), null)));
        costList.forEach(cost -> {
            PrigRecord record = result.get(cost.getIdnum());
            if (record != null)
                record.getCost().add(cost);
        });
        adiList.forEach(adi -> {
            PrigRecord record = result.get(adi.getIdnum());
            if (record != null)
                record.setAdi(adi);
        });
        return result;
    }

    public Map<Long, PassRecord> findPassRecords(Date requestDate) {
        Map<Long, PassRecord> result = new LinkedHashMap<>();
        List<PassMain> mainList = passMainRepo.findAllByRequestDate(requestDate);
        List<PassCost> costList = passCostRepo.findAllByRequestDate(requestDate);
        List<PassEx> exList     = passExRepo.findAllByRequestDate(requestDate);

        mainList.forEach(passMain -> result.put(passMain.getIdnum(), new PassRecord(passMain, new ArrayList<>(), new ArrayList<>())));
        costList.forEach(cost -> {
            PassRecord record = result.get(cost.getIdnum());
            if (record != null)
                record.getCost().add(cost);
        });
        exList.forEach(ex -> {
            PassRecord record = result.get(ex.getIdnum());
            if (record != null)
                record.getEx().add(ex);
        });
        return result;
    }

    @Getter
    @Setter
    @Builder
    static public class PrigRecord {
        public PrigMain main;
        public List<PrigCost> cost;
        public PrigAdi adi;

        public String toString() {
            return "cost:\t" + cost + "\nmain\t" + main;
        }
    }

    @Getter
    @Setter
    @Builder
    static public class PassRecord {
        public PassMain main;
        public List<PassCost> cost;
        public List<PassEx> ex;

        public String toString() {
            return "cost:\t" + cost + "\nmain\t" + main;
        }
    }
}
