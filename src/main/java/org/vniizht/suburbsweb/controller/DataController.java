package org.vniizht.suburbsweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.vniizht.suburbsweb.service.transformation.data.CostRepository;
import org.vniizht.suburbsweb.service.transformation.data.MainRepository;

@RestController
public class DataController {

    @Autowired private CostRepository costRepo;
    @Autowired private MainRepository mainRepo;

//    @GetMapping("/table/main")
//    public List<Main> getMainTable() {
//        return mainRepo.findAll().stream().sorted(Comparator.comparing(L2Common::getIdnum)).collect(Collectors.toList());
//    }
//
//    @GetMapping("/table/cost/{idnum}")
//    public List<Cost> findAllCostsByIdnum(@PathVariable Long idnum) {
//        return costRepo.findAllByIdnum(idnum);
//    }
}
