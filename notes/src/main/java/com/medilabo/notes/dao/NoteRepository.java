package com.medilabo.notes.dao;

import com.medilabo.notes.model.Note;
import com.medilabo.notes.model.NoteProjection;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    @Query(value = "{'patientId' : ?0}", fields = "{'patId' : 1, 'date' : 1, 'note' : 1}", sort = "{'date' : -1}")
    List<NoteProjection> findNoteAndDateByPatientIdOrderByDateDesc(Long patientId);
}

