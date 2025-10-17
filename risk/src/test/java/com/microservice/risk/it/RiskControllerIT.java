package com.microservice.risk.it;

import com.microservice.risk.client.NoteClient;
import com.microservice.risk.client.PatientClient;
import com.project.common.dto.NoteResponseDTO;
import com.project.common.dto.PatientDTO;
import com.project.common.model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RiskControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientClient patientClient;

    @MockitoBean
    private NoteClient noteClient;

    private PatientDTO patientNone, patientBorderline, patientInDanger, patientEarlyOnset;

    @BeforeEach
    void setUp() {
        patientNone = new PatientDTO(1L, "Test", "TestNone", LocalDate.of(1966, 12, 31), Gender.F, "1 Brookside St", "100-222-3333");
        patientBorderline = new PatientDTO(2L, "Test", "TestBorderline", LocalDate.of(1945, 6, 24), Gender.M, "2 High St", "200-333-4444");
        patientInDanger = new PatientDTO(3L, "Test", "TestInDanger", LocalDate.of(2004, 6, 18), Gender.M, "3 Club Road", "300-444-5555");
        patientEarlyOnset = new PatientDTO(4L, "Test", "TestEarlyOnset", LocalDate.of(2002, 6, 28), Gender.F, "4 Valley Dr", "400-555-6666");

    }

    @Test
    void getAllPatients_ShouldCalculateRisk() throws Exception {
        List<PatientDTO> allPatients = List.of(patientNone, patientBorderline, patientInDanger, patientEarlyOnset);

        when(patientClient.getAllPatients()).thenReturn(allPatients);

        // mock des notes par patient
        when(noteClient.getNoteAndDateByPatientId(1L)).thenReturn(
                List.of(new NoteResponseDTO("Le patient se sent très bien. Poids égal ou inférieur au poids recommandé", LocalDate.now()))
        );
        when(noteClient.getNoteAndDateByPatientId(2L)).thenReturn(
                List.of(
                        new NoteResponseDTO("Le patient ressent beaucoup de stress. Audition anormale", LocalDate.now()),
                        new NoteResponseDTO("Réaction aux médicaments. Audition continue d'être anormale", LocalDate.now())
                )
        );
        when(noteClient.getNoteAndDateByPatientId(3L)).thenReturn(
                List.of(
                        new NoteResponseDTO("Le patient déclare qu'il fume depuis peu", LocalDate.now()),
                        new NoteResponseDTO("Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière Il se plaint également de crises d’apnée respiratoire anormales Tests de laboratoire indiquant un taux de cholestérol LDL élevé", LocalDate.now())
                )
        );
        when(noteClient.getNoteAndDateByPatientId(4L)).thenReturn(
                List.of(
                        new NoteResponseDTO("Difficulté à monter les escaliers. Essoufflement. Anticorps élevés. Réaction aux médicaments", LocalDate.now()),
                        new NoteResponseDTO("Mal au dos assis longtemps", LocalDate.now()),
                        new NoteResponseDTO("A commencé à fumer. Hémoglobine A1C élevée", LocalDate.now()),
                        new NoteResponseDTO("Taille, Poids, Cholestérol, Vertige et Réaction", LocalDate.now())
                )
        );

        mockMvc.perform(get("/risk/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].riskOfDiabetes").value("None"))
                .andExpect(jsonPath("$[1].riskOfDiabetes").value("Borderline"))
                .andExpect(jsonPath("$[2].riskOfDiabetes").value("InDanger"))
                .andExpect(jsonPath("$[3].riskOfDiabetes").value("EarlyOnSet"));
    }
}
