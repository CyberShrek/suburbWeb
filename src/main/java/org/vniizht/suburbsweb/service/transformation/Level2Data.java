package org.vniizht.suburbsweb.service.transformation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level2.AbstractParent;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Adi;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Cost;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Main;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class Level2Data {

    @Autowired private AdiRepo    adiRepo;
    @Autowired private CostRepo   costRepo;
    @Autowired private MainRepo   mainRepo;

    @Transactional
    public Map<Long, Record> getRecordsByIdGreaterThan(Long id) {
        Map<Long, Record> result = new LinkedHashMap<>();
        processRepoInserts(Adi.class, result);
        processRepoInserts(Cost.class, result);
        processRepoInserts(Main.class, result);
        return result;
    }

    private void processRepoInserts(Class<? extends AbstractParent> itemClass, Map<Long, Record> targetMap){
        (itemClass == Adi.class ? adiRepo
                : itemClass == Cost.class ? costRepo
                : mainRepo).findAll().forEach(
                        item -> processRecordInsert(itemClass, item, targetMap));
    }

    private void processRecordInsert(Class<? extends AbstractParent> itemClass,
                                     AbstractParent recordItem,
                                     Map<Long, Record> targetMap) {
        targetMap.compute(recordItem.getId(), (key, record) -> {
            if(record == null) {
                record = new Record();
            }
            if(itemClass == Adi.class) {
                record.setAdi((Adi) recordItem);
            }
            else if(itemClass == Main.class) {
                record.setMain((Main) recordItem);
            }
            else if(itemClass == Cost.class) {
                if(record.getCost() == null) record.setCost(new ArrayList<>());
                record.getCost().add((Cost) recordItem);
            }
            return record;
        });
    }

    private void showById(long idnum) {
        adiRepo.findById(idnum).ifPresent(System.out::println);
        costRepo.findById(new Cost.Identifier(idnum, (short) 1)).ifPresent(System.out::println);
        mainRepo.findById(idnum).ifPresent(System.out::println);
    }

    @Getter
    @Setter
    static public class Record{
        public Adi adi;
        public List<Cost> cost;
        public Main main;
    }
}
