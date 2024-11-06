package org.vniizht.suburbsweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.vniizht.suburbsweb.model.TransformationOptions;
import org.vniizht.suburbsweb.service.Transformation;

@Controller
@RequestMapping("/transform")
public class TransformationController {

    @Autowired
    private Transformation transformation;

    @GetMapping
    public String getPage() {
        return "transform.html";
    }

    @PostMapping
    @ResponseBody
    public String runTransformation(@RequestBody TransformationOptions options) {
        return transformation.transform(options);
    }
}