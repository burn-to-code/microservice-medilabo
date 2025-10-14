package com.microservice.gateway.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GatewaySecurityTests {

    @Autowired
    private WebTestClient webTestClient;

    @TestConfiguration
    static class TestControllerConfig {
        @RestController
        static class TestController {
            @GetMapping("/test")
            public Mono<String> getPatients() {
                return Mono.just("OK");
            }
        }
    }
    @Test
    void accessingPatientsWithoutLoginShouldReturnUnauthorized() {
        webTestClient.get().uri("/test")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void loginWithValidUserShouldSucceed() {
        String basicAuthHeader = "Basic " + java.util.Base64.getEncoder()
                .encodeToString("admin:password".getBytes());

        webTestClient.get().uri("/test")
                .header(HttpHeaders.AUTHORIZATION, basicAuthHeader)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("OK");
    }

    @Test
    void wrongPasswordShouldFailLogin() {
        String basicAuthHeader = "Basic " + java.util.Base64.getEncoder()
                .encodeToString("admin:wrong".getBytes());

        webTestClient.get().uri("/test")
                .header(HttpHeaders.AUTHORIZATION, basicAuthHeader)
                .exchange()
                .expectStatus().isUnauthorized();
    }
}

