package org.vniizht.suburbsweb;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        System.out.println("Running war...");
        application.application().setAdditionalProfiles("war");
        return application.sources(SuburbsWebApplication.class);
    }
}
