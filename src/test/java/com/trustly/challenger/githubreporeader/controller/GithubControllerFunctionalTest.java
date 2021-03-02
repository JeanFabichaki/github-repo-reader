package com.trustly.challenger.githubreporeader.controller;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import com.trustly.challenger.githubreporeader.presentation.controller.representation.ExtensionRepresentation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT)
class GithubControllerFunctionalTest {

    @LocalServerPort
    private Integer port;

    @Test
    void shouldReturnValidValues() {

        ExtractableResponse<Response> response = RestAssured
            .given()
            .port(port)
            .when()
                .get("/analysis/user/JeanFabichaki/repo/todo-rust")
            .then()
            .statusCode(200)
            .extract();

        final var listExtensions = response.body().as(ExtensionRepresentation[].class);
        Assertions.assertEquals(5, listExtensions.length);

        final var jsonExtension = Arrays.stream(listExtensions)
            .filter(extensionRepresentation -> extensionRepresentation.getExtension().equals("json"))
            .findFirst();

        Assertions.assertEquals(26, jsonExtension.get().getTotalLines());
        Assertions.assertEquals(394, jsonExtension.get().getTotalBytes());
    }

}
