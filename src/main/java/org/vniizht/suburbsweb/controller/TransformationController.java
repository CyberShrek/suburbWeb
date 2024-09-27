package org.vniizht.suburbsweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vniizht.suburbsweb.service.transformation.Transformation;

import java.util.Date;

@Controller
@ResponseBody
public class TransformationController {

    @Autowired
    private Transformation transformation;

//    @GetMapping("/transformation")
//    public String get() {
//        return "transformation.html";
//    }

    @PostMapping("/transform/{requestDate}")
    public String runTransformation(@PathVariable @DateTimeFormat(pattern = "yyyyMMdd") Date requestDate) {
        return transformation.tryPrig(requestDate);
    }

    @PostMapping("/transform")
    public String runTransformation() {
        return transformation.check();
    }

//    @GetMapping("/transformation/co22/{idnum}")
//    @ResponseBody
//    public PrigConversion.Converted getCO22Tables(@PathVariable Long idnum) {
//        return transformation.getConvertedByIdnum(idnum);
//    }
}
