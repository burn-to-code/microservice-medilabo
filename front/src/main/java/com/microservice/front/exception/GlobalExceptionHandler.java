package com.microservice.front.exception;

import com.microservice.front.config.security.AuthSession;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private final AuthSession auth;

    public GlobalExceptionHandler(AuthSession auth) {
        this.auth = auth;
    }

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

    @ExceptionHandler(FeignException.Unauthorized.class)
    public String handleUnauthorizedException(FeignException.Unauthorized e, RedirectAttributes model){
        log.error("Unauthorized: {}", e.getMessage());
        if(auth.getAuthHeader() != null) {
            log.info("Session expirée : redirection vers /login");
            model.addFlashAttribute("error", "Une erreur s'est produite : Vous avez été déconnecté");
        } else {
            log.info("Accès non authentifié : redirection vers /login");
            model.addFlashAttribute("error", "Veuillez vous connecter.");
        }
        return "redirect:/login";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, Model model) {
        log.error("Unexpected error", e);
        model.addAttribute("error", "Une erreur inattendue s'est produite");
        return "error";
    }
}