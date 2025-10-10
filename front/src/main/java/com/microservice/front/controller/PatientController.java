package com.microservice.front.controller;

import com.microservice.front.service.PatientService;
import com.project.common.dto.PatientDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public String listPatient(Model model){
        List<PatientDTO> patients = patientService.getAllPatients();
        model.addAttribute("patients", patients);
        return "patient/list";
    }

    @GetMapping("/add")
    public String addPatient(Model model){
        model.addAttribute("patient", new PatientDTO());
        return "patient/add";
    }

    @PostMapping("/save")
    public String savePatient(@Valid @ModelAttribute("patient") PatientDTO patient, BindingResult result){
        if(result.hasErrors()){
            return "patient/add";
        }
        patientService.savePatient(patient);
        return "redirect:/patient";
    }

    @GetMapping("/edit/{id}")
    public String editPatientForm(@PathVariable Long id, Model model){
        PatientDTO patient = patientService.getPatientById(id);
        model.addAttribute("patient", patient);
        return "patient/edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePatient(@PathVariable Long id, @Valid @ModelAttribute("patient") PatientDTO patient, BindingResult result){
        if(result.hasErrors()){
            return "patient/edit";
        }

        log.debug("Sending patient update: {}", patient);

        patientService.updatePatient(id, patient);
        return "redirect:/patient";
    }
}
