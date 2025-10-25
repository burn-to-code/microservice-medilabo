package com.microservice_patient.patient.it;

import com.microservice_patient.patient.dao.PatientRepository;
import com.project.common.dto.PatientDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PatientControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PatientRepository patientRepository;

    private PatientDTO patientDTO;
    @BeforeEach
    void setUp() {
        patientRepository.deleteAll();

        patientDTO = new PatientDTO();
        patientDTO.setFirstName("John");
        patientDTO.setLastName("Doe");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setGender(com.project.common.model.Gender.M);
        patientDTO.setAddress("123 Street");
        patientDTO.setPhoneNumber("0102030405");

        // Sauvegarde via repository direct (entité Patient)
        var patient = new com.microservice_patient.patient.model.Patient();
        patient.setFirstName(patientDTO.getFirstName());
        patient.setLastName(patientDTO.getLastName());
        patient.setDateOfBirth(patientDTO.getDateOfBirth());
        patient.setGender(com.microservice_patient.patient.model.Gender.M);
        patient.setAddress(patientDTO.getAddress());
        patient.setPhoneNumber(patientDTO.getPhoneNumber());
        patientRepository.save(patient);

        // récupérer l'ID généré pour tests PUT/DELETE
        patientDTO.setId(patient.getId());
    }

    @Test
    void getAllPatients_returnsList() {
        ResponseEntity<PatientDTO[]> response =
                restTemplate.getForEntity("/patients", PatientDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getFirstName()).isEqualTo("John");
    }

    @Test
    void getPatientById_returnsPatient() {
        ResponseEntity<PatientDTO> response =
                restTemplate.getForEntity("/patients/" + patientDTO.getId(), PatientDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getLastName()).isEqualTo("Doe");
    }

    @Test
    void savePatient_createsPatient() {
        PatientDTO newPatientDTO = new PatientDTO();
        newPatientDTO.setFirstName("Jane");
        newPatientDTO.setLastName("Smith");
        newPatientDTO.setDateOfBirth(LocalDate.of(1985, 5, 5));
        newPatientDTO.setGender(com.project.common.model.Gender.F);

        ResponseEntity<PatientDTO> response =
                restTemplate.postForEntity("/patients", newPatientDTO, PatientDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(patientRepository.findAll()).hasSize(2);
    }

    @Test
    void updatePatient_modifiesPatient() {
        PatientDTO updateDTO = new PatientDTO();
        updateDTO.setId(patientDTO.getId());
        updateDTO.setFirstName(patientDTO.getFirstName());
        updateDTO.setLastName("Updated");
        updateDTO.setDateOfBirth(patientDTO.getDateOfBirth());
        updateDTO.setGender(patientDTO.getGender());
        updateDTO.setAddress(patientDTO.getAddress());
        updateDTO.setPhoneNumber(patientDTO.getPhoneNumber());

        HttpEntity<PatientDTO> request = new HttpEntity<>(updateDTO);

        ResponseEntity<PatientDTO> response = restTemplate.exchange(
                "/patients/" + patientDTO.getId(), HttpMethod.PUT, request, PatientDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getLastName()).isEqualTo("Updated");
    }
}