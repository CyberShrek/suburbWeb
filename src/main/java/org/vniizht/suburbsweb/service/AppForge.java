package org.vniizht.suburbsweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.vniizht.suburbsweb.util.ResourcesAccess;

import java.io.IOException;

@Service
public class AppForge {

    @Autowired
    private RestTemplate jsonRest;

    private static final String forgeUrl = "http://localhost:8080/appforge/";

    public String getView() throws IOException {
        try {
            return jsonRest.postForEntity(
                    forgeUrl,
                    ResourcesAccess.getJsonConfig("view.json"),
                    String.class).getBody();
        }
        catch (HttpClientErrorException exception){
            return "AppForge: " + exception.getMessage();
        }
    }
}
