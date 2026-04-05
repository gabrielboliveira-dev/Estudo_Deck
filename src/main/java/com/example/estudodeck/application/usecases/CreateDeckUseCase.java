package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateDeckUseCase {

    private final DeckRepository repository;
    private final UserContext userContext;

    public CreateDeckUseCase(DeckRepository repository, UserContext userContext) {
        this.repository = repository;
        this.userContext = userContext;
    }

    public Deck execute(Input input) {
        UUID userId = userContext.getAuthenticatedUserId();
        Deck newDeck = Deck.create(input.name(), input.parentId(), userId);
        return repository.save(newDeck);
    }

    public record Input(String name, UUID parentId) {}
}
