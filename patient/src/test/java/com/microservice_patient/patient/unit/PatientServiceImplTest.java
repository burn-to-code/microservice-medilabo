package com.microservice_patient.patient.unit;

import com.microservice_patient.patient.dao.PatientRepository;
import com.microservice_patient.patient.exception.PatientConflictException;
import com.microservice_patient.patient.exception.PatientNotFound;
import com.microservice_patient.patient.model.Gender;
import com.microservice_patient.patient.model.Patient;
import com.microservice_patient.patient.service.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patient.setGender(Gender.M);
        patient.setAddress("123 Street");
        patient.setPhoneNumber("0102030405");
    }

    @Test
    void getAllPatients_returnsList() {
        when(patientRepository.findAll()).thenReturn(List.of(patient));

        List<Patient> patients = patientService.getAllPatients();

        assertThat(patients).hasSize(1);
        assertThat(patients.getFirst().getFirstName()).isEqualTo("John");
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getPatientById_existing_returnsPatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient found = patientService.getPatientById(1L);

        assertThat(found.getLastName()).isEqualTo("Doe");
        verify(patientRepository, times(1)).findById(1L);
    }

    @Test
    void getPatientById_nonExisting_throwsNotFound() {
        when(patientRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFound.class, () -> patientService.getPatientById(2L));
        verify(patientRepository, times(1)).findById(2L);
    }

    @Test
    void savePatient_withId_throwsConflict() {
        Patient withId = new Patient();
        withId.setId(99L);

        assertThrows(PatientConflictException.class, () -> patientService.savePatient(withId));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void savePatient_withoutId_savesPatient() {
        Patient newPatient = new Patient();
        newPatient.setFirstName("Jane");
        newPatient.setLastName("Smith");

        when(patientRepository.save(newPatient)).thenReturn(newPatient);

        Patient saved = patientService.savePatient(newPatient);

        assertThat(saved.getFirstName()).isEqualTo("Jane");
        verify(patientRepository, times(1)).save(newPatient);
    }

    @Test
    void updatePatient_existing_updatesPatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Patient updatedDetails = new Patient();
        updatedDetails.setFirstName("Updated");
        updatedDetails.setLastName("Name");

        Patient updated = patientService.updatePatient(1L, updatedDetails);

        assertThat(updated.getFirstName()).isEqualTo("Updated");
        assertThat(updated.getLastName()).isEqualTo("Name");
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void updatePatient_nonExisting_throwsNotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFound.class, () -> patientService.updatePatient(99L, patient));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void deletePatient_existing_deletes() {
        when(patientRepository.existsById(1L)).thenReturn(true);

        patientService.deletePatientById(1L);

        verify(patientRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePatient_nonExisting_throwsNotFound() {
        when(patientRepository.existsById(99L)).thenReturn(false);

        assertThrows(PatientNotFound.class, () -> patientService.deletePatientById(99L));
        verify(patientRepository, never()).deleteById(any());
    }
}
