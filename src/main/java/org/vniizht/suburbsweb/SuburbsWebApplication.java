package org.vniizht.suburbsweb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.vniizht.suburbsweb.model.TransformationOptions;
import org.vniizht.suburbsweb.service.Transformation;

import java.util.*;

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
            executeConsoleLogic(context, new HashSet<>(Arrays.asList(args)));
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

    private static void executeConsoleLogic(ConfigurableApplicationContext context, Set<String> args) throws Exception {

        Date requestDate = new Date();

        // Поиск даты в формате DDMMYYYYY (8 цифр) и присвоение в requestDate
        String ddMMyyyy = args.stream().filter(arg -> arg.matches("\\d{8}")).findFirst().orElse(null);
        if(ddMMyyyy != null) {
            requestDate.setDate(Integer.parseInt(ddMMyyyy.substring(0, 2)));
            requestDate.setMonth(Integer.parseInt(ddMMyyyy.substring(2, 4)) - 1);
            requestDate.setYear(Integer.parseInt(ddMMyyyy.substring(4, 8)) - 1900);
        } else {
            requestDate.setDate(requestDate.getDate() - 1);
        }


        // Запуск функции бина
        Transformation transformation = context.getBean(Transformation.class);
        transformation.transform(new TransformationOptions(requestDate, args.contains("prig"), args.contains("pass")));
    }
}