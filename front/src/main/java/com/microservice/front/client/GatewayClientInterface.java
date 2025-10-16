package com.microservice.front.client;

import com.project.common.dto.NoteRequestDTO;
import com.project.common.dto.NoteResponseDTO;
import com.project.common.dto.PatientDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "gateway"
)
public interface GatewayClientInterface {

    @GetMapping("/patients")
    List<PatientDTO> getAllPatients();

    @GetMapping("/patients/{id}")
    PatientDTO getPatientById(@PathVariable Long id);

    @PostMapping("/patients")
    PatientDTO savePatient(@Valid @RequestBody PatientDTO patient);

    @PutMapping("/patients/{id}")
    PatientDTO updatePatient(@PathVariable Long id, @Valid @RequestBody PatientDTO patientDetails);

    @DeleteMapping("/patients/{id}")
    void deletePatientById(@PathVariable Long id);

    @GetMapping("/notes/{patientId}")
    List<NoteResponseDTO> getNoteAndDateByPatientId(@PathVariable Long patientId);

    @PostMapping("/notes")
    void saveNote(@Valid @RequestBody NoteRequestDTO note);
}
