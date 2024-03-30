package me.dhassan.infrastructure.usecases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import me.dhassan.domain.entity.Note;
import me.dhassan.domain.entity.User;
import me.dhassan.domain.usecases.NoteUseCases;
import me.dhassan.infrastructure.repository.NoteRepositoryImpl;
import me.dhassan.infrastructure.repository.UserRepositoryImpl;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class NoteUseCasesImpl implements NoteUseCases {
    @Inject
    NoteRepositoryImpl noteRepository;

    @Inject
    UserRepositoryImpl userRepository;

    @Transactional
    public Note createNote(Note note) {
        noteRepository.save(note);
        return note;
    }

    public <IDType> List<Note> getUserNotes(IDType userId) {
        User user = userRepository.findById(userId);
        return noteRepository.findAll(user);
    }

    public <IDType> Note getNoteById(IDType id) {
        return noteRepository.findById(id);
    }

    @Transactional
    public <IDType> Note updateNoteById(IDType id, Note note) {
        note.id = UUID.fromString(id.toString());
        noteRepository.update(note);
        return note;
    }


    @Transactional
    public <IDType> void deleteNoteById(IDType id) {
        noteRepository.delete(id);
    }
}
