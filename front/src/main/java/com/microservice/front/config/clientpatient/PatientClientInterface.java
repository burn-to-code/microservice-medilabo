package com.microservice.front.config.clientpatient;

import com.microservice.front.config.BasicAuthClientConfig;
import com.project.common.dto.PatientDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "patient-service",
        url = "${spring.application.rest.client.url}",
        configuration = BasicAuthClientConfig.class
)
public interface PatientClientInterface {

    @GetMapping("/patients")
    List<PatientDTO> getAllPatients();


    @GetMapping("/patients/{id}")
    PatientDTO getPatientById(@PathVariable Long id);
}
