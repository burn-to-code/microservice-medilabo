package com.microservice.front.controller;

import com.microservice.front.service.NoteService;
import com.microservice.front.service.PatientService;
import com.project.common.dto.NoteRequestDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/history")
@Slf4j
public class HistoryController {

    private final PatientService patientService;
    private final NoteService noteService;

    public HistoryController(PatientService patientService, NoteService noteService) {
        this.patientService = patientService;
        this.noteService = noteService;
    }

    @ModelAttribute("noteRequest")
    public NoteRequestDTO noteRequest() {
        return new NoteRequestDTO(0L, "", "");
    }


    @GetMapping("/{id}")
    public String listHistoryWithPatientId(@PathVariable Long id, Model model){
        log.debug("Getting history with patient id {}", id);

        Assert.notNull(id, "PatientId must not be null");
        Assert.isTrue(id > 0, "PatientId must be greater than 0");

        model.addAttribute("patient", patientService.getPatientById(id));
        model.addAttribute("notes", noteService.getNoteAndDateByPatientId(id));

        return "patient/history";
    }

    @PostMapping("/save")
    public String saveNote(@Valid @ModelAttribute("noteRequest") NoteRequestDTO note){
        log.debug("Saving note {}", note);
        noteService.saveNote(note);

        return "redirect:/history/" + note.patientID();
    }

}
