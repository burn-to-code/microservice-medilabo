package com.microservice.front.service;

import com.microservice.front.client.GatewayClientInterface;
import com.project.common.dto.PatientDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService{

    private final GatewayClientInterface gatewayClientInterface;

    public PatientServiceImpl(GatewayClientInterface gatewayClientInterface) {
        this.gatewayClientInterface = gatewayClientInterface;
    }

    @Override
    public List<PatientDTO> getAllPatients() {
        return gatewayClientInterface.getAllPatients();
    }

    @Override
    public PatientDTO getPatientById(Long id) {
        return gatewayClientInterface.getPatientById(id);
    }

    @Override
    public PatientDTO savePatient(PatientDTO patient) {
        return gatewayClientInterface.savePatient(patient);
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientDTO patientDetails) {
        return gatewayClientInterface.updatePatient(id, patientDetails);
    }

    @Override
    public void deletePatientById(Long id) {
        gatewayClientInterface.deletePatientById(id);
    }
}
