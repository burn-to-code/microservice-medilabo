package com.medilabo.notes.controller;

import com.medilabo.notes.service.NoteService;
import com.project.common.dto.NoteRequestDTO;
import com.project.common.dto.NoteResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @GetMapping("/{patientId}")
    public ResponseEntity<List<NoteResponseDTO>> getNotesByPatientId(@PathVariable Long patientId){
        List<NoteResponseDTO> notes = noteService.getNoteAndDateByPatientId(patientId);
        return ResponseEntity.ok(notes);
    }

    @PostMapping
    public ResponseEntity<Void> saveNote(@RequestBody @Valid NoteRequestDTO note){
        noteService.saveNote(note);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
