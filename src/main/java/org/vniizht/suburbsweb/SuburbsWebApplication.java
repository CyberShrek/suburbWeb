package org.vniizht.suburbsweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.vniizht.suburbsweb.model.TransformationOptions;
import org.vniizht.suburbsweb.service.Transformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class SuburbsWebApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(SuburbsWebApplication.class);

        // Активируем профиль "console", если запущено из JAR
        if (isRunningFromJar()) {
            app.setAdditionalProfiles("console");
        }

        ConfigurableApplicationContext context = app.run(args);

        // Если запущено из JAR, выполняем базовую логику и завершаем
        if (isRunningFromJar()) {
            System.out.println("Запуск в консольном режиме...");
            executeConsoleLogic(context);
            System.out.println("Завершение...");
            SpringApplication.exit(context, () -> 0);
        } else {
            System.out.println("Запуск в веб-режиме...");
        }
    }

    private static boolean isRunningFromJar() {
        String classPath = System.getProperty("java.class.path");
        return classPath.contains(".jar");
    }

    private static void executeConsoleLogic(ConfigurableApplicationContext context) throws Exception {
        // Запуск функции бина
        Transformation transformation = context.getBean(Transformation.class);
        transformation.transform();
    }
}