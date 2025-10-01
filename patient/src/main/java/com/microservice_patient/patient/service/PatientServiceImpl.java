package com.microservice_patient.patient.service;

import com.microservice_patient.patient.dao.PatientRepository;
import com.microservice_patient.patient.exception.PatientConflictException;
import com.microservice_patient.patient.exception.PatientNotFound;
import com.microservice_patient.patient.model.Patient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService{
    private final PatientRepository patientRepository;

    @Override
    public List<Patient> getAllPatients(){
        log.debug("Getting all patients");
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id){
        log.debug("Getting patient with id {}", id);
        Patient patient = patientRepository.findById(id).orElseThrow(() ->
                 new PatientNotFound("Patient with id " + id + " not found")
        );
        log.info("Found patient {}", patient.getFirstName()+" "+patient.getLastName());
        return patient;
    }

    @Override
    public Patient savePatient(Patient patient){
        log.debug("Saving patient {}", patient.getFirstName()+" "+patient.getLastName());
        if(patient.getId() != null){
            throw new PatientConflictException(
                    "Cannot create patient with predefined ID. Use PUT to update existing patient."
            );
        }
        Patient patientSave = patientRepository.save(patient);
        log.info("Saved patient {}", patient.getFirstName()+" "+patient.getLastName());
        return patientSave;
    }

    @Override
    public Patient updatePatient(Long id, Patient patientDetails){
        log.debug("Updating patient with id {}", id);
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFound("Patient with id " + id + " not found"));

        log.debug("Existing patient {}", existing.getFirstName()+" "+existing.getLastName());
        existing.setFirstName(patientDetails.getFirstName());
        existing.setLastName(patientDetails.getLastName());
        existing.setDateOfBirth(patientDetails.getDateOfBirth());
        existing.setGender(patientDetails.getGender());
        existing.setAddress(patientDetails.getAddress());
        existing.setPhoneNumber(patientDetails.getPhoneNumber());

        Patient patientUpdate = patientRepository.save(existing);
        log.info("Updated patient {}", patientUpdate.getFirstName()+" "+patientUpdate.getLastName());
        return patientUpdate;
    }

    @Override
    public void deletePatientById(Long id){
        log.debug("Deleting patient with id {}", id);
        if(!patientRepository.existsById(id)){
            throw new PatientNotFound("Patient with id " + id + " not found");
        }
        patientRepository.deleteById(id);
        log.info("Deleted patient with id {}", id);
    }

}
