package com.microservice.front.service;

import com.project.common.dto.PatientDTO;

import java.util.List;

public interface PatientService {

    List<PatientDTO> getAllPatients();
}
