package com.microservice.front.integration;

import com.microservice.front.config.PatientServiceMockConfig;
import com.microservice.front.controller.PatientController;
import com.microservice.front.service.PatientService;
import com.project.common.dto.PatientDTO;
import com.project.common.model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@Import(PatientServiceMockConfig.class)
class PatientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientService patientService; // Mock injecté

    private PatientDTO patient;

    @BeforeEach
    void setUp() {
        patient = new PatientDTO(1L, "John", "Doe", LocalDate.of(1990,1,1), Gender.M, "123 rue A", "0102030405");

        when(patientService.getAllPatients()).thenReturn(List.of(patient));
        when(patientService.getPatientById(1L)).thenReturn(patient);
    }

    @Test
    void listPatient_shouldReturnPatientListPage() throws Exception {
        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/list"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("patients", List.of(patient)));
    }

    @Test
    void addPatient_shouldReturnAddPatientPage() throws Exception {
        mockMvc.perform(get("/patient/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/add"))
                .andExpect(model().attributeExists("patient"));
    }

    @Test
    void savePatient_shouldRedirectAfterSuccess() throws Exception {
        mockMvc.perform(post("/patient/save")
                        .param("firstName", "Jane")
                        .param("lastName", "Doe")
                        .param("dateOfBirth", "1995-05-05")
                        .param("gender", "F")
                        .param("address", "456 rue B")
                        .param("phoneNumber", "0607080910"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medilabo/patient"));
    }

    @Test
    void editPatientForm_shouldReturnEditPage() throws Exception {
        mockMvc.perform(get("/patient/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/edit"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attribute("patient", patient));
    }

    @Test
    void updatePatient_shouldRedirectAfterSuccess() throws Exception {
        mockMvc.perform(post("/patient/edit/1")
                        .param("firstName", "JohnUpdated")
                        .param("lastName", "DoeUpdated")
                        .param("dateOfBirth", "1990-01-01")
                        .param("gender", "M")
                        .param("address", "123 rue Updated")
                        .param("phoneNumber", "0102030405"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/medilabo/patient"));
    }
}
