package org.vniizht.suburbsweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.vniizht.suburbsweb.exception.UserCheckException;
import org.vniizht.suburbsweb.service.UserCheck;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @Autowired
    private UserCheck userCheck;

    @GetMapping
    public String index(HttpServletRequest request) throws Exception {
        userCheck.checkRequest(request);
        return "index.html";
    }
}