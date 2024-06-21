package org.vniizht.suburbsweb.service.transformation.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level2.L2Common;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Cost;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Main;
import org.vniizht.suburbsweb.service.Logger;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class Level2Data {

    @Autowired private Logger logger;

    @Autowired private CostRepository costRepo;
    @Autowired private MainRepository mainRepo;

    public Map<Long,Record> getRecordsByRequestDate(Date requestDate) {
        Map<Long, Record> result = new LinkedHashMap<>();
        List<Main> mainList = mainRepo.findAllByRequestDate(requestDate);
        List<Cost> costList = costRepo.findAllByRequestDate(requestDate);

        mainList.forEach(main -> result.put(main.getIdnum(), new Record(main, new ArrayList<>())));
//        costList.forEach(cost -> {
//            Record record = result.get(cost.getIdnum());
//            if (record != null)
//                record.getCost().add(cost);
//        });
        return result;
    }

    @Transactional
    public Map<Long, Record> getRecordsByIdGreaterThan(Long id) {
        Map<Long, Record> result = new LinkedHashMap<>();
        addEntityByIdGreaterThan(id, Cost.class, result);
        addEntityByIdGreaterThan(id, Main.class, result);
        return result;
    }

    @Transactional
    public Record getRecordByIdnum(Long idnum) {
        return Record.builder()
                .main(mainRepo.findById(idnum).orElse(null))
                .cost(costRepo.findAllByIdnum(idnum))
                .build();
    }

    private void addEntityByIdGreaterThan(Long id,
                                          Class<? extends L2Common> itemClass,
                                          Map<Long, Record> targetMap){
        (itemClass == Cost.class ? costRepo : mainRepo)
                .findAllByIdnumGreaterThan(id)
                .forEach(item -> processRecordAdd(itemClass, item, targetMap));
    }

    private void processRecordAdd(Class<? extends L2Common> itemClass,
                                  L2Common recordItem,
                                  Map<Long, Record> targetMap) {
        targetMap.compute(recordItem.getIdnum(), (key, record) -> {
            if      (record == null)          record = new Record(null, null);
            else if (itemClass == Main.class) record.setMain((Main) recordItem);
            else if (itemClass == Cost.class) {
                 if (record.getCost() == null) record.setCost(new ArrayList<>());
                 record.getCost().add((Cost) recordItem);
            }
            return record;
        });
    }

    private void showById(long idnum) {
        costRepo.findById(new Cost.Identifier(idnum, (short) 1)).ifPresent(System.out::println);
        mainRepo.findById(idnum).ifPresent(System.out::println);
    }

    @Getter
    @Setter
    @Builder
    static public class Record{
        public Main main;
        public List<Cost> cost;

        public String toString() {
            return "cost:\t" + cost + "\nmain\t" + main;
        }
    }
}
