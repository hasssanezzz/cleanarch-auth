package me.dhassan.api.resources;


import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dhassan.api.responseObjects.ErrorResponse;
import me.dhassan.domain.entity.Note;
import me.dhassan.infrastructure.entity.NoteEntity;
import me.dhassan.infrastructure.mapper.NoteMapper;
import me.dhassan.infrastructure.usecases.NoteUseCasesImpl;
import me.dhassan.api.contexts.SecurityContext;
import me.dhassan.api.interceptors.Authenticated;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Path("/api/notes")
public class NoteResource {

    @Inject
    SecurityContext securityContext;

    @Inject
    NoteUseCasesImpl noteService;

    @Inject
    NoteMapper noteMapper;

    @Inject
    Validator validator;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getUserNotes() {
        List<Note> notes = noteService.getUserNotes(securityContext.userId);
        return Response.ok().entity(notes).build();
    }

    @GET
    @Path("/{note_id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getNoteBId(@PathParam("note_id") UUID noteId) {
        // TODO Ask: what to do about security here

        Note note = noteService.getNoteById(noteId);

        if (note == null || !note.userId.equals(securityContext.userId))
            return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.ok().entity(note).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response createNote(Note note) {

        note.userId = securityContext.userId;

        // TODO fix the validator.validate(obj) problem
        // handle validation
        Set<ConstraintViolation<NoteEntity>> violations = validator.validate(noteMapper.mapToNoteEntity(note));
        ErrorResponse errors = new ErrorResponse();
        if (!violations.isEmpty()) {
            for (ConstraintViolation<NoteEntity> violation : violations) {
                errors.addError(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errors)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        Note newNote = noteService.createNote(note);

        if (newNote == null)
            return Response.status(Response.Status.BAD_REQUEST).build();

        return Response.ok().entity(newNote).build();
    }

    @PUT
    @Path("/{note_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response updateNoteById(@PathParam("note_id") UUID noteId, Note note) {

        note.userId = securityContext.userId;


        // TODO fix the validator.validate(obj) problem
        // handle validation
        Set<ConstraintViolation<NoteEntity>> violations = validator.validate(noteMapper.mapToNoteEntity(note));
        ErrorResponse errors = new ErrorResponse();
        if (!violations.isEmpty()) {
            for (ConstraintViolation<NoteEntity> violation : violations) {
                errors.addError(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(errors)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        Note updatedNote = noteService.updateNoteById(noteId, note);
        return Response.ok().entity(updatedNote).build();
    }


    @DELETE
    @Path("/{note_id}")
    @Transactional
    @Authenticated
    public Response deleteNoteById(@PathParam("note_id") UUID noteId) {
        Note note = noteService.getNoteById(noteId);

        // make sure not exists
        if (note == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // make sure the user owns the note
        if (!note.userId.equals(securityContext.userId)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        noteService.deleteNoteById(noteId);
        return Response.noContent().build();
    }

}
