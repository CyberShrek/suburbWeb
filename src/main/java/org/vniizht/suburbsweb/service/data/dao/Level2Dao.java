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
    @Autowired private PassMainRepo passMainRepo;

    public Set<PrigRecord> findPrigRecords(Date requestDate) {
        Map<Long, PrigRecord> collector = new LinkedHashMap<>();
        List<PrigMain> mainList = prigMainRepo.findAllByRequestDate(requestDate);
        mainList.forEach(main -> collector.put(main.idnum, new PrigRecord(main)));
        return new LinkedHashSet<>(collector.values());
    }

    public PrigRecord findPrigRecord(Date requestDate) {
        Set<PrigRecord> records = findPrigRecords(requestDate);
        return records.isEmpty() ? null : records.iterator().next();
    }

    public Set<PassRecord> findPassRecords(Date requestDate) {
        Map<Long, PassRecord> collector = new LinkedHashMap<>();
        List<PassMain> mainList = passMainRepo.findAllByRequestDate(requestDate);
        mainList.forEach(passMain -> collector.put(passMain.idnum, new PassRecord(passMain)));
        return new LinkedHashSet<>(collector.values());
    }

    public PassRecord findPassRecord(Date requestDate) {
        Set<PassRecord> records = findPassRecords(requestDate);
        return records.isEmpty() ? null : records.iterator().next();
    }

    @Getter
    @Setter
    static public class PrigRecord extends Record {
        private PrigMain main;
//        private List<PrigCost> cost;

        PrigRecord(PrigMain main) {
            super(main.idnum);
            this.main = main;
//            this.cost = new ArrayList<>();
        }
    }

    @Getter
    @Setter
    static public class PassRecord extends Record {
        private PassMain main;

        PassRecord(PassMain main) {
            super(main.idnum);
            this.main = main;
        }
    }

    @AllArgsConstructor
    static public abstract class Record {
        private Long idnum;
    }
}
