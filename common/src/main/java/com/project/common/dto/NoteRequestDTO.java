package com.project.common.dto;

public record NoteRequestDTO(
        Long patientID,
        String patientName,
        String note
) { }
