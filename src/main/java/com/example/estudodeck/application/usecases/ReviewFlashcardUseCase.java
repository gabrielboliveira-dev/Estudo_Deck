package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Flashcard;
import com.example.estudodeck.domain.LevelingSystem;
import com.example.estudodeck.infrastructure.persistence.entities.UserJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataUserRepository;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewFlashcardUseCase {

    private final DeckRepository deckRepository;
    private final CreateReviewLogUseCase createReviewLogUseCase;
    private final CreateReviewOutcomeUseCase createReviewOutcomeUseCase;
    private final UserContext userContext;
    private final SpringDataUserRepository userRepository;
    private final LevelingSystem levelingSystem;

    public ReviewFlashcardUseCase(DeckRepository deckRepository, CreateReviewLogUseCase createReviewLogUseCase, CreateReviewOutcomeUseCase createReviewOutcomeUseCase, UserContext userContext, SpringDataUserRepository userRepository, LevelingSystem levelingSystem) {
        this.deckRepository = deckRepository;
        this.createReviewLogUseCase = createReviewLogUseCase;
        this.createReviewOutcomeUseCase = createReviewOutcomeUseCase;
        this.userContext = userContext;
        this.userRepository = userRepository;
        this.levelingSystem = levelingSystem;
    }

    public void execute(Input input) {
        UUID userId = userContext.getAuthenticatedUserId();
        Deck deck = deckRepository.findByIdAndUserId(input.deckId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found"));

        Flashcard flashcard = deck.getCards().stream()
                .filter(f -> f.getId().equals(input.flashcardId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard not found"));

        var maturity = flashcard.getMaturity();
        boolean success = input.quality() >= 3;

        flashcard.processReview(input.quality());
        deckRepository.save(deck);

        createReviewLogUseCase.execute();
        createReviewOutcomeUseCase.execute(new CreateReviewOutcomeUseCase.Input(flashcard.getId(), userId, success, maturity));

        // Grant XP
        UserJpaEntity user = userContext.getAuthenticatedUser();
        int xpGained = levelingSystem.calculateXpFromReview(input.quality(), maturity);
        user.setXp(user.getXp() + xpGained);
        user.setLevel(levelingSystem.calculateLevel(user.getXp()));
        userRepository.save(user);
    }

    public record Input(UUID deckId, UUID flashcardId, int quality) {}
}
