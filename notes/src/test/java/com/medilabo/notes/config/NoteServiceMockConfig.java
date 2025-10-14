package com.medilabo.notes.config;

import com.medilabo.notes.service.NoteService;
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
