package com.microservice.front.service;

import com.project.common.dto.PatientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService{

    private final RestTemplate restTemplate;

    public PatientServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public List<PatientDTO> getAllPatients() {
        ResponseEntity<PatientDTO[]> responseEntity =  restTemplate.getForEntity(
                "http://patient:8081/patients", PatientDTO[].class);
        assert responseEntity.getBody() != null;
        return Arrays.asList(responseEntity.getBody());
    }
}
