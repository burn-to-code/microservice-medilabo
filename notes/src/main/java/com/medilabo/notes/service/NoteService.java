package com.medilabo.notes.service;

import com.project.common.dto.NoteRequestDTO;
import com.project.common.dto.NoteResponseDTO;

import java.util.List;

public interface NoteService {

    void saveNote(NoteRequestDTO dto);

    List<NoteResponseDTO> getNoteAndDateByPatientId(Long id);
}
