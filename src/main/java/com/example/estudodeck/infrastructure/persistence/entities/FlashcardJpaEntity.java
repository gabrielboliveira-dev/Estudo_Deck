package com.example.estudodeck.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "flashcards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardJpaEntity {

    @Id
    private UUID id;
    private String question;
    private String answer;
    private Integer repetitions;
    private Double easinessFactor;
    private Integer intervalInDays;
    private LocalDateTime nextDueDate;

    @ManyToOne
    @JoinColumn(name = "deck_id")
    private DeckJpaEntity deck;
}