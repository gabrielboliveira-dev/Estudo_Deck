package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteFlashcardUseCase {

    private final DeckRepository deckRepository;
    private final UserContext userContext;

    public DeleteFlashcardUseCase(DeckRepository deckRepository, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.userContext = userContext;
    }

    public void execute(Input input) {
        UUID userId = userContext.getAuthenticatedUserId();
        Deck deck = deckRepository.findByIdAndUserId(input.deckId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found"));

        deck.removeCard(input.flashcardId());

        deckRepository.save(deck);
    }

    public record Input(UUID deckId, UUID flashcardId) {}
}
