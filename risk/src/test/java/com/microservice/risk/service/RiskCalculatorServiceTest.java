package com.microservice.risk.service;

import com.microservice.risk.client.NoteClient;
import com.microservice.risk.client.PatientClient;
import com.project.common.dto.NoteResponseDTO;
import com.project.common.dto.PatientDTO;
import com.project.common.model.Gender;
import com.project.common.model.LevelRiskOfDiabetes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RiskCalculatorServiceTest {

    private PatientClient patientClient;
    private NoteClient noteClient;
    private RiskCalculatorService riskCalculatorService;

    @BeforeEach
    void setUp() {
        patientClient = Mockito.mock(PatientClient.class);
        noteClient = Mockito.mock(NoteClient.class);
        riskCalculatorService = new RiskCalculatorServiceImpl(patientClient, noteClient);
    }

    // --------------------------------------------------------
    // CAS 1 : PATIENT NONE
    // --------------------------------------------------------
    @Test
    void calculateDiabetesRisk_ShouldReturnNone_ForPatientTestNone() {
        PatientDTO patient = new PatientDTO();
        patient.setId(1L);
        patient.setFirstName("Test");
        patient.setLastName("TestNone");
        patient.setDateOfBirth(LocalDate.of(1966, 12, 31));
        patient.setGender(Gender.F);

        List<NoteResponseDTO> notes = List.of(
                new NoteResponseDTO(
                        "Le patient déclare qu'il se sent très bien. Poids égal ou inférieur au poids recommandé.",
                        LocalDate.of(2023, 1, 1))
        );

        when(patientClient.getPatientById(1L)).thenReturn(patient);
        when(noteClient.getNoteAndDateByPatientId(1L)).thenReturn(notes);

        PatientDTO result = riskCalculatorService.calculateDiabeteForOnePatient(1L);

        assertEquals(LevelRiskOfDiabetes.None, result.getRiskOfDiabetes());
    }

    // --------------------------------------------------------
    // CAS 2 : PATIENT BORDERLINE
    // --------------------------------------------------------
    @Test
    void calculateDiabetesRisk_ShouldReturnBorderline_ForPatientTestBorderline() {
        PatientDTO patient = new PatientDTO();
        patient.setId(2L);
        patient.setFirstName("Test");
        patient.setLastName("TestBorderline");
        patient.setDateOfBirth(LocalDate.of(1945, 6, 24));
        patient.setGender(Gender.M);

        List<NoteResponseDTO> notes = List.of(
                new NoteResponseDTO(
                        "Le patient déclare qu'il ressent beaucoup de stress au travail. Il se plaint également que son audition est anormale dernièrement.",
                        LocalDate.of(2023, 1, 1)),
                new NoteResponseDTO(
                        "Le patient déclare avoir fait une réaction aux médicaments au cours des 3 derniers mois. Il remarque également que son audition continue d'être anormale.",
                        LocalDate.of(2023, 2, 1))
        );

        when(patientClient.getPatientById(2L)).thenReturn(patient);
        when(noteClient.getNoteAndDateByPatientId(2L)).thenReturn(notes);

        PatientDTO result = riskCalculatorService.calculateDiabeteForOnePatient(2L);

        assertEquals(LevelRiskOfDiabetes.Borderline, result.getRiskOfDiabetes());
    }

    // --------------------------------------------------------
    // CAS 3 : PATIENT IN DANGER
    // --------------------------------------------------------
    @Test
    void calculateDiabetesRisk_ShouldReturnInDanger_ForPatientTestInDanger() {
        PatientDTO patient = new PatientDTO();
        patient.setId(3L);
        patient.setFirstName("Test");
        patient.setLastName("TestInDanger");
        patient.setDateOfBirth(LocalDate.of(2004, 6, 18));
        patient.setGender(Gender.M);

        List<NoteResponseDTO> notes = List.of(
                new NoteResponseDTO(
                        "Le patient déclare qu'il fume depuis peu.",
                        LocalDate.of(2023, 1, 1)),
                new NoteResponseDTO(
                        "Le patient déclare qu'il est fumeur et qu'il a cessé de fumer l'année dernière. Il se plaint également de crises d’apnée respiratoire anormales. Tests de laboratoire indiquant un taux de cholestérol LDL élevé.",
                        LocalDate.of(2023, 2, 1))
        );

        when(patientClient.getPatientById(3L)).thenReturn(patient);
        when(noteClient.getNoteAndDateByPatientId(3L)).thenReturn(notes);

        PatientDTO result = riskCalculatorService.calculateDiabeteForOnePatient(3L);

        assertEquals(LevelRiskOfDiabetes.InDanger, result.getRiskOfDiabetes());
    }

    // --------------------------------------------------------
    // CAS 4 : PATIENT EARLY ONSET
    // --------------------------------------------------------
    @Test
    void calculateDiabetesRisk_ShouldReturnEarlyOnset_ForPatientTestEarlyOnset() {
        PatientDTO patient = new PatientDTO();
        patient.setId(4L);
        patient.setFirstName("Test");
        patient.setLastName("TestEarlyOnset");
        patient.setDateOfBirth(LocalDate.of(2002, 6, 28));
        patient.setGender(Gender.F);

        List<NoteResponseDTO> notes = List.of(
                new NoteResponseDTO(
                        "Le patient déclare qu'il lui est devenu difficile de monter les escaliers. Il se plaint également d’être essoufflé. Tests de laboratoire indiquant que les anticorps sont élevés. Réaction aux médicaments.",
                        LocalDate.of(2023, 1, 1)),
                new NoteResponseDTO(
                        "Le patient déclare qu'il a mal au dos lorsqu'il reste assis pendant longtemps.",
                        LocalDate.of(2023, 2, 1)),
                new NoteResponseDTO(
                        "Le patient déclare avoir commencé à fumer depuis peu. Hémoglobine A1C supérieure au niveau recommandé.",
                        LocalDate.of(2023, 3, 1)),
                new NoteResponseDTO(
                        "Taille, Poids, Cholestérol, Vertige et Réaction.",
                        LocalDate.of(2023, 4, 1))
        );

        when(patientClient.getPatientById(4L)).thenReturn(patient);
        when(noteClient.getNoteAndDateByPatientId(4L)).thenReturn(notes);

        PatientDTO result = riskCalculatorService.calculateDiabeteForOnePatient(4L);

        assertEquals(LevelRiskOfDiabetes.EarlyOnSet, result.getRiskOfDiabetes());
    }
}
