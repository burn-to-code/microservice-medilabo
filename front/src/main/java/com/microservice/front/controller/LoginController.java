package com.microservice.front.controller;

import com.microservice.front.client.PatientClientInterface;
import com.microservice.front.config.security.AuthSession;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class LoginController {

    private final AuthSession authSession;
    private final PatientClientInterface patientClientInterface;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String tryLogin(@RequestParam String username, @RequestParam String password, Model model){
        authSession.login(username, password);
        try {
            patientClientInterface.getAllPatients();
            return "redirect:/patient";
        } catch (FeignException.Unauthorized e) {
            authSession.logout();
            model.addAttribute("error", "erreur : login ou mot de passe incorrect");
            return "login";
        }
    }
}
