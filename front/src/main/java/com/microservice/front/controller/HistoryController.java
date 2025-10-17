package com.microservice.front.controller;

import com.microservice.front.service.NoteService;
import com.microservice.front.service.RiskService;
import com.project.common.dto.NoteRequestDTO;
import com.project.common.dto.PatientDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/history")
@Slf4j
public class HistoryController {

    private final NoteService noteService;
    private final RiskService riskService;

    public HistoryController(NoteService noteService, RiskService riskService) {
        this.noteService = noteService;
        this.riskService = riskService;
    }

    @GetMapping("/{id}")
    public String listHistoryWithPatientId(@PathVariable Long id, Model model){
        log.debug("Getting history with patient id {}", id);

        Assert.notNull(id, "PatientId must not be null");
        Assert.isTrue(id > 0, "PatientId must be greater than 0");

        PatientDTO patient = riskService.getPatientWithRiskById(id);
        System.out.println("DEBUG patientDTO.riskOfDiabetes = " + patient.getRiskOfDiabetes());

        model.addAttribute("patient", patient);
        model.addAttribute("notes", noteService.getNoteAndDateByPatientId(id));
        model.addAttribute("noteRequest", new NoteRequestDTO(patient.getId(), patient.getFirstName()));

        return "patient/history";
    }

    @PostMapping("/save")
    public String saveNote(@Valid @ModelAttribute("noteRequest") NoteRequestDTO note, BindingResult result, RedirectAttributes redirectAttributes){
        if(result.hasErrors()){

            log.debug("Binding errors: {}", result.getAllErrors());

            if(note.getPatientID() == null || note.getPatientID() <= 0 || note.getPatientName().isBlank()) {
                redirectAttributes.addFlashAttribute("error", "Une erreur est survenue");
                return "redirect:/patient";
            }
            redirectAttributes.addFlashAttribute("error", "Veuillez remplir tous les champs");
            return "redirect:/history/" + note.getPatientID();
        }
        log.debug("Saving note {}", note);
        noteService.saveNote(note);

        return "redirect:/history/" + note.getPatientID();
    }

}
