package com.microservice.front.service;

import com.project.common.dto.NoteRequestDTO;
import com.project.common.dto.NoteResponseDTO;

import java.util.List;

public interface NoteService {

    List<NoteResponseDTO> getNoteAndDateByPatientId(Long id);

    void saveNote(NoteRequestDTO note);
}
