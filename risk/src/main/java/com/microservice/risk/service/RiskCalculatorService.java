package com.microservice.risk.service;

import com.project.common.dto.PatientDTO;

import java.util.List;

public interface RiskCalculatorService {

    List<PatientDTO> calculateDiabeteForAllPatient();
    PatientDTO calculateDiabeteForOnePatient(Long id);
}
