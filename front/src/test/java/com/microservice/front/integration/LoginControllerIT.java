package com.microservice.front.integration;


import com.microservice.front.config.PatientServiceMockConfig;
import com.microservice.front.config.security.AuthSession;
import com.microservice.front.service.PatientService;
import feign.FeignException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(PatientServiceMockConfig.class)
class LoginControllerIT {

    @Autowired
    AuthSession authSession;

    @Autowired
    private PatientService patientService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void login_success_shouldRedirectToPatient() throws Exception {
        when(patientService.getAllPatients()).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("username", "admin")
                        .param("password", "password")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(redirectedUrl("/patient"));
    }

    @Test
    void login_fail_shouldReturnLoginWithError() throws Exception {
        when(patientService.getAllPatients()).thenThrow(FeignException.Unauthorized.class);

        mockMvc.perform(post("/login")
                        .param("username", "user")
                        .param("password", "wrongpass")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));

    }

    @Test
    void logout_shouldClearAuthAndRedirect() throws Exception {
        // ✅ Préparer un utilisateur connecté
        authSession.login("admin", "password");

        mockMvc.perform(post("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // todo : FIX THIS SHIT
    @Disabled
    @Test
    void redirectOnLogin_WhenUserIsNotConnected() throws Exception {
        when(patientService.getAllPatients()).thenThrow(FeignException.Unauthorized.class);

        mockMvc.perform(get("/patient"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // TODO : Fix this shit
    @Disabled
    @Test
    void redirectOnLogin_WhenUserIsNotConnectedAndUrlIsNotCorrected() throws Exception {
        when(patientService.getAllPatients()).thenThrow(FeignException.Unauthorized.class);

        mockMvc.perform(get("/juvghvggy"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
