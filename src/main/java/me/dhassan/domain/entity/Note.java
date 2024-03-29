package me.dhassan.domain.entity;

import java.util.UUID;

public class Note {
    public UUID id;
    public UUID userId;
    public String title;
    public String content;

    public Note(UUID id, UUID userId, String title, String content) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
    }
}
