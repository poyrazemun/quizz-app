package com.poyraz.annotation;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@OpenAPIDefinition(info = @Info(title = "Quiz Application API", version = "1.0", description = "API documentation for the Quiz Application", contact = @Contact(name = "Omer Fatih")),
        servers = {@Server(url = "http://localhost:8080", description = "Local server")})
public @interface AppDefinition {
}
