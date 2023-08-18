package org.vniizht.suburbsweb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.vniizht.suburbsweb.util.ResourcesAccess;

import java.io.IOException;

@Service
public class AppForge {

    private final RestTemplate restTemplate;

    private static final String forgeUrl = "http://localhost:8080/appforge/";

    AppForge(RestTemplateBuilder restTemplateBuilder){
        restTemplate = restTemplateBuilder.build();
    }

    public String getView() throws IOException {
        ResponseEntity<String> entity = restTemplate.postForEntity(
                forgeUrl,
                "",
                String.class);

        if(entity.getStatusCode() != HttpStatus.OK) {
            System.err.println("Cannot forge application: " + entity);
        }

        if(entity.getStatusCode() == HttpStatus.TEMPORARY_REDIRECT) {
            System.err.println("New url: " + entity.getHeaders().getFirst("Location"));
        }

        return entity.getBody();
    }
}
