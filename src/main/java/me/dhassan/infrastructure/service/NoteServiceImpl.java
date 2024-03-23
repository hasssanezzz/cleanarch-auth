package me.dhassan.infrastructure.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import me.dhassan.domain.entity.Note;
import me.dhassan.domain.service.NoteService;
import me.dhassan.infrastructure.entity.NoteEntity;
import me.dhassan.infrastructure.entity.UserEntity;
import me.dhassan.infrastructure.mapper.NoteMapper;

import java.util.List;

@ApplicationScoped
public class NoteServiceImpl implements NoteService {
    @Inject
    EntityManager entityManager;

    @Transactional
    public <IDType> Note createNote(Note note, IDType userId) {
        UserEntity user = entityManager.find(UserEntity.class, userId);

        if (user == null) return null;

        NoteEntity noteEntity = NoteMapper.mapToNoteEntity(note, user);
        entityManager.persist(noteEntity);

        return NoteMapper.mapToNoteModel(noteEntity);
    }

    @Transactional
    public <IDType> List<Note> getUserNotes(IDType userId) {
        // TODO remove user password
        String jpql = "SELECT n FROM note n WHERE n.userEntity.id = :userId";
        TypedQuery<NoteEntity> query = entityManager.createQuery(jpql, NoteEntity.class);
        query.setParameter("userId", userId);

        return NoteMapper.mapNoteEntitiesToNoteModels(
                query.getResultList()
        );
    }

    @Transactional
    public <IDType> Note getNoteById(IDType id) {
        return NoteMapper.mapToNoteModel(entityManager.find(NoteEntity.class, id));
    }



    @Transactional
    public <IDType> Note updateNoteById(IDType id, Note note) {
        NoteEntity noteEntity = entityManager.find(NoteEntity.class, id);

        if (noteEntity == null)
            return null;

        noteEntity.title = note.title == null ? noteEntity.title : note.title;
        noteEntity.content = note.content == null ? noteEntity.content : note.content;

        entityManager.merge(noteEntity);

        return NoteMapper.mapToNoteModel(noteEntity);
    }


    @Transactional
    public <IDType> boolean deleteNoteById(IDType id) {
        NoteEntity noteEntity = entityManager.find(NoteEntity.class, id);

        if (noteEntity == null)
            return false;

        entityManager.remove(noteEntity);

        return true;
    }
}
