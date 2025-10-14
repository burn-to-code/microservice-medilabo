package com.microservice.front.controller;


import com.microservice.front.config.security.AuthSession;
import com.microservice.front.service.PatientService;
import feign.FeignException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class LoginController {

    private final AuthSession authSession;
    private final PatientService patientService;

    public LoginController(AuthSession authSession, PatientService patientService) {
        this.authSession = authSession;
    	this.patientService = patientService;
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String tryLogin(@RequestParam String username, @RequestParam String password, Model model){
        authSession.login(username, password);
        try {
            patientService.getAllPatients();
            return "redirect:/patient";
        } catch (FeignException.Unauthorized e) {
            authSession.logout();
            model.addAttribute("error", "erreur : login ou mot de passe incorrect");
            return "login";
        }
    }

    @PostMapping("/logout")
    public String logout(){
        authSession.logout();
        return "redirect:/login";
    }
}
