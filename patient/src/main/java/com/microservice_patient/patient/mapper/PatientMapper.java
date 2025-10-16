package com.microservice_patient.patient.mapper;

import com.microservice_patient.patient.model.Patient;
import com.project.common.dto.PatientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "gender", source = "gender", qualifiedByName = "toDTOGender")
    @Mapping(target = "riskOfDiabetes", ignore = true)
    PatientDTO toDTO(Patient patient);

    @Mapping(target = "gender", source = "gender", qualifiedByName = "toEntityGender")
    Patient toEntity(PatientDTO patientDTO);

    // Met à jour une entité existante avec les données du DTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gender", source = "gender", qualifiedByName = "toEntityGender")
    void updateEntityFromDTO(PatientDTO dto, @MappingTarget Patient entity);

    @Named("toDTOGender")
    default com.project.common.model.Gender mapToDTO(com.microservice_patient.patient.model.Gender gender){
        return gender != null ? com.project.common.model.Gender.valueOf(gender.name()) : null;
    }

    @Named("toEntityGender")
    default com.microservice_patient.patient.model.Gender mapToEntity(com.project.common.model.Gender gender){
        return gender != null ? com.microservice_patient.patient.model.Gender.valueOf(gender.name()) : null;
    }
}
