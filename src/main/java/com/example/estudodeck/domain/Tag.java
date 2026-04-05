package com.example.estudodeck.domain;

import lombok.Data;
import java.util.UUID;

@Data
public class Tag {
    private final UUID id;
    private final String name;

    public Tag(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tag name cannot be blank.");
        }
        this.id = UUID.randomUUID();
        this.name = name.trim().toLowerCase();
    }

    public Tag(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
