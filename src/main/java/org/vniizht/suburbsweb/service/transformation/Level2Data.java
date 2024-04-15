package org.vniizht.suburbsweb.service.transformation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Cost;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Service
public class Level2Data {

    @Autowired private AdiRepo    adiRepo;
    @Autowired private CostRepo   costRepo;
    @Autowired private MainRepo   mainRepo;


    @Transactional
    public void showById(long idnum) {
        adiRepo.findById(idnum).ifPresent(System.out::println);
        costRepo.findById(new Cost.Identifier(idnum, (short) 1)).ifPresent(System.out::println);
        mainRepo.findById(idnum).ifPresent(System.out::println);
    }

    @PostConstruct
    public void test(){
        showById(265208343601L);
    }
}
