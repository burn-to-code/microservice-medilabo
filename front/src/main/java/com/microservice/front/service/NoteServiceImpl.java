package com.microservice.front.service;

import com.microservice.front.client.NoteClientInterface;
import com.project.common.dto.NoteRequestDTO;
import com.project.common.dto.NoteResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NoteServiceImpl implements NoteService {

    private final NoteClientInterface noteClientInterface;

    public NoteServiceImpl(NoteClientInterface noteClientInterface) {
        this.noteClientInterface = noteClientInterface;
    }


    @Override
    public List<NoteResponseDTO> getNoteAndDateByPatientId(Long id) {
        log.debug("Getting notes and date by patient id {}", id);
        return noteClientInterface.getNoteAndDateByPatientId(id);
    }

    @Override
    public void saveNote(NoteRequestDTO note) {
        log.debug("Saving note {}", note);
        noteClientInterface.saveNote(note);
    }
}
