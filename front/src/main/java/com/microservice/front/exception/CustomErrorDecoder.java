package com.microservice.front.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String invoqueur, Response response) {
        String message = "Unknown error";
        if(response.body() != null) {
            try {
                JsonNode node = objectMapper.readTree(response.body().asInputStream());
                if(node.has("error")) {
                    message = node.get("error").asText();
                }
            } catch (IOException ignored) {
            }
        }

        return switch (response.status()) {
            case 404 -> new NotFoundException(message);
            case 409 -> new ConflictException(message);
            default -> defaultErrorDecoder.decode(invoqueur, response);
        };
    }
}
