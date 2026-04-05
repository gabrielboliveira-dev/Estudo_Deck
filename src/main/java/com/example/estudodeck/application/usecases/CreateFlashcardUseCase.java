package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.CardContent;
import com.example.estudodeck.domain.CardType;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Flashcard;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public class CreateFlashcardUseCase {

    private final DeckRepository deckRepository;
    private final UserContext userContext;

    public CreateFlashcardUseCase(DeckRepository deckRepository, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.userContext = userContext;
    }

    public Flashcard execute(Input input) {
        UUID userId = userContext.getAuthenticatedUserId();
        Deck deck = deckRepository.findByIdAndUserId(input.deckId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found"));

        CardContent content = CardContentFactory.create(input.type(), input.contentFields());

        Flashcard newFlashcard = Flashcard.createNew(content);
        deck.addCard(newFlashcard);

        deckRepository.save(deck);

        return newFlashcard;
    }

    public record Input(
            UUID deckId,
            CardType type,
            Map<String, String> contentFields
    ) {}
}
