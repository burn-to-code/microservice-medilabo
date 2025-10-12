package com.project.common.dto;

import java.time.LocalDate;

public record NoteResponseDTO(
        String note,
        LocalDate time
) {}
