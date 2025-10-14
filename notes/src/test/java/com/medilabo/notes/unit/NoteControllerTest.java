package com.medilabo.notes.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.notes.config.NoteServiceMockConfig;
import com.medilabo.notes.controller.NoteController;
import com.medilabo.notes.service.NoteService;
import com.project.common.dto.NoteRequestDTO;
import com.project.common.dto.NoteResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(NoteServiceMockConfig.class)
@WebMvcTest(NoteController.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetNotes() throws Exception {

        //given
        List<NoteResponseDTO> notes = List.of(
                new NoteResponseDTO("Test note", LocalDate.of(2022, 1, 1))
        );
        when(noteService.getNoteAndDateByPatientId(1L)).thenReturn(notes);

        //When + Then
        mockMvc.perform(get("/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].note").value("Test note"));
    }

    @Test
    void testGetNotes_nonExistingPatient_returnsEmptyList() throws Exception {
        //Given
        when(noteService.getNoteAndDateByPatientId(99L)).thenReturn(List.of());

        //When + Then
        mockMvc.perform(get("/notes/99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testGetNotes_withInvalidPatientId_shouldReturnBadRequest() throws Exception {
        // GIVEN
        when(noteService.getNoteAndDateByPatientId(-1L))
                .thenThrow(new IllegalArgumentException("Patient id must be greater than 0"));

        // WHEN + THEN
        mockMvc.perform(get("/notes/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Patient id must be greater than 0"));
    }

    @Test
    void saveNote () throws Exception {
        NoteRequestDTO noteRequestDTO = new NoteRequestDTO(1L, "Test Patient", "Test note");

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noteRequestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void saveNote_shouldReturnBadRequest_whenInvalidRequest() throws Exception {
        NoteRequestDTO invalidNote = new NoteRequestDTO(null, "", "");

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidNote)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.patientID").value("Patient ID is required"))
                .andExpect(jsonPath("$.patientName").value("Patient name is required"))
                .andExpect(jsonPath("$.note").value("Note is required"));
    }
}

