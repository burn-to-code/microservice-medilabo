package com.microservice.front.config;

import com.microservice.front.config.security.AuthSession;
import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicAuthClientConfig {

    private final AuthSession authSession;

    public BasicAuthClientConfig(AuthSession authSession) {
        this.authSession = authSession;
    }

    @Bean
    public RequestInterceptor dynamicBasicAuthInterceptor() {
        return requestTemplate -> {
            String authHeader = authSession.getAuthHeader();
            if (authHeader != null) {
                requestTemplate.header("Authorization", authHeader);
            }
        };
    }

    @Bean
    public Logger.Level basicAuthLoggerLevel() {return Logger.Level.FULL;}
}
