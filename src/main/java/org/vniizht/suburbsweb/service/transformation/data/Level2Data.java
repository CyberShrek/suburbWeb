package org.vniizht.suburbsweb.service.transformation.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level2.L2Common;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Adi;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Cost;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Main;
import org.vniizht.suburbsweb.service.Logger;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class Level2Data {

    @Autowired private Logger logger;

    @Autowired private AdiRepository adiRepo;
    @Autowired private CostRepository costRepo;
    @Autowired private MainRepository mainRepo;

    @Transactional
    public Map<Long, Record> getRecordsByIdGreaterThan(Long id) {
        Map<Long, Record> result = new LinkedHashMap<>();
        addEntityByIdGreaterThan(id, Adi.class, result);
        addEntityByIdGreaterThan(id, Cost.class, result);
        addEntityByIdGreaterThan(id, Main.class, result);
        return result;
    }

    private void addEntityByIdGreaterThan(Long id,
                                          Class<? extends L2Common> itemClass,
                                          Map<Long, Record> targetMap){
        (itemClass == Adi.class ? adiRepo
                : itemClass == Cost.class ? costRepo
                : mainRepo).findAllByIdnumGreaterThan(id).forEach(
                        item -> processRecordAdd(itemClass, item, targetMap));
    }

    private void processRecordAdd(Class<? extends L2Common> itemClass,
                                  L2Common recordItem,
                                  Map<Long, Record> targetMap) {
        targetMap.compute(recordItem.getIdnum(), (key, record) -> {
            if      (record == null)          record = new Record();
            if      (itemClass == Adi.class)  record.setAdi((Adi) recordItem);
            else if (itemClass == Main.class) record.setMain((Main) recordItem);
            else if (itemClass == Cost.class) {
                 if (record.getCost() == null) record.setCost(new ArrayList<>());
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
        public Main main;
        public List<Cost> cost;

        public String toString() {
            return "adi:\t" + adi + "\ncost:\t" + cost + "\nmain\t" + main;
        }
    }
}
