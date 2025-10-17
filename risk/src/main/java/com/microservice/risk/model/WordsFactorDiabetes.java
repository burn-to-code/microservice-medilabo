package com.microservice.risk.model;

import lombok.Getter;

@Getter
public enum WordsFactorDiabetes {

    HEMOGLOBINE_A1C("hemoglobine a1c"),
    MICROALBUMINE("microalbumine"),
    TAILLE("taille"),
    POIDS("poids"),
    FUMEUR("fumeur"),
    FUMEUSE("fumeuse"),
    ANORMAL("anormal"),
    CHOLESTEROL("cholesterol"),
    VERTIGES("vertiges"),
    RECHUTE("rechute"),
    REACTION("reaction"),
    ANTICORPS("anticorps");

    private final String value;

    WordsFactorDiabetes(String value) {
        this.value = value;
    }

}
