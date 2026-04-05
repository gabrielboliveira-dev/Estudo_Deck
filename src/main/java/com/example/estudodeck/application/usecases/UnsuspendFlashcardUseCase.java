package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Flashcard;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UnsuspendFlashcardUseCase {

    private final DeckRepository deckRepository;
    private final UserContext userContext;

    public UnsuspendFlashcardUseCase(DeckRepository deckRepository, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.userContext = userContext;
    }

    public void execute(Input input) {
        UUID userId = userContext.getAuthenticatedUserId();
        Deck deck = deckRepository.findByIdAndUserId(input.deckId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found"));

        Flashcard flashcard = deck.getCards().stream()
                .filter(f -> f.getId().equals(input.flashcardId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found"));

        flashcard.unsuspend();

        deckRepository.save(deck);
    }

    public record Input(UUID deckId, UUID flashcardId) {}
}
