package me.dhassan.infrastructure.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    public String name;

    @Column(name="email", unique = true)
    public String email;


    @NotBlank(message = "title may not be blank")
    @Size(min = 8, max = 512, message = "password must be from 3 to 512 characters")
    public String password;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval = true)
    public List<NoteEntity> noteEntities = new ArrayList<>();

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, orphanRemoval = true)
    public List<NoteEntity> tagEntities = new ArrayList<>();

    public UserEntity() {}

    public UserEntity(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
