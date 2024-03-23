package me.dhassan.infrastructure.mapper;

import me.dhassan.domain.entity.Note;
import me.dhassan.infrastructure.entity.NoteEntity;
import me.dhassan.infrastructure.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public class NoteMapper {
    public static Note mapToNoteModel(NoteEntity noteEntity) {
        Note note = new Note(noteEntity.title, noteEntity.content);
        note.id = noteEntity.id;

        return note;
    }

    public static NoteEntity mapToNoteEntity(Note note, UserEntity userEntity) {
        NoteEntity noteEntity = new NoteEntity(note.title, note.content, userEntity);
        noteEntity.id = note.id;

        return noteEntity;
    }

    public static List<Note> mapNoteEntitiesToNoteModels(List<NoteEntity> noteEntities) {
        return noteEntities.stream()
                .map(NoteMapper::mapToNoteModel)
                .collect(Collectors.toList());
    }
}
