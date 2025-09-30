package com.microservice_patient.patient.unit;

import com.microservice_patient.patient.model.Gender;
import com.microservice_patient.patient.model.Patient;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PatientValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validPatient_noViolations() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setGender(Gender.M);
        patient.setAddress("123 Street");
        patient.setPhoneNumber("0102030405");

        Set<ConstraintViolation<Patient>> violations = validator.validate(patient);
        assertThat(violations).isEmpty();
    }

    @Test
    void firstNameBlank_violations() {
        Patient patient = new Patient();
        patient.setFirstName(""); // invalide
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setGender(Gender.M);

        Set<ConstraintViolation<Patient>> violations = validator.validate(patient);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("FirstName Must Not Be Null");
    }

    @Test
    void lastNameBlank_violations() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName(""); // invalide
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setGender(Gender.M);

        Set<ConstraintViolation<Patient>> violations = validator.validate(patient);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("lastName must not be null");
    }

    @Test
    void dateOfBirthNull_violations() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(null); // invalide
        patient.setGender(Gender.M);

        Set<ConstraintViolation<Patient>> violations = validator.validate(patient);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("La date de naissance est obligatoire");
    }

    @Test
    void dateOfBirthFuture_violations() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.now().plusDays(1)); // invalide
        patient.setGender(Gender.M);

        Set<ConstraintViolation<Patient>> violations = validator.validate(patient);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("La date de naissance doit être dans le passé");
    }

    @Test
    void genderNull_violations() {
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setGender(null); // invalide

        Set<ConstraintViolation<Patient>> violations = validator.validate(patient);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("Gender must not be null");
    }
}
