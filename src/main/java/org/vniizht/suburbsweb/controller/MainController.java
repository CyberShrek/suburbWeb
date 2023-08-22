package org.vniizht.suburbsweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vniizht.suburbsweb.exception.UserCheckException;
import org.vniizht.suburbsweb.service.AppForge;
import org.vniizht.suburbsweb.service.UserCheck;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class MainController {

    @Autowired
    private AppForge appForge;

    @Autowired
    private UserCheck userCheck;

    @GetMapping
    public String hello(HttpServletRequest request) throws IOException, UserCheckException {
//        userCheck.checkRequest(request);
        return appForge.getView();
    }
}
