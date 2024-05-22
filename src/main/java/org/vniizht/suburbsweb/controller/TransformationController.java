package org.vniizht.suburbsweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TransformationController {

    @GetMapping("/transformation")
    public String get() {
        return "transformation.html";
    }

    @PostMapping("/transformation")
    public void runTransformation() {

    }
}
