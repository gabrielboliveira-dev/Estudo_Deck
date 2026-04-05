package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Tag;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RemoveTagFromDeckUseCase {

    private final DeckRepository deckRepository;
    private final UserContext userContext;

    public RemoveTagFromDeckUseCase(DeckRepository deckRepository, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.userContext = userContext;
    }

    public void execute(Input input) {
        UUID userId = userContext.getAuthenticatedUserId();
        Deck deck = deckRepository.findByIdAndUserId(input.deckId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found"));

        Tag tagToRemove = deck.getTags().stream()
                .filter(tag -> tag.getName().equals(input.tagName().trim().toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found on this deck"));

        deck.removeTag(tagToRemove);

        deckRepository.save(deck);
    }

    public record Input(UUID deckId, String tagName) {}
}
