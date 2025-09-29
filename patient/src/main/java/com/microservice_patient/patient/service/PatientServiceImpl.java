package com.microservice_patient.patient.service;

import com.microservice_patient.patient.dao.PatientRepository;
import com.microservice_patient.patient.exception.PatientConflictException;
import com.microservice_patient.patient.exception.PatientNotFound;
import com.microservice_patient.patient.model.Patient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService{
    private final PatientRepository patientRepository;

    @Override
    public List<Patient> getAllPatients(){
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id){
        return patientRepository.findById(id).orElseThrow(() ->
                 new PatientNotFound("Patient with id " + id + " not found")
        );
    }

    @Override
    public Patient savePatient(Patient patient){
        if(patient.getId() != null){
            throw new PatientConflictException(
                    "Cannot create patient with predefined ID. Use PUT to update existing patient."
            );
        }
        return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Long id, Patient patientDetails){
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFound("Patient with id " + id + " not found"));

        existing.setFirstName(patientDetails.getFirstName());
        existing.setLastName(patientDetails.getLastName());
        existing.setDateOfBirth(patientDetails.getDateOfBirth());
        existing.setGender(patientDetails.getGender());
        existing.setAddress(patientDetails.getAddress());
        existing.setPhoneNumber(patientDetails.getPhoneNumber());

        return patientRepository.save(existing);
    }

    @Override
    public void deletePatientById(Long id){
        if(!patientRepository.existsById(id)){
            throw new PatientNotFound("Patient with id " + id + " not found");
        }
        patientRepository.deleteById(id);
    }

}
