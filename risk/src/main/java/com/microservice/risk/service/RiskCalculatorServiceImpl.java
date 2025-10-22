package com.microservice.risk.service;

import com.microservice.risk.client.NoteClient;
import com.microservice.risk.client.PatientClient;
import com.microservice.risk.model.WordsFactorDiabetes;
import com.project.common.dto.NoteResponseDTO;
import com.project.common.dto.PatientDTO;
import com.project.common.model.Gender;
import com.project.common.model.LevelRiskOfDiabetes;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
@Service
@Slf4j
public class RiskCalculatorServiceImpl implements RiskCalculatorService {

    private final PatientClient patientClient;
    private final NoteClient noteClient;

    public RiskCalculatorServiceImpl(PatientClient patientClient, NoteClient noteClient) {
        this.patientClient = patientClient;
        this.noteClient = noteClient;
    }

    /**
     * Calcule le risque de diabète pour une liste de patients.
     * <p>
     * Pour chaque patient de la liste, la méthode :
     * <ul>
     *      <li>Récupère tous les patient via le service {@link PatientClient}</li>
     *     <li>Récupère ses notes via le service {@link NoteClient}</li>
     *     <li>Calcule son âge à partir de sa date de naissance</li>
     *     <li>Compte le nombre de déclencheurs présents dans ses notes</li>
     *     <li>Détermine le niveau de risque selon l'âge, le sexe et le score</li>
     *     <li>Met à jour le champ {@link PatientDTO#setRiskOfDiabetes(LevelRiskOfDiabetes)} avec le résultat</li>
     * </ul>
     * <p>
     * Si une exception se produit lors de l'appel des services externes ou du calcul, le risque du patient
     * sera défini sur {@link LevelRiskOfDiabetes#DonneesInsuffisantes}.
     *
     * @return la liste des patients avec le champ {@code riskOfDiabetes} mis à jour
     */
    @Override
    public List<PatientDTO> calculateDiabeteForAllPatient() {
        List<PatientDTO> patientList;
        try {
            patientList = patientClient.getAllPatients();
        } catch (FeignException e){
            log.error("Impossible de récupérer la liste des patients : {}", e.getMessage(), e);
            PatientDTO placeholder = new PatientDTO(null, "Indisponible", "Indisponible", null, Gender.UNDEFINED, null, null);
            placeholder.setRiskOfDiabetes(LevelRiskOfDiabetes.DonneesInsuffisantes);
            return List.of(placeholder);
        }

        log.info("Calcul du risque de diabète pour {} patients", patientList.size());

        if (patientList.isEmpty()) {
            log.info("Liste de patient vide, pas de calcul");
            return patientList;
        }
        return patientList.stream()
                .peek(this::tryAndCatchCalculateDiabetesWithPatientDTO)
                .toList();
    }

    /**
     * Calcule le risque de diabète pour un patient unique identifié par son ID.
     * <p>
     * La méthode :
     * <ul>
     *     <li>Récupère le patient via {@link PatientClient}</li>
     *     <li>Récupère ses notes via {@link NoteClient}</li>
     *     <li>Calcule l'âge et le score des déclencheurs dans les notes</li>
     *     <li>Détermine le niveau de risque selon l'âge, le sexe et le score</li>
     *     <li>Met à jour le champ {@link PatientDTO#setRiskOfDiabetes(LevelRiskOfDiabetes)} avec le résultat</li>
     * </ul>
     * <p>
     * Si le patient est introuvable ou si une exception survient lors des appels externes, un
     * {@link PatientDTO} de fallback est retourné avec le champ {@code riskOfDiabetes} à
     * {@link LevelRiskOfDiabetes#DonneesInsuffisantes}.
     *
     * @param patientId l'identifiant du patient pour lequel calculer le risque
     * @return le patient avec le champ {@code riskOfDiabetes} mis à jour, ou un fallback si erreur
     */
    @Override
    public PatientDTO calculateDiabeteForOnePatient(Long patientId) {
        Assert.notNull(patientId, "patientId must not be null");
        log.debug("Calcul du risque de diabète pour le patient ID={}", patientId);
        return tryAndCatchCalculateDiabetesWithPatientId(patientId);

    }

    private PatientDTO tryAndCatchCalculateDiabetesWithPatientId(Long patientId) {
        try {
            PatientDTO patient = patientClient.getPatientById(patientId);
            List<NoteResponseDTO> notes = noteClient.getNoteAndDateByPatientId(patientId);

            if (patient == null) {
                log.warn("Patient ID={} introuvable", patientId);
                return new PatientDTO();
            }

            if (notes.isEmpty() ) {
                log.warn("Aucune note trouvée pour le patient ID={}", patient.getId());
                patient.setRiskOfDiabetes(LevelRiskOfDiabetes.None);
                return patient;
            }

            int age = calculateAgeForOnePatient(patient.getDateOfBirth());
            int score = countWordFactorsInNotes(notes);

            LevelRiskOfDiabetes result = levelOfRisk(age, score, patient.getGender());
            patient.setRiskOfDiabetes(result);
            log.info("Patient ID={} : âge={}, score={}, risque={}", patientId, age, score, result);
            return patient;
        } catch (FeignException e) {
            log.error("Erreur Feign lors de l'appel d'un service externe pour le patient ID={}", patientId, e);
            return createFallBackPatientDTO(patientId);
        } catch (Exception e) {
            log.error("Échec du calcul du risque pour le patient ID={}: {}", patientId, e.getMessage());
            return createFallBackPatientDTO(patientId);
        }
    }

