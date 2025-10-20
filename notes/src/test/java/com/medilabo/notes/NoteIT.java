package com.medilabo.notes;

import com.medilabo.notes.dao.NoteRepository;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.model.NoteProjection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class NoteIT {

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0").withExposedPorts(27017);

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setup() {
        noteRepository.deleteAll();
    }


    @Test
    void ShouldSaveAndRetrieveNote() {
        Note note = new Note(1L, "John Doe", "Première note test");
        noteRepository.save(note);

        List<NoteProjection> result = noteRepository.findNoteAndDateByPatientIdOrderByDateDesc(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getNote()).isEqualTo("Première note test");
        assertThat(result.getFirst().getDate()).isBeforeOrEqualTo(LocalDate.now());
    }

    @Test
    void shouldSaveNoteViaControllerAndRetrieveIt() throws Exception {
        // given
        String noteJson = """
                {
                  "patientID": 2,
                  "patientName": "Jane Doe",
                  "note": "Contrôle annuel OK"
                }
                """;

        // when -> création
        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(noteJson))
                .andExpect(status().isCreated());

        // then -> récupération
        mockMvc.perform(get("/notes/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].note").value("Contrôle annuel OK"));
    }


}
