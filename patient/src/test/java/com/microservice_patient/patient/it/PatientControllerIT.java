package com.microservice_patient.patient.it;

import com.microservice_patient.patient.model.Gender;
import com.microservice_patient.patient.model.Patient;
import com.microservice_patient.patient.dao.PatientRepository;
import org.junit.jupiter.api.Assertions;
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

    private Patient patient;

    @BeforeEach
    void setUp() {
        patientRepository.deleteAll();

        patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setGender(Gender.M);
        patient.setAddress("123 Street");
        patient.setPhoneNumber("0102030405");

        patientRepository.save(patient);
    }

    @Test
    void getAllPatients_returnsList() {
        ResponseEntity<Patient[]> response = restTemplate.getForEntity("/patients", Patient[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getFirstName()).isEqualTo("John");
    }

    @Test
    void getPatientById_returnsPatient() {
        ResponseEntity<Patient> response = restTemplate.getForEntity("/patients/" + patient.getId(), Patient.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getLastName()).isEqualTo("Doe");
    }

    @Test
    void savePatient_createsPatient() {
        Patient newPatient = new Patient();
        newPatient.setFirstName("Jane");
        newPatient.setLastName("Smith");
        newPatient.setDateOfBirth(LocalDate.of(1985, 5, 5));
        newPatient.setGender(Gender.F);

        ResponseEntity<Patient> response = restTemplate.postForEntity("/patients", newPatient, Patient.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(patientRepository.findAll()).hasSize(2);
    }

    @Test
    void updatePatient_modifiesPatient() {
        patient.setLastName("Updated");
        HttpEntity<Patient> request = new HttpEntity<>(patient);

        ResponseEntity<Patient> response = restTemplate.exchange(
                "/patients/" + patient.getId(), HttpMethod.PUT, request, Patient.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().getLastName()).isEqualTo("Updated");
    }

    @Test
    void deletePatient_removesPatient() {
        restTemplate.delete("/patients/" + patient.getId());

        assertThat(patientRepository.findAll()).isEmpty();
    }
}
