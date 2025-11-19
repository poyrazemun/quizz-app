package com.poyraz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@Slf4j
@PropertySource("classpath:messages.properties")
@SpringBootApplication
public class App {

    public static void main(String[] args) {
        log.info("Starting the application...");
        SpringApplication.run(App.class, args);
        log.info("Application started!");
    }

}
