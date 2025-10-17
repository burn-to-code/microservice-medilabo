package com.project.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.common.model.Gender;
import com.project.common.model.LevelRiskOfDiabetes;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {
    private Long id;

    @NotBlank(message = "FirstName must not be blank")
    private String firstName;

    @NotBlank(message = "lastName must not be blank")
    private String lastName;

    @Past(message = "Date of birth must be in the past")
    @NotNull(message = "Date of birth must not be null")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender must not be null")
    private Gender gender;

    private String address;
    private String phoneNumber;

    private LevelRiskOfDiabetes riskOfDiabetes;

    public PatientDTO(Long id,
                      String firstName,
                      String lastName,
                      LocalDate dateOfBirth,
                      Gender gender,
                      String address,
                      String phoneNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
