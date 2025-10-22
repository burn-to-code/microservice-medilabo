package com.medilabo.notes.model;

import java.time.LocalDate;

public interface NoteProjection {
    String getNote();
    LocalDate getCreationDate();
}
