package me.dhassan.domain.service;

import me.dhassan.domain.entity.Note;

import java.util.List;

public interface NoteUseCases {

    <IDType> Note createNote(Note note);

    <IDType> List<Note> getUserNotes(IDType userId);

    <IDType> Note getNoteById(IDType id);

    <IDType> Note updateNoteById(IDType id, Note note);

    <IDType> void deleteNoteById(IDType id);
}
