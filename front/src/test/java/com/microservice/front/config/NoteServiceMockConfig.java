package com.microservice.front.config;

import com.microservice.front.service.NoteService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoteServiceMockConfig {

    @Bean
    public NoteService noteService() {
        return Mockito.mock(NoteService.class);
    }
}
