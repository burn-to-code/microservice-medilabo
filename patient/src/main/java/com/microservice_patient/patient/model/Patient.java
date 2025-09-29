package com.microservice_patient.patient.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "FirstName Must Not Be Null")
    private String firstName;

    @NotBlank(message = "lastName must not be null")
    private String lastName;

    @NotBlank(message = "dateOfBirth must not be null")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender must not be null")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String address;
    private String phoneNumber;
}
