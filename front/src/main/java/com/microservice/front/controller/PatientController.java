package com.microservice.front.controller;

import com.microservice.front.service.PatientService;
import com.project.common.dto.PatientDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
    public String savePatient(@Valid PatientDTO patient, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("patient", patient);
            return "patient/add";
        }
        patientService.savePatient(patient);
        return "redirect:/medilabo/patient";
    }
}
