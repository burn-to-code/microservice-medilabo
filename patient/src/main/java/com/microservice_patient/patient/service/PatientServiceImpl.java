package com.microservice_patient.patient.service;

import com.microservice_patient.patient.dao.PatientRepository;
import com.microservice_patient.patient.exception.PatientConflictException;
import com.microservice_patient.patient.exception.PatientNotFound;
import com.microservice_patient.patient.mapper.PatientMapper;
import com.microservice_patient.patient.model.Patient;
import com.project.common.dto.PatientDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService{
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    public List<PatientDTO> getAllPatients(){
        log.debug("Getting all patients");
        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PatientDTO getPatientById(Long id){
        log.debug("Getting patient with id {}", id);
        Patient patient = patientRepository.findById(id).orElseThrow(() ->
                 new PatientNotFound("Patient with id " + id + " not found")
        );
        log.info("Found patient {}", patient.getFirstName()+" "+patient.getLastName());
        return patientMapper.toDTO(patient);
    }

    @Override
    public PatientDTO savePatient(PatientDTO patient){
        log.debug("Saving patient {}", patient.getFirstName()+" "+patient.getLastName());
        if(patient.getId() != null){
            throw new PatientConflictException(
                    "Cannot create patient with predefined ID. Use PUT to update existing patient."
            );
        }
        Patient patientSave = patientRepository.save(patientMapper.toEntity(patient));
        log.info("Saved patient {}", patient.getFirstName()+" "+patient.getLastName());
        return patientMapper.toDTO(patientSave);
    }

    @Override
    public PatientDTO updatePatient(Long id, PatientDTO patientDetails){
        log.debug("Updating patient with id {}", id);
        Patient existing = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFound("Patient with id " + id + " not found"));

        log.debug("Existing patient {}", existing.getFirstName()+" "+existing.getLastName());
        patientMapper.updateEntityFromDTO(patientDetails, existing);

        Patient patientUpdate = patientRepository.save(existing);
        log.info("Updated patient {}", patientUpdate.getFirstName()+" "+patientUpdate.getLastName());

        return patientMapper.toDTO(patientUpdate);
    }

}
