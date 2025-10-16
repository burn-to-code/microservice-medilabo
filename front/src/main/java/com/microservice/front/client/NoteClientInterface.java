package com.microservice.front.client;

import com.project.common.dto.NoteRequestDTO;
import com.project.common.dto.NoteResponseDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        name = "gateway"
)
public interface NoteClientInterface {

    @GetMapping("/notes/{patientId}")
    List<NoteResponseDTO> getNoteAndDateByPatientId(@PathVariable Long patientId);

    @PostMapping("/notes")
    void saveNote(@Valid @RequestBody NoteRequestDTO note);
}
