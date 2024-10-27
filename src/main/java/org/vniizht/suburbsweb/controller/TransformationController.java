package org.vniizht.suburbsweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.vniizht.suburbsweb.model.TransformationOptions;
import org.vniizht.suburbsweb.service.Transformation;

@Controller
@ResponseBody
public class TransformationController {

    @Autowired
    private Transformation transformation;

    @PostMapping("/transform")
    public String runTransformation(@RequestBody TransformationOptions options) throws Exception {
        return transformation.transform(options);
    }
}