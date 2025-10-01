package com.microservice.front.controller;

import com.microservice.front.service.PatientService;
import com.project.common.dto.PatientDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
}
