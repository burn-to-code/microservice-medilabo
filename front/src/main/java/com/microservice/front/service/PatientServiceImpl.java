package com.microservice.front.service;

import com.microservice.front.config.clientpatient.PatientClientInterface;
import com.project.common.dto.PatientDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService{

    private final PatientClientInterface patientClientInterface;

    public PatientServiceImpl(PatientClientInterface patientClientInterface) {
        this.patientClientInterface = patientClientInterface;
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return patientClientInterface.getAllPatients();
    }
}
