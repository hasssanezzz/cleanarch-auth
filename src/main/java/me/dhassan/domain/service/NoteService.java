package me.dhassan.domain.service;

import me.dhassan.domain.entity.Note;

import java.util.List;

public interface NoteService {

    <IDType> Note createNote(Note note, IDType userId);

    <IDType> List<Note> getUserNotes(IDType userId);

    <IDType> Note getNoteById(IDType id);

    <IDType> Note updateNoteById(IDType id, Note updatedNoteEntity);

    <IDType> boolean deleteNoteById(IDType id);
}
