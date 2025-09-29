package com.microservice_patient.patient.controller;

import com.microservice_patient.patient.model.Patient;
import com.microservice_patient.patient.service.PatientService;
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
    public List<Patient> getAllPatients(){
        return patientService.getAllPatients();
    }

    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id){
        return patientService.getPatientById(id);
    }

    @PostMapping()
    public ResponseEntity<Patient> savePatient(@RequestBody @Valid Patient patient){
        Patient created = patientService.savePatient(patient);
        URI location = URI.create("/patients/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @RequestBody @Valid Patient patientDetails){
        return patientService.updatePatient(id, patientDetails);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePatientById(@PathVariable Long id){
        patientService.deletePatientById(id);
    }
}
