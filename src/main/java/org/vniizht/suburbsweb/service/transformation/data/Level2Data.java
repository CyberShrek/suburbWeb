package org.vniizht.suburbsweb.service.transformation.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level2.L2Common;
import org.vniizht.suburbsweb.model.transformation.level2.PrigAdi;
import org.vniizht.suburbsweb.model.transformation.level2.PrigCost;
import org.vniizht.suburbsweb.model.transformation.level2.PrigMain;
import org.vniizht.suburbsweb.service.Logger;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class Level2Data {

    @Autowired private Logger logger;

    @Autowired private CostRepository costRepo;
    @Autowired private MainRepository mainRepo;
    @Autowired private AdiRepository  adiRepo;

    public Map<Long,Record> getRecordsByRequestDate(Date requestDate) {
        Map<Long, Record> result = new LinkedHashMap<>();
        List<PrigMain> prigMainList = mainRepo.findAllByRequestDate(requestDate);
        List<PrigCost> prigCostList = costRepo.findAllByRequestDate(requestDate);
        List<PrigAdi> prigAdiList = adiRepo.findAllByRequestDate(requestDate);

        prigMainList.forEach(prigMain -> result.put(prigMain.getIdnum(), new Record(prigMain, new ArrayList<>(), null)));
        prigCostList.forEach(cost -> {
            Record record = result.get(cost.getIdnum());
            if (record != null)
                record.getPrigCost().add(cost);
        });
        prigAdiList.forEach(prigAdi -> {
            Record record = result.get(prigAdi.getIdnum());
            if (record != null)
                record.setPrigAdi(prigAdi);
        });
        return result;
    }

    @Transactional
    public Map<Long, Record> getRecordsByIdGreaterThan(Long id) {
        Map<Long, Record> result = new LinkedHashMap<>();
        addEntityByIdGreaterThan(id, PrigCost.class, result);
        addEntityByIdGreaterThan(id, PrigMain.class, result);
        return result;
    }

    @Transactional
    public Record getRecordByIdnum(Long idnum) {
        return Record.builder()
                .prigMain(mainRepo.findById(idnum).orElse(null))
                .prigCost(costRepo.findAllByIdnum(idnum))
                .build();
    }

    private void addEntityByIdGreaterThan(Long id,
                                          Class<? extends L2Common> itemClass,
                                          Map<Long, Record> targetMap){
        (itemClass == PrigCost.class ? costRepo : mainRepo)
                .findAllByIdnumGreaterThan(id)
                .forEach(item -> processRecordAdd(itemClass, item, targetMap));
    }

    private void processRecordAdd(Class<? extends L2Common> itemClass,
                                  L2Common recordItem,
                                  Map<Long, Record> targetMap) {
        targetMap.compute(recordItem.getIdnum(), (key, record) -> {
            if      (record == null)          record = new Record(null, null, null);
            else if (itemClass == PrigMain.class) record.setPrigMain((PrigMain) recordItem);
            else if (itemClass == PrigCost.class) {
                 if (record.getPrigCost() == null) record.setPrigCost(new ArrayList<>());
                 record.getPrigCost().add((PrigCost) recordItem);
            }
            return record;
        });
    }

    private void showById(long idnum) {
        costRepo.findById(new PrigCost.Identifier(idnum, (short) 1)).ifPresent(System.out::println);
        mainRepo.findById(idnum).ifPresent(System.out::println);
    }

    @Getter
    @Setter
    @Builder
    static public class Record{
        public PrigMain prigMain;
        public List<PrigCost> prigCost;
        public PrigAdi prigAdi;

        public String toString() {
            return "cost:\t" + prigCost + "\nmain\t" + prigMain;
        }
    }
}
