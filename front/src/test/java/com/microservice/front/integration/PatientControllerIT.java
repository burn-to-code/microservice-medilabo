package com.microservice.front.integration;

import com.microservice.front.config.PatientServiceMockConfig;
import com.microservice.front.exception.ConflictException;
import com.microservice.front.exception.NotFoundException;
import com.microservice.front.service.PatientService;
import com.project.common.model.Gender;
import com.project.common.dto.PatientDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PatientServiceMockConfig.class)
class PatientControllerIT {

    @Autowired
    private PatientService patientService;

    @Autowired
    private MockMvc mockMvc;

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
        Mockito.when(patientService.getAllPatients()).thenReturn(Collections.singletonList(validPatient));

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

    @Test
    void savePatient_invalidPatient_shouldReturnAddViewWithErrors() throws Exception {
        mockMvc.perform(post("/patient/save")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("dateOfBirth", "")
                        .param("gender", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("patient/add"))
                .andExpect(model().attributeHasFieldErrors("patient", "firstName", "lastName", "dateOfBirth", "gender"));
    }

    @Test
    void editPatientForm_shouldReturnEditView() throws Exception {
        Mockito.when(patientService.getPatientById(1L)).thenReturn(validPatient);

        mockMvc.perform(get("/patient/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/edit"))
                .andExpect(model().attributeExists("patient"));
    }

    @Test
    void editPatientForm_notFound_shouldTriggerExceptionHandler() throws Exception {
        Mockito.when(patientService.getPatientById(1L)).thenThrow(new NotFoundException("Patient not found"));

        mockMvc.perform(get("/patient/edit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient?error=Patient+not+found"));
    }

    @Test
    void updatePatient_validPatient_shouldRedirectToList() throws Exception {
        mockMvc.perform(post("/patient/edit/1")
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

    @Test
    void updatePatient_invalidPatient_shouldReturnEditViewWithErrors() throws Exception {
        mockMvc.perform(post("/patient/edit/1")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("dateOfBirth", "")
                        .param("gender", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().isOk())
                .andExpect(view().name("patient/edit"))
                .andExpect(model().attributeHasFieldErrors("patient", "firstName", "lastName", "dateOfBirth", "gender"));
    }

    @Test
    void updatePatient_conflict_shouldTriggerExceptionHandler() throws Exception {
        doThrow(new ConflictException("Conflict")).when(patientService).updatePatient(eq(1L), any(PatientDTO.class));

        mockMvc.perform(post("/patient/edit/1")
                        .param("firstName", validPatient.getFirstName())
                        .param("lastName", validPatient.getLastName())
                        .param("dateOfBirth", "01/01/1990")
                        .param("gender", validPatient.getGender().name())
                        .param("address", validPatient.getAddress())
                        .param("phoneNumber", validPatient.getPhoneNumber())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient?error=Conflict"));
    }
}
