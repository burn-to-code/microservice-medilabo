package com.microservice.risk.service;

import com.project.common.dto.PatientDTO;

import java.util.List;

public interface RiskCalculatorService {

    List<PatientDTO> calculateDiabeteForAllPatient(List<PatientDTO> patientList);

    PatientDTO calculateDiabeteForOnePatient(PatientDTO patient);
}
