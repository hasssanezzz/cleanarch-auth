package me.dhassan.infrastructure.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import me.dhassan.domain.entity.Note;
import me.dhassan.domain.entity.User;
import me.dhassan.domain.repository.NoteRepository;
import me.dhassan.infrastructure.entity.NoteEntity;
import me.dhassan.infrastructure.mapper.NoteMapper;

import java.util.List;


@ApplicationScoped
public class NoteRepositoryImpl implements NoteRepository {

    @Inject
    NoteMapper noteMapper;

    @Inject
    EntityManager entityManager;

    public <IDType> Note findById(IDType id) {
        return noteMapper.mapToNoteModel(entityManager.find(NoteEntity.class, id));
    }

    public List<Note> findAll(User user) {
        // TODO remove user password
        String jpql = "SELECT n FROM note n WHERE n.userEntity.id = :userId";
        TypedQuery<NoteEntity> query = entityManager.createQuery(jpql, NoteEntity.class);
        query.setParameter("userId", user.id);

        return noteMapper.mapNoteEntitiesToNoteModels(
                query.getResultList()
        );
    }

    @Transactional
    public void save(Note note) {
        NoteEntity noteEntity = noteMapper.mapToNoteEntity(note);

        System.out.println("\n ======================================== \n");
        System.out.println(noteEntity.id);
        System.out.println(noteEntity.title);
        System.out.println(noteEntity.userEntity.email);
        System.out.println("\n ======================================== \n");

        entityManager.persist(noteEntity);
    }

    @Transactional
    public void update(Note note) {
        NoteEntity noteEntity = entityManager.find(NoteEntity.class, note.id);

        noteEntity.title = note.title == null ? noteEntity.title : note.title;
        noteEntity.content = note.content == null ? noteEntity.content : note.content;

        entityManager.merge(noteEntity);
    }


    @Transactional
    public <IDType> void delete(IDType noteId) {
        NoteEntity noteEntity = entityManager.find(NoteEntity.class, noteId);

        System.out.println("Entity State before removal: " + entityManager.contains(noteEntity)); // Should return true
        entityManager.remove(noteEntity);
        System.out.println("Entity State after removal: " + entityManager.contains(noteEntity)); // Should return false after removal
    }
}
