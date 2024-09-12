package org.vniizht.suburbsweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vniizht.suburbsweb.service.transformation.PrigConversion;
import org.vniizht.suburbsweb.service.transformation.Transformation;

@Controller
public class TransformationController {

    @Autowired
    private Transformation transformation;

    @GetMapping("/transformation")
    public String get() {
        return "transformation.html";
    }

    @PostMapping("/transformation")
    public void runTransformation() {

    }

    @GetMapping("/transformation/co22/{idnum}")
    @ResponseBody
    public PrigConversion.Converted getCO22Tables(@PathVariable Long idnum) {
        return transformation.getConvertedByIdnum(idnum);
    }
}
