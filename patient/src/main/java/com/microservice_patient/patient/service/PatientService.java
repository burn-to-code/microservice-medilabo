package com.microservice_patient.patient.service;

import com.project.common.dto.PatientDTO;

import java.util.List;

public interface PatientService {

    List<PatientDTO> getAllPatients();
    PatientDTO getPatientById(Long id);
    PatientDTO savePatient(PatientDTO patient);
    PatientDTO updatePatient(Long id, PatientDTO patientDetails);
    void deletePatientById(Long id);
}
