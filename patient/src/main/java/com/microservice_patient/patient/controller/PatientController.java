package com.microservice_patient.patient.controller;

import com.microservice_patient.patient.service.PatientService;
import com.project.common.dto.PatientDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<PatientDTO> getAllPatients(){
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public PatientDTO getPatientById(@PathVariable Long id){
        return patientService.getPatientById(id);
    }

    @PostMapping()
    public ResponseEntity<PatientDTO> savePatient(@RequestBody @Valid PatientDTO patient){
        PatientDTO created = patientService.savePatient(patient);
        URI location = URI.create("/patients/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public PatientDTO updatePatient(@PathVariable Long id, @RequestBody @Valid PatientDTO patientDetails){
        return patientService.updatePatient(id, patientDetails);
    }
}
