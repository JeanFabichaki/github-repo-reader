package com.trustly.challenger.githubreporeader.config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trustly.challenger.githubreporeader.exception.NotFoundException;

@TestInstance(Lifecycle.PER_CLASS)
class RestWebExceptionHandlerTest {

    private RestWebExceptionHandler handler;
    private ServerWebExchange serverWebExchange;
    private ServerHttpResponse serverHttpResponse;
    private HttpHeaders httpHeaders;

    @BeforeAll
    void setUp() {
        serverWebExchange = mock(ServerWebExchange.class);
        serverHttpResponse = mock(ServerHttpResponse.class);
        httpHeaders = mock(HttpHeaders.class);
        handler = new RestWebExceptionHandler(new ObjectMapper());
    }

    @Test
    void shouldReturnCustomErrorWhenThrowNotFoundException() {

        when(serverWebExchange.getResponse()).thenReturn(serverHttpResponse);
        when(serverHttpResponse.getHeaders()).thenReturn(httpHeaders);

        handler.handle(serverWebExchange, new NotFoundException());
        verify(serverHttpResponse, times(1)).setStatusCode(HttpStatus.NOT_FOUND);
    }

}
