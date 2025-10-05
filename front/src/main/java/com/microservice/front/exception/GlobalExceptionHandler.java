package com.microservice.front.exception;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public void handleNotFoundException(NotFoundException e, HttpServletResponse response) throws Exception{
        log.error("Resource not found: {}", e.getMessage());
        response.sendRedirect("/medilabo/patient?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
    }

    @ExceptionHandler(ConflictException.class)
    public void handleConflictException(ConflictException e, HttpServletResponse response) throws IOException {
        log.error("Conflict: {}", e.getMessage());
        response.sendRedirect("/medilabo/patient?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, Model model) {
        log.error("Unexpected error", e);
        model.addAttribute("error", "Une erreur inattendue s'est produite");
        return "error";
    }
}