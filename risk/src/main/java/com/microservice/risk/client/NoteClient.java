package com.microservice.risk.client;

import com.project.common.dto.NoteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "notes")
public interface NoteClient {

    @GetMapping("/notes/{patientId}")
    List<NoteResponseDTO> getNoteAndDateByPatientId(@PathVariable Long patientId);
}
