package com.trustly.challenger.githubreporeader.config;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trustly.challenger.githubreporeader.exception.NotFoundException;
import com.trustly.challenger.githubreporeader.presentation.controller.representation.ErrorRepresentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
@RequiredArgsConstructor
class RestWebExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof NotFoundException) {

            exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            final var errorRepresentation = ErrorRepresentation.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .title(HttpStatus.NOT_FOUND.getReasonPhrase())
                .details("Repository not found or is private")
                .build();

            try {
                var dataBufferError = new DefaultDataBufferFactory()
                    .wrap(objectMapper.writeValueAsBytes(errorRepresentation));
                return exchange.getResponse().writeWith(Mono.just(dataBufferError));
            } catch (JsonProcessingException e) {
                log.error("Error create ErrorRepresentation", e);
                return Mono.empty();
            }
        }
        return Mono.error(ex);
    }
}
