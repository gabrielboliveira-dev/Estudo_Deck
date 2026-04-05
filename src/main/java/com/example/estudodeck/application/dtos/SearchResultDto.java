package com.example.estudodeck.application.dtos;

import lombok.Data;
import java.util.UUID;

@Data
public class SearchResultDto {
    private final UUID flashcardId;
    private final String prompt;
    private final UUID deckId;
    private final String deckName;
}
