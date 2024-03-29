package me.dhassan.infrastructure.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import me.dhassan.domain.entity.Note;
import me.dhassan.domain.entity.User;
import me.dhassan.infrastructure.entity.NoteEntity;
import me.dhassan.infrastructure.entity.UserEntity;
import me.dhassan.infrastructure.repository.UserRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class NoteMapper {

    @Inject
    UserRepositoryImpl userRepository;

    @Inject
    UserMapper userMapper;


    public Note mapToNoteModel(NoteEntity noteEntity) {
        return new Note(noteEntity.id, noteEntity.userEntity.id, noteEntity.title, noteEntity.content);
    }

    public NoteEntity mapToNoteEntity(Note note) {
        User user = userRepository.findById(note.userId);
        NoteEntity noteEntity = new NoteEntity(note.title, note.content, userMapper.mapToUserEntity(user));

        if (note.id != null)
            noteEntity.id = note.id;

        return noteEntity;
    }

    public List<Note> mapNoteEntitiesToNoteModels(List<NoteEntity> noteEntities) {
        return noteEntities.stream()
                .map(this::mapToNoteModel)
                .collect(Collectors.toList());
    }
}
