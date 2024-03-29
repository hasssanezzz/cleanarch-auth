package me.dhassan.infrastructure.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "tag")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(name="title", unique = true)
    public String title;


    @ManyToMany(mappedBy = "tags")
    private Set<NoteEntity> notes = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore // TODO - fix the recursive JSON problem
    public UserEntity userEntity;

    public TagEntity() {}

    public TagEntity(String title) {
        this.title = title;
    }
}
