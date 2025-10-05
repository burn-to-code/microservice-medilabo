package com.microservice_patient.patient.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PatientNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> patientNotFound(PatientNotFound ex){
        log.error("Patient not found", ex);
        return Map.of("error", ex.getMessage());
    }

    @ExceptionHandler(PatientConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> patientConflict(PatientConflictException ex){
        log.error("Patient conflict", ex);
        return Map.of("error", ex.getMessage());
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> genericHandler(RuntimeException ex){
        return Map.of("error", ex.getMessage());
    }

}