    private void tryAndCatchCalculateDiabetesWithPatientDTO(PatientDTO p) {
        try {
            log.debug("Calcul du risque pour le patient ID={} Nom={}", p.getId(), p.getLastName());
            List<NoteResponseDTO> notes = noteClient.getNoteAndDateByPatientId(p.getId());

            if (notes.isEmpty()) {
                log.warn("Aucune note trouvée pour un patient ID={}", p.getId());
                p.setRiskOfDiabetes(LevelRiskOfDiabetes.None);
                return;
            }

            int age = calculateAgeForOnePatient(p.getDateOfBirth());
            int score = countWordFactorsInNotes(notes);

            LevelRiskOfDiabetes result = levelOfRisk(age, score, p.getGender());
            log.info("Résultat patient ID={} : âge={}, score={}, risque={}", p.getId(), age, score, result);
            p.setRiskOfDiabetes(result);

        } catch (FeignException e) {
            log.error("Erreur Feign lors de l'appel d'un service externe pour le patient ID={}", p.getId(), e);
            p.setRiskOfDiabetes(LevelRiskOfDiabetes.DonneesInsuffisantes);
        } catch (Exception e) {
            log.error("Erreur lors du calcul du risque pour le patient ID={}: {}", p.getId(), e.getMessage());
            p.setRiskOfDiabetes(LevelRiskOfDiabetes.DonneesInsuffisantes);
        }
    }

    // Pour fallback
    private PatientDTO createFallBackPatientDTO(Long patientId){
        try {
            PatientDTO fallback = patientClient.getPatientById(patientId);
            fallback.setRiskOfDiabetes(LevelRiskOfDiabetes.DonneesInsuffisantes);
            return fallback;
        } catch (FeignException e) {
            log.error("Erreur Feign lors de l'appel d'un service externe pour le patient ID={}", patientId, e);
            PatientDTO fallback = new PatientDTO(patientId, "Inconnu", "Inconnu", null, Gender.UNDEFINED, null, null);
            fallback.setRiskOfDiabetes(LevelRiskOfDiabetes.DonneesInsuffisantes);
            return fallback;
        }
    }

    // Pour calculer l'age d'un patient'
    private int calculateAgeForOnePatient(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    // 3 méthodes pour récupérer le score
    private String fusionAllPatientNotes(List<NoteResponseDTO> notes) {
        List<String> notesAsString = notes
                .stream()
                .map(NoteResponseDTO::note)
                .toList();

        return String.join(" ", notesAsString).toLowerCase();
    }

    private String normalizeText(String note) {
        return Normalizer.normalize(note, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")  // supprime accents
                .replaceAll("[^a-z0-9 ]", " ") // garde lettres/chiffres/espace
                .replaceAll("\\s+", " ") // normalise espaces multiples
                .trim();
    }

    /**
     * Compte le nombre de facteurs de risque de diabète présents dans les notes d'un patient.
     * <p>
     * La méthode normalise d'abord toutes les notes du patient en supprimant les accents,
     * les caractères spéciaux et en réduisant les espaces multiples. Ensuite, elle parcourt
     * tous les facteurs définis dans {@link WordsFactorDiabetes} et vérifie leur présence
     * dans le texte normalisé.
     * </p>
     * <p>
     * Pour gérer les variations linguistiques, le calcul utilise un "radical" du mot-clé
     * en supprimant les terminaisons simples (pluriel ou féminin) et applique une expression
     * régulière pour matcher les formes singulier/pluriel et masculin/féminin correspondantes.
     * Par exemple, un facteur "vertiges" pourra matcher "vertige" dans une note.
     * </p>
     * <p>
     * Chaque facteur n'est compté qu'une seule fois même s'il apparaît plusieurs fois dans les notes.
     * </p>
     *
     * @param notes la liste des {@link NoteResponseDTO} du patient à analyser
     * @return le nombre total de facteurs de risque présents dans les notes
     */
    private int countWordFactorsInNotes(List<NoteResponseDTO> notes) {
        int count = 0;

        String normalizedText = normalizeText(fusionAllPatientNotes(notes));
        List<String> values = Arrays.stream(WordsFactorDiabetes.values()).map(WordsFactorDiabetes::getValue).toList();

        for (String value : values) {
            if (normalizedText.contains(value.toLowerCase())) {
                count++;
            }
        }

        return count;
    }


    // 3 méthodes pour la logique conditionnelle
    private LevelRiskOfDiabetes levelOfRisk(int age, int score, Gender genre) {
        if (score <= 0) {
            return com.project.common.model.LevelRiskOfDiabetes.None;
        }

        boolean ageMoreThan30 = age > 30;
        boolean isMale = genre == Gender.M;

        if (ageMoreThan30) {
            return levelOfRiskForAgeMoreThan30(score);
        } else {
            return levelOfRiskForAgeLessThan30(score, isMale);
        }
    }


    private LevelRiskOfDiabetes levelOfRiskForAgeMoreThan30(int score) {
        if (score >= 2 && score <= 5) {
            return LevelRiskOfDiabetes.Borderline;
        }
        if (score > 5 && score <= 7) {
            return LevelRiskOfDiabetes.InDanger;
        }
        if (score > 7) {
            return LevelRiskOfDiabetes.EarlyOnSet;
        }
        return LevelRiskOfDiabetes.None;
    }

    private LevelRiskOfDiabetes levelOfRiskForAgeLessThan30(int score, boolean isMale) {
        if (isMale) {
            if(score == 3 || score == 4) {
                return LevelRiskOfDiabetes.InDanger;
            } else if (score >= 5) {
                return LevelRiskOfDiabetes.EarlyOnSet;
            }
        } else {
            if(score >= 4 && score <= 6) {
                return LevelRiskOfDiabetes.InDanger;
            } else if(score >= 7) {
                return LevelRiskOfDiabetes.EarlyOnSet;
            }
        }
        return LevelRiskOfDiabetes.None;
    }

}


