package com.microservice.front.integration;

import com.microservice.front.service.NoteService;
import com.microservice.front.service.RiskService;
import com.project.common.dto.PatientDTO;
import com.project.common.model.Gender;
import com.project.common.model.LevelRiskOfDiabetes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HistoryControllerIT {

    @MockitoBean
    private RiskService riskService;

    @MockitoBean
    private NoteService noteService;

    @Autowired
    private MockMvc mockMvc;

    private PatientDTO patient;

    @BeforeEach
    void setup() {
        patient = new PatientDTO(
                1L,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                Gender.M,
                "123 Street",
                "0123456789"
        );
        patient.setRiskOfDiabetes(LevelRiskOfDiabetes.None);
    }

    @Test
    void listHistoryWithPatientId_shouldReturnHistoryView() throws Exception {
        Mockito.when(riskService.getPatientWithRiskById(1L)).thenReturn(patient);
        Mockito.when(noteService.getNoteAndDateByPatientId(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/history/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient/history"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attributeExists("notes"))
                .andExpect(model().attributeExists("noteRequest"));
    }

    @Test
    void saveNote_validNote_shouldRedirectToHistory() throws Exception {
        mockMvc.perform(post("/history/save")
                        .param("patientID", "1")
                        .param("patientName", "John")
                        .param("note", "Test note")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/history/1"));
    }

    @Test
    void saveNote_missingPatientInfo_shouldRedirectToPatient() throws Exception {
        mockMvc.perform(post("/history/save")
                        .param("patientID", "")
                        .param("patientName", "")
                        .param("note", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void saveNote_invalidNote_shouldReturnHistoryWithError() throws Exception {
        mockMvc.perform(post("/history/save")
                        .param("patientID", "1")
                        .param("patientName", "Doe")
                        .param("note", "")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/history/1"))
                .andExpect(flash().attribute("error", "Veuillez remplir tous les champs"));

    }

}
