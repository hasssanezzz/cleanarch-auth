package me.dhassan.domain.repository;

import me.dhassan.domain.entity.Note;
import me.dhassan.domain.entity.User;

import java.util.List;

public interface NoteRepository {
    <IDType> Note findById(IDType id);
    List<Note> findAll(User user);
    void save(Note note);
    void update(Note note);
    <IDType> void delete(IDType id);
}
