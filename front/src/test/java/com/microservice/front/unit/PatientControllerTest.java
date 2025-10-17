package com.microservice.front.unit;

import com.microservice.front.controller.PatientController;
import com.microservice.front.service.PatientService;
import com.project.common.dto.PatientDTO;
import com.project.common.model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = com.microservice.front.exception.GlobalExceptionHandler.class))
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    private PatientDTO validPatient;

    @BeforeEach
    void setup() {
        validPatient = new PatientDTO(
                1L,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                Gender.M,
                "123 Street",
                "0123456789"
        );
    }

    @Test
    void listPatient_shouldReturnViewWithPatients() throws Exception {
        when(patientService.getAllPatients()).thenReturn(Collections.singletonList(validPatient));

        mockMvc.perform(get("/patient"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/list"))
                .andExpect(model().attributeExists("patients"));
    }

    @Test
    void addPatient_shouldReturnAddView() throws Exception {
        mockMvc.perform(get("/patient/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/add"))
                .andExpect(model().attributeExists("patient"));
    }

    @Test
    void savePatient_validPatient_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/patient/save")
                        .param("firstName", validPatient.getFirstName())
                        .param("lastName", validPatient.getLastName())
                        .param("dateOfBirth", "01/01/1990")
                        .param("gender", validPatient.getGender().name())
                        .param("address", validPatient.getAddress())
                        .param("phoneNumber", validPatient.getPhoneNumber())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient"));
    }
}
