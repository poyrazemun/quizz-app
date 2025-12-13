package com.poyraz.util;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import static io.restassured.RestAssured.given;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CucumberUtil {

    public static Response executeApiCall(int port, String urlEndpoint, HttpMethod httpMethod,
                                          String requestBody) {
        try {
            if (HttpMethod.POST.equals(httpMethod)) {
                return given()
                        .port(port)
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .post(urlEndpoint);
            } else if (HttpMethod.GET.equals(httpMethod)) {
                return given()
                        .port(port)
                        .get(urlEndpoint);
            } else {
                throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute API call: " + e.getMessage(), e);
        }
    }
}
