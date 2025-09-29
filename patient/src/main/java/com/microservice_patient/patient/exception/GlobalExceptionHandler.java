package com.microservice_patient.patient.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PatientNotFound.class)
    public Map<String, String> patientNotFound(PatientNotFound ex){
        return Map.of("error", ex.getMessage());
    }
    @ExceptionHandler(PatientConflictException.class)
    public Map<String, String> patientConflict(PatientConflictException ex){
        return Map.of("error", ex.getMessage());
    }
}
