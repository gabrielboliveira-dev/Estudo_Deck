package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.application.gateways.ReviewOutcomeGateway;
import com.example.estudodeck.domain.BasicCardContent;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Flashcard;
import com.example.estudodeck.domain.LevelingSystem;
import com.example.estudodeck.infrastructure.persistence.entities.UserJpaEntity; // Import UserJpaEntity
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataUserRepository;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewFlashcardUseCaseTest {

    @InjectMocks
    private ReviewFlashcardUseCase reviewFlashcardUseCase;

    @Mock
    private DeckRepository deckRepository;
    @Mock
    private CreateReviewLogUseCase createReviewLogUseCase;
    @Mock
    private CreateReviewOutcomeUseCase createReviewOutcomeUseCase;
    @Mock
    private UserContext userContext;
    @Mock
    private SpringDataUserRepository userRepository;
    @Mock
    private LevelingSystem levelingSystem;

    // Mock User ID for testing purposes
    private final UUID MOCK_USER_ID = UUID.randomUUID();

    @Test
    public void shouldReviewFlashcard() {
        // Given
        UUID deckId = UUID.randomUUID();
        int quality = 4;

        Deck deck = Deck.create("Test Deck", null, MOCK_USER_ID);
        Flashcard flashcard = Flashcard.createNew(new BasicCardContent("Question", "Answer"));
        deck.addCard(flashcard);

        when(userContext.getAuthenticatedUserId()).thenReturn(MOCK_USER_ID);
        when(deckRepository.findByIdAndUserId(deckId, MOCK_USER_ID)).thenReturn(Optional.of(deck));
        when(userContext.getAuthenticatedUser()).thenReturn(new UserJpaEntity()); // Mock user for XP logic

        // When
        reviewFlashcardUseCase.execute(new ReviewFlashcardUseCase.Input(deckId, flashcard.getId(), quality));

        // Then
        verify(deckRepository).save(deck);
        verify(createReviewLogUseCase).execute();
        verify(createReviewOutcomeUseCase).execute(any(CreateReviewOutcomeUseCase.Input.class));
        verify(userRepository).save(any(UserJpaEntity.class));
    }
}
