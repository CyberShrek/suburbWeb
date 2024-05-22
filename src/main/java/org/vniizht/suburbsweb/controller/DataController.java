package org.vniizht.suburbsweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Adi;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Cost;
import org.vniizht.suburbsweb.model.transformation.level2.tables.Main;
import org.vniizht.suburbsweb.service.transformation.data.AdiRepository;
import org.vniizht.suburbsweb.service.transformation.data.CostRepository;
import org.vniizht.suburbsweb.service.transformation.data.MainRepository;

import java.util.List;

@RestController
public class DataController {

    @Autowired private AdiRepository adiRepo;
    @Autowired private CostRepository costRepo;
    @Autowired private MainRepository mainRepo;

    @GetMapping("/table/main")
    public List<Main> getMainTable() {
        return mainRepo.findAll();
    }

    @GetMapping("/table/cost/{idnum}")
    public List<Cost> findAllCostsByIdnum(@PathVariable Long idnum) {
        return costRepo.findAllByIdnum(idnum);
    }

    @GetMapping("/table/adi/{idnum}")
    public Adi getAdiByIdnum(@PathVariable Long idnum) {
        return adiRepo.findById(idnum).orElse(null);
    }
}
