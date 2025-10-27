package com.microservice.risk.it;

import com.microservice.risk.client.NoteClient;
import com.project.common.dto.NoteResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RiskControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @MockitoBean
    private NoteClient noteClient;

    @Test
    void getAllPatients_ShouldCalculateRisk() throws Exception {

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

        String patientsJson = """
                [
                    {"id":1,"firstName":"Test","lastName":"TestNone","dateOfBirth":"31/12/1966","gender":"F","address":"1 Brookside St","phoneNumber":"100-222-3333"},
                    {"id":2,"firstName":"Test","lastName":"TestBorderline","dateOfBirth":"24/06/1945","gender":"M","address":"2 High St","phoneNumber":"200-333-4444"},
                    {"id":3,"firstName":"Test","lastName":"TestInDanger","dateOfBirth":"18/06/2004","gender":"M","address":"3 Club Road","phoneNumber":"300-444-5555"},
                    {"id":4,"firstName":"Test","lastName":"TestEarlyOnset","dateOfBirth":"28/06/2002","gender":"F","address":"4 Valley Dr","phoneNumber":"400-555-6666"}
                ]
                """;

        mockMvc.perform(post("/risk/all")
                        .contentType("application/json")
                        .content(patientsJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andExpect(jsonPath("$[0].riskOfDiabetes").value("None"))
                .andExpect(jsonPath("$[1].riskOfDiabetes").value("Borderline"))
                .andExpect(jsonPath("$[2].riskOfDiabetes").value("InDanger"))
                .andExpect(jsonPath("$[3].riskOfDiabetes").value("EarlyOnSet"));
    }
}
