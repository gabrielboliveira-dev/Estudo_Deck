package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteDeckUseCase {

    private final DeckRepository deckRepository;
    private final UserContext userContext;

    public DeleteDeckUseCase(DeckRepository deckRepository, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.userContext = userContext;
    }

    public void execute(Input input) {
        UUID userId = userContext.getAuthenticatedUserId();
        if (!deckRepository.findByIdAndUserId(input.deckId(), userId).isPresent()) {
            throw new ResourceNotFoundException("Deck not found or you don't own this deck");
        }
        deckRepository.deleteByIdAndUserId(input.deckId(), userId);
    }

    public record Input(UUID deckId) {}
}
