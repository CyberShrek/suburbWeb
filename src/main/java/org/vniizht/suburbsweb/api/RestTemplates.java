package org.vniizht.suburbsweb.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplates {

    @Autowired
    RestTemplateBuilder builder;

    @Bean(name = "jsonRest")
    public RestTemplate getJsonRestTemplateBean(){
        return builder
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
