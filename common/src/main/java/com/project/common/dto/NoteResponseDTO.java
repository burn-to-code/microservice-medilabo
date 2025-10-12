package com.project.common.dto;

import java.time.LocalDateTime;

public record NoteResponseDTO(
        String notes,
        LocalDateTime time
) {}
