package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Flashcard;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnsuspendFlashcardUseCaseTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private UnsuspendFlashcardUseCase useCase;

    @Test
    void shouldUnsuspendCard() {
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        UUID flashcardId = UUID.randomUUID();

        Deck mockDeck = mock(Deck.class);
        Flashcard mockCard = mock(Flashcard.class);

        when(mockCard.getId()).thenReturn(flashcardId);
        when(mockDeck.getCards()).thenReturn(List.of(mockCard));
        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.of(mockDeck));

        useCase.execute(new UnsuspendFlashcardUseCase.Input(deckId, flashcardId));

        verify(mockCard).unsuspend();
        verify(deckRepository).save(mockDeck);
    }
}