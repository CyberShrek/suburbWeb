package org.vniizht.suburbsweb;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!war") // Запускается только в НЕ-WAR режиме
public class ConsoleRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext context;

    public ConsoleRunner(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (args.containsOption("task")) {
            executeTask(args);
            SpringApplication.exit(context, () -> 0); // Корректное завершение
        }
    }

    private void executeTask(ApplicationArguments args) {
        // Ваша логика здесь
        System.out.println("Выполняю задачу с аргументами: " + args.getOptionValues("task"));
    }
}
