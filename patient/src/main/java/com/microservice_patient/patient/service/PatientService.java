package com.microservice_patient.patient.service;

import com.microservice_patient.patient.model.Patient;

import java.util.List;

public interface PatientService {

    List<Patient> getAllPatients();
    Patient getPatientById(Long id);
    Patient savePatient(Patient patient);
    Patient updatePatient(Long id, Patient patientDetails);
    void deletePatientById(Long id);
}
