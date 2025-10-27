package com.microservice.front.service;

import com.project.common.dto.PatientDTO;

import java.util.List;

public interface RiskService {

    List<PatientDTO> getPatientsWithRisk(List<PatientDTO> patients);

    PatientDTO getPatientWithRisk(PatientDTO patient);
}
