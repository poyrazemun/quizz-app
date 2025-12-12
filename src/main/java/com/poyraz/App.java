package com.poyraz;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@OpenAPIDefinition(info = @Info(title = "Quiz Application API", version = "1.0", description = "API documentation for the Quiz Application", contact = @Contact(name = "Omer Fatih")),
        servers = {@Server(url = "http://localhost:8080", description = "Local server")})
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
