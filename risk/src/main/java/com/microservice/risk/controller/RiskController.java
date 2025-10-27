package com.microservice.risk.controller;

import com.microservice.risk.service.RiskCalculatorService;
import com.project.common.dto.PatientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/risk")
@Slf4j
public class RiskController {

    private final RiskCalculatorService riskCalculatorService;

    public RiskController(RiskCalculatorService riskCalculatorService) {
        this.riskCalculatorService = riskCalculatorService;
    }

    @PostMapping("/all")
    public ResponseEntity<List<PatientDTO>> getAllPatients(@RequestBody List<PatientDTO> patientList) {
        log.info("Requête reçu pour obtenir les patients");
        List<PatientDTO> patients =  riskCalculatorService.calculateDiabeteForAllPatient(patientList);
        return ResponseEntity.ok(patients);
    }

    @PostMapping()
    public ResponseEntity<PatientDTO> getPatient(@RequestBody PatientDTO patientWithNotRisk){
        PatientDTO patient = riskCalculatorService.calculateDiabeteForOnePatient(patientWithNotRisk);

        if(patient == null || patient.getId() == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(patient);
    }

}
