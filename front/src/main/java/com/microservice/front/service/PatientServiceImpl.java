package com.microservice.front.service;

import com.microservice.front.client.PatientClientInterface;
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

    @Override
    public PatientDTO getPatientById(Long id) {
        return patientClientInterface.getPatientById(id);
    }

    @Override
    public PatientDTO savePatient(PatientDTO patient) {
        return patientClientInterface.savePatient(patient);
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientDTO patientDetails) {
        return patientClientInterface.updatePatient(id, patientDetails);
    }

    @Override
    public void deletePatientById(Long id) {
        patientClientInterface.deletePatientById(id);
    }
}
