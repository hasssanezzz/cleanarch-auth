package me.dhassan.domain.entity;

import java.util.UUID;

public class Note {
    public UUID id;
    public String title;
    public String content;

    public Note(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
