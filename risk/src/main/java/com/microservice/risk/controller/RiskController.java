package com.microservice.risk.controller;

import com.microservice.risk.service.RiskCalculatorService;
import com.project.common.dto.PatientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/risk")
@Slf4j
public class RiskController {

    private final RiskCalculatorService riskCalculatorService;

    public RiskController(RiskCalculatorService riskCalculatorService) {
        this.riskCalculatorService = riskCalculatorService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<PatientDTO>> getAllPatients(){
        log.info("Requête reçu pour obtenir les patients");
        List<PatientDTO> patients =  riskCalculatorService.calculateDiabeteForAllPatient();
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id){
        log.info("requête reçu pour obtenir le patient avec l'id {}", id);
        PatientDTO patient = riskCalculatorService.calculateDiabeteForOnePatient(id);

        if(patient == null || patient.getId() == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(patient);
    }

}
