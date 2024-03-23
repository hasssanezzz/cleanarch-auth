package me.dhassan.domain.entity;

import java.util.List;
import java.util.UUID;

public class User {
    public UUID id;
    public String name;
    public String email;
    public String password;
    public List<Note> notes;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
