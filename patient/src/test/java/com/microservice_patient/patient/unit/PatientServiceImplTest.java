package com.microservice_patient.patient.unit;

import com.microservice_patient.patient.dao.PatientRepository;
import com.microservice_patient.patient.exception.PatientConflictException;
import com.microservice_patient.patient.exception.PatientNotFound;
import com.microservice_patient.patient.mapper.PatientMapper;
import com.microservice_patient.patient.model.Gender;
import com.microservice_patient.patient.model.Patient;
import com.microservice_patient.patient.service.PatientServiceImpl;
import com.project.common.dto.PatientDTO;
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

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientDTO patientDTO;

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

        patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setFirstName("John");
        patientDTO.setLastName("Doe");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setGender(com.project.common.model.Gender.M);
        patientDTO.setAddress("123 Street");
        patientDTO.setPhoneNumber("0102030405");
    }

    @Test
    void getAllPatients_returnsList() {
        when(patientRepository.findAll()).thenReturn(List.of(patient));
        when(patientMapper.toDTO(patient)).thenReturn(patientDTO);

        List<PatientDTO> patients = patientService.getAllPatients();

        assertThat(patients).hasSize(1);
        assertThat(patients.getFirst().getFirstName()).isEqualTo("John");
        verify(patientRepository, times(1)).findAll();
    }

    @Test
    void getPatientById_existing_returnsPatient() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.toDTO(patient)).thenReturn(patientDTO);

        PatientDTO found = patientService.getPatientById(1L);

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
        PatientDTO withId = new PatientDTO();
        withId.setId(99L);

        assertThrows(PatientConflictException.class, () -> patientService.savePatient(withId));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void savePatient_withoutId_savesPatient() {
        PatientDTO newPatientDTO = new PatientDTO();
        newPatientDTO.setFirstName("Jane");
        newPatientDTO.setLastName("Smith");

        Patient patientEntity = new Patient();
        patientEntity.setFirstName("Jane");
        patientEntity.setLastName("Smith");

        Patient savedEntity = new Patient();
        savedEntity.setId(2L);
        savedEntity.setFirstName("Jane");
        savedEntity.setLastName("Smith");

        PatientDTO savedDTO = new PatientDTO();
        savedDTO.setId(2L);
        savedDTO.setFirstName("Jane");
        savedDTO.setLastName("Smith");

        when(patientMapper.toEntity(newPatientDTO)).thenReturn(patientEntity);
        when(patientRepository.save(patientEntity)).thenReturn(savedEntity);
        when(patientMapper.toDTO(savedEntity)).thenReturn(savedDTO);

        PatientDTO result = patientService.savePatient(newPatientDTO);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getId()).isEqualTo(2L);
        verify(patientRepository, times(1)).save(patientEntity);
    }

    @Test
    void updatePatient_existing_updatesPatient() {
        PatientDTO updateDTO = new PatientDTO();
        updateDTO.setFirstName("Updated");
        updateDTO.setLastName("Name");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.toDTO(any(Patient.class))).thenReturn(updateDTO);
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));


        PatientDTO updated = patientService.updatePatient(1L, updateDTO);

        assertThat(updated.getFirstName()).isEqualTo("Updated");
        assertThat(updated.getLastName()).isEqualTo("Name");
        verify(patientRepository, times(1)).save(patient);
    }

    @Test
    void updatePatient_nonExisting_throwsNotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFound.class, () -> patientService.updatePatient(99L, patientDTO));
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
