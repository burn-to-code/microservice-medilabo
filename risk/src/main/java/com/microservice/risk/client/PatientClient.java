package com.microservice.risk.client;

import com.project.common.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "patient")
public interface PatientClient{

    @GetMapping("/patients")
    List<PatientDTO> getAllPatients();

    @GetMapping("/patients/{id}")
    PatientDTO getPatientById(@PathVariable Long id);
}
