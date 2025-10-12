package com.medilabo.notes.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "notes")
@Data
public class Note {
    @Id
    private String id;

    @NotNull(message = "Patient id must not be null")
    @Field("patId")
    private Long patientId;

    @Field("patient")
    @NotBlank(message = "Patient name must not be blank")
    private String patientName;

    @NotBlank(message = "Note must not be blank")
    private String note;

    @NotNull(message = "Date of birth must not be null")
    @Past(message = "Date of birth must be in the past")
    private LocalDate date;

    public Note(Long patientId, String patientName, String note) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.note = note;
        this.date = LocalDate.now();
    }
}
