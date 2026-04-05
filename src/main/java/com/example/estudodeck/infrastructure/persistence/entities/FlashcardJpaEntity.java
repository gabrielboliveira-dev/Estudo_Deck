package com.example.estudodeck.infrastructure.persistence.entities;

import com.example.estudodeck.domain.CardType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@SqlResultSetMapping(
    name = "SearchResultMapping",
    columns = {
        @ColumnResult(name = "flashcardId", type = UUID.class),
        @ColumnResult(name = "prompt", type = String.class),
        @ColumnResult(name = "deckId", type = UUID.class),
        @ColumnResult(name = "deckName", type = String.class)
    }
)
@Entity
@Table(name = "flashcards")
@Getter
@Setter
public class FlashcardJpaEntity {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Column(columnDefinition = "TEXT")
    private String question; // Used for BASIC

    @Column(columnDefinition = "TEXT")
    private String answer; // Used for BASIC

    @Column(columnDefinition = "TEXT")
    private String text; // Used for CLOZE

    private Integer repetitions;
    private Double easinessFactor;
    private Integer intervalInDays;
    private LocalDateTime nextDueDate;
    private Integer leechCount;
    private Boolean suspended;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id")
    private DeckJpaEntity deck;
}
