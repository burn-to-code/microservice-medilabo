package com.microservice.front.config.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@SessionScope
public class AuthSession {

    private String username;
    private String password;

    public Boolean isAuthenticated() {
        return username != null && password != null;
    }

    public void login(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void logout() {
        this.username = null;
        this.password = null;
    }


    public String getAuthHeader() {
        if (!isAuthenticated()) return null;
        String auth = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }
}
