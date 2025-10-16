package com.microservice.front.controller;

import com.microservice.front.service.NoteService;
import com.microservice.front.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping
    public String listHistoryWithPatientId(@RequestParam("PatientId") Long PatientId, Model model){
        log.debug("Getting history with patient id {}", PatientId);

        Assert.notNull(PatientId, "PatientId must not be null");
        Assert.isTrue(PatientId > 0, "PatientId must be greater than 0");

        model.addAttribute("patient", patientService.getPatientById(PatientId));
        model.addAttribute("notes", noteService.getNoteAndDateByPatientId(PatientId));

        return "history";
    }

}
