package com.medilabo.notes.service;

import com.medilabo.notes.dao.NoteRepository;
import com.medilabo.notes.model.Note;
import com.project.common.dto.NoteRequestDTO;
import com.project.common.dto.NoteResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;


    @Override
    public void saveNote(NoteRequestDTO dto){
        Assert.notNull(dto, "Note request dto must not be null");
        log.debug("Saving note {}", dto);
        noteRepository.save(new Note(dto.getPatientID(), dto.getPatientName(), dto.getNote()));
    }

    @Override
    public List<NoteResponseDTO> getNoteAndDateByPatientId(Long id){
        Assert.notNull(id, "Patient id must not be null");
        Assert.isTrue(id > 0, "Patient id must be greater than 0");

        log.debug("Getting notes and date by patient id {}", id);
        List<Note> notes = noteRepository.findByPatientIdOrderByDateDesc(id);

        log.info("Found {} notes for patient id {}", notes.size(), id);
        return notes.stream()
                .map(note -> new NoteResponseDTO(note.getNote(), note.getDate()))
                .toList();
    }
}
