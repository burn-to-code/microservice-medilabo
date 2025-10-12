package com.project.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NoteRequestDTO(
        @NotNull(message = "Patient ID is required")
        Long patientID,
        @NotBlank(message = "Patient name is required")
        String patientName,
        @NotBlank(message = "Note is required")
        String note
) { }
