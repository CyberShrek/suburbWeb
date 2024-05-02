package org.vniizht.suburbsweb.service;

import org.springframework.stereotype.Service;

@Service
public class Logger {

    public void log(String... content) {
        System.out.println(String.join("\t", content));
    }
}
