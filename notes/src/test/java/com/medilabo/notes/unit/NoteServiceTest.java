package com.medilabo.notes.unit;

import com.medilabo.notes.dao.NoteRepository;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.model.NoteProjection;
import com.medilabo.notes.service.NoteServiceImpl;
import com.project.common.dto.NoteRequestDTO;
import com.project.common.dto.NoteResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteServiceImpl noteService;

    private NoteRequestDTO validDto;

    @BeforeEach
    void setup() {
        validDto = new NoteRequestDTO(1L, "Test Patient", "Ceci est une note");
    }

    // --- SAVE TESTS ------------------------------------------------------------

    @Test
    void saveNote_shouldSaveSuccessfully() {
        // Act
        noteService.saveNote(validDto);

        // Assert
        verify(noteRepository, times(1)).save(any(Note.class));
    }

    @Test
    void saveNote_shouldThrowException_whenDtoIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> noteService.saveNote(null));

        assertEquals("Note request dto must not be null", exception.getMessage());
        verifyNoInteractions(noteRepository);
    }

    // --- GET TESTS ------------------------------------------------------------

    @Test
    void getNoteAndDateByPatientId_shouldReturnMappedDTOs() {
        // Arrange
        NoteProjection note1 = mock(NoteProjection.class);
        when(note1.getNote()).thenReturn("Note 1");
        when(note1.getCreationDate()).thenReturn(LocalDate.of(2024, 1, 1));

        NoteProjection note2 = mock(NoteProjection.class);
        when(note2.getNote()).thenReturn("Note 2");
        when(note2.getCreationDate()).thenReturn(LocalDate.of(2023, 12, 1));

        when(noteRepository.findNoteAndDateByPatientIdOrderByDateDesc(1L))
                .thenReturn(List.of(note1, note2));

        // Act
        List<NoteResponseDTO> result = noteService.getNoteAndDateByPatientId(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals("Note 1", result.getFirst().note());
        assertEquals(LocalDate.of(2024, 1, 1), result.getFirst().time());
        verify(noteRepository, times(1)).findNoteAndDateByPatientIdOrderByDateDesc(1L);
    }

    @Test
    void getNoteAndDateByPatientId_shouldThrowException_whenIdIsNull() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> noteService.getNoteAndDateByPatientId(null));

        assertEquals("Patient id must not be null", exception.getMessage());
        verifyNoInteractions(noteRepository);
    }

    @Test
    void getNoteAndDateByPatientId_shouldThrowException_whenIdIsNegative() {
        // Act + Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> noteService.getNoteAndDateByPatientId(-5L));

        assertEquals("Patient id must be greater than 0", exception.getMessage());
        verifyNoInteractions(noteRepository);
    }

    @Test
    void getNoteAndDateByPatientId_shouldReturnEmptyList_whenNoNotesFound() {
        when(noteRepository.findNoteAndDateByPatientIdOrderByDateDesc(1L))
                .thenReturn(List.of());

        List<NoteResponseDTO> result = noteService.getNoteAndDateByPatientId(1L);

        assertTrue(result.isEmpty());
        verify(noteRepository, times(1)).findNoteAndDateByPatientIdOrderByDateDesc(1L);
    }
}
