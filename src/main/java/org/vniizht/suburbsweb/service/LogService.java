package org.vniizht.suburbsweb.service;

import org.springframework.stereotype.Service;

@Service
public class LogService {

    public void log(String message) {
        System.out.println(message);
    }
}
