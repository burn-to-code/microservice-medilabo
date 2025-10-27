package com.microservice.front.service;

import com.microservice.front.client.GatewayClientInterface;
import com.project.common.dto.PatientDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RiskServiceImpl implements RiskService{

    private final GatewayClientInterface gatewayClientInterface;

    public RiskServiceImpl(GatewayClientInterface gatewayClientInterface) {
        this.gatewayClientInterface = gatewayClientInterface;
    }

    @Override
    public List<PatientDTO> getPatientsWithRisk(List<PatientDTO> patients) {
    return gatewayClientInterface.getAllPatientsWithRisk(patients);
    }

    @Override
    public PatientDTO getPatientWithRisk(PatientDTO patient) {
        return gatewayClientInterface.getPatientWithRiskById(patient);
    }
}
