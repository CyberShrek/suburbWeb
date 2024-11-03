package org.vniizht.suburbsweb.service.data.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.data.entities.level2.*;
import org.vniizht.suburbsweb.service.data.repository.level2.*;

import java.util.*;

@Service
public class Level2Dao {

    @Autowired private PrigMainRepo prigMainRepo;
    @Autowired private PrigCostRepo prigCostRepo;
    @Autowired private PrigAdiRepo prigAdiRepo;
    @Autowired private PassMainRepo passMainRepo;
    @Autowired private PassCostRepo passCostRepo;
    @Autowired private PassExRepo passExRepo;

    public Set<PrigRecord> findPrigRecords(Date requestDate) {
        Map<Long, PrigRecord> collector = new LinkedHashMap<>();
        List<PrigMain> mainList = prigMainRepo.findAllByRequestDate(requestDate);
        List<PrigCost> costList = prigCostRepo.findAllByRequestDate(requestDate);
        List<PrigAdi>   adiList = prigAdiRepo .findAllByRequestDate(requestDate);

        mainList.forEach(main -> collector.put(main.idnum, new PrigRecord(main)));
        costList.forEach(cost -> {
            PrigRecord record = collector.get(cost.idnum);
            if (record != null)
                record.getCost().add(cost);
        });
        adiList.forEach(adi -> {
            PrigRecord record = collector.get(adi.idnum);
            if (record != null)
                record.setAdi(adi);
        });
        return new LinkedHashSet<>(collector.values());
    }

    public Set<PassRecord> findPassRecords(Date requestDate) {
        Map<Long, PassRecord> collector = new LinkedHashMap<>();
        List<PassMain> mainList = passMainRepo.findAllByRequestDate(requestDate);
        List<PassCost> costList = passCostRepo.findAllByRequestDate(requestDate);
        List<PassEx>     exList = passExRepo  .findAllByRequestDate(requestDate);

        mainList.forEach(passMain -> collector.put(passMain.idnum, new PassRecord(passMain)));
        costList.forEach(cost -> {
            PassRecord record = collector.get(cost.idnum);
            if (record != null)
                record.getCost().add(cost);
        });
        exList.forEach(ex -> {
            PassRecord record = collector.get(ex.idnum);
            if (record != null)
                record.getEx().add(ex);
        });
        return new LinkedHashSet<>(collector.values());
    }

    public Date getFirstRequestDate() {
        return prigMainRepo.findByOrderByRequestDateAsc().requestDate;
    }

    @Getter
    @Setter
    static public class PrigRecord extends Record {
        private PrigMain main;
        private List<PrigCost> cost;
        private PrigAdi adi;

        PrigRecord(PrigMain main) {
            super(main.idnum);
            this.main = main;
            this.cost = new ArrayList<>();
        }
    }

    @Getter
    @Setter
    static public class PassRecord extends Record {
        private PassMain main;
        private List<PassCost> cost;
        private List<PassEx> ex;

        PassRecord(PassMain main) {
            super(main.idnum);
            this.main = main;
            this.cost = new ArrayList<>();
            this.ex   = new ArrayList<>();
        }
    }

    @AllArgsConstructor
    static public abstract class Record {
        private Long idnum;
    }
}
