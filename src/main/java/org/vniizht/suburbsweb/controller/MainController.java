package org.vniizht.suburbsweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vniizht.suburbsweb.service.AppForge;

import java.io.IOException;

@RestController
public class MainController {

    @Autowired
    private AppForge appForge;


    @GetMapping
    public String hello() throws IOException {
        return appForge.getView();
    }
}
