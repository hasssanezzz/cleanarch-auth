package me.dhassan.api.resources;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import me.dhassan.domain.entity.Note;
import me.dhassan.infrastructure.service.NoteServiceImpl;
import me.dhassan.infrastructure.service.UserServiceImpl;
import me.dhassan.api.contexts.SecurityContext;
import me.dhassan.api.interceptors.Authenticated;

import java.util.List;
import java.util.UUID;

@Path("/api/notes")
public class NoteResource {

    @Inject
    SecurityContext securityContext;

    @Inject
    NoteServiceImpl noteServiceImpl;

    @Inject
    UserServiceImpl userServiceImpl;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response getUserNotes() {
        List<Note> notes = noteServiceImpl.getUserNotes(securityContext.userId);
        return Response.ok().entity(notes).build();
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response createNote(Note note) {

        // TODO handle validation

        Note newNote = noteServiceImpl.createNote(note, securityContext.userId);

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

        // TODO handle validation

        Note updatedNote = noteServiceImpl.updateNoteById(noteId, note);
        return Response.ok().entity(updatedNote).build();
    }


    @DELETE
    @Path("/{note_id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Authenticated
    public Response deleteNoteById(@PathParam("note_id") UUID noteId) {
        noteServiceImpl.deleteNoteById(noteId);
        return Response.noContent().build();
    }

}
