package com.example.estudodeck.domain;

public interface CardContent {
    String getPrompt();
    String getFullContent();
    CardType getType();
}
