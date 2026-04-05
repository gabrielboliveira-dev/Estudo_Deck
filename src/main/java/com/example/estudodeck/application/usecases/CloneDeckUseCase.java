package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CloneDeckUseCase {

    private final DeckRepository deckRepository;
    private final UserContext userContext;

    public CloneDeckUseCase(DeckRepository deckRepository, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.userContext = userContext;
    }

    public void execute(Input input) {
        UUID newOwnerId = userContext.getAuthenticatedUserId();

        Deck publicDeck = deckRepository.findPublicById(input.deckId())
                .orElseThrow(() -> new ResourceNotFoundException("Public deck not found"));

        Deck clonedDeck = publicDeck.cloneForUser(newOwnerId);
        deckRepository.save(clonedDeck);
    }

    public record Input(UUID deckId) {}
}
