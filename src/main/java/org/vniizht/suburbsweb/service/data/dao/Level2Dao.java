package org.vniizht.suburbsweb.service.data.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.service.data.entities.level2.*;
import org.vniizht.suburbsweb.service.data.repository.level2.*;

import java.util.*;

@Service
public class Level2Dao {

    @Autowired private PrigMainRepo prigMainRepo;
    @Autowired private PassMainRepo passMainRepo;

    public List<Long> findPrigIdnumsByRequestDate(Date requestDate) {
        return prigMainRepo.findIdnumByRequestDate(requestDate);
    }

    public List<Long> findSpecialIdnums() {
        return prigMainRepo.findSpecialIdnums();
    }

    public List<Long> findPassIdnumsByRequestDate(Date requestDate) {
        return passMainRepo.findIdnumByRequestDate(requestDate);
    }

    public Set<Record> findPrigRecordsByIdnums(Date requestDate, List<Long> ids) {
        Map<Long, PrigRecord> collector = new LinkedHashMap<>();
        List<PrigMain> mainList = prigMainRepo.findAllByRequestDateAndIdnumIn(requestDate, ids);
        mainList.forEach(main -> collector.put(main.idnum, new PrigRecord(main)));
        return new LinkedHashSet<>(collector.values());
    }

    public Set<Record> findPrigRecordsByIdnums(List<Long> ids) {
        Map<Long, PrigRecord> collector = new LinkedHashMap<>();
        List<PrigMain> mainList = prigMainRepo.findAllByIdnumIn(ids);
        mainList.forEach(main -> collector.put(main.idnum, new PrigRecord(main)));
        return new LinkedHashSet<>(collector.values());
    }

    public Set<Record> findPassRecordsByIdnums(Date requestDate, List<Long> ids) {
        Map<Long, PassRecord> collector = new LinkedHashMap<>();
        List<PassMain> mainList = passMainRepo.findAllByRequestDateAndIdnumIn(requestDate, ids);
        mainList.forEach(main -> collector.put(main.idnum, new PassRecord(main)));
        return new LinkedHashSet<>(collector.values());
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
