package me.dhassan.infrastructure.entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "tag")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID id;

    @Column(name="title", unique = true)
    public String title;

    public TagEntity() {}

    public TagEntity(String title) {
        this.title = title;
    }
}
