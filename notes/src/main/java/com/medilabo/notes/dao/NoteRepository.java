package com.medilabo.notes.dao;

import com.medilabo.notes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    List<Note> findByPatientIdOrderByDateDesc(Long patientId);
}
