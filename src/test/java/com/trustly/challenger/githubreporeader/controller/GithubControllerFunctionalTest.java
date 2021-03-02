package com.trustly.challenger.githubreporeader.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.trustly.challenger.githubreporeader.presentation.controller.representation.ErrorRepresentation;
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
    void shouldReturn200WithValidRepo() {

        ExtractableResponse<Response> response = RestAssured
            .given()
            .port(port)
            .when()
                .get("/api/user/JeanFabichaki/repo/todo-rust")
            .then()
            .statusCode(200)
            .extract();

        final var listExtensions = response.body().as(ExtensionRepresentation[].class);
        assertEquals(5, listExtensions.length);

        final var jsonExtension = Arrays.stream(listExtensions)
            .filter(extensionRepresentation -> extensionRepresentation.getExtension().equals("json"))
            .findFirst();

        assertEquals(26, jsonExtension.get().getTotalLines());
        assertEquals(394, jsonExtension.get().getTotalBytes());
    }

    @Test
    void shouldReturn404WithInvalidRepo() {

        ExtractableResponse<Response> response = RestAssured
            .given()
            .port(port)
            .when()
            .get("/api/user/JeanFabichaki/repo/notfound")
            .then()
            .statusCode(404)
            .extract();

        final var reponseError = response.body().as(ErrorRepresentation.class);
        Assertions.assertNotNull(reponseError);

        assertEquals(404, reponseError.getStatus());
        assertEquals(HttpStatus.NOT_FOUND.getReasonPhrase(), reponseError.getTitle());
    }

}
