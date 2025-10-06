package com.microservice.front.config;

import com.microservice.front.service.PatientService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PatientServiceMockConfig {

    @Bean
    public PatientService patientService() {
        return Mockito.mock(PatientService.class);
    }
}
