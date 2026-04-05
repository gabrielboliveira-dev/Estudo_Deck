package com.example.estudodeck.domain;

import lombok.Getter;
import java.util.UUID;

@Getter
public class User {
    private final UUID id;
    private final String email;
    private final String password;
    private long xp;
    private int level;

    private User(String email, String password) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.xp = 0;
        this.level = 1;
    }

    public static User create(String email, String password) {
        // Validation for email and password would go here
        return new User(email, password);
    }

    public void addXp(long xp) {
        this.xp += xp;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
