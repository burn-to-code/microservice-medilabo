package com.microservice.front.service;

import com.project.common.dto.PatientDTO;

import java.util.List;

public interface RiskService {

    List<PatientDTO> getPatientsWithRisk();

    PatientDTO getPatientWithRiskById(Long id);
}
