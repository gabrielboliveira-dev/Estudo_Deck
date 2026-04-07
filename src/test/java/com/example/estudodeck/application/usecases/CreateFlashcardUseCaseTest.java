package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.CardType;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Flashcard;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateFlashcardUseCaseTest {

    @InjectMocks
    private CreateFlashcardUseCase createFlashcardUseCase;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @Test
    void execute_ShouldCreateFlashcardAndSave() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        CreateFlashcardUseCase.Input input = new CreateFlashcardUseCase.Input(
                deckId,
                CardType.BASIC,
                Map.of("question", "Q1", "answer", "A1")
        );

        Deck mockDeck = Deck.create("Test Deck", null, userId);

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.of(mockDeck));

        // When
        Flashcard result = createFlashcardUseCase.execute(input);

        // Then
        assertNotNull(result);
        assertEquals(CardType.BASIC, result.getContent().getType());
        verify(deckRepository).save(mockDeck);
        assertTrue(mockDeck.getCards().contains(result));
    }

    @Test
    void execute_WhenDeckNotFound_ShouldThrowException() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        CreateFlashcardUseCase.Input input = new CreateFlashcardUseCase.Input(
                deckId,
                CardType.BASIC,
                Map.of("question", "Q1", "answer", "A1")
        );

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> createFlashcardUseCase.execute(input));
        verify(deckRepository, never()).save(any());
    }
}
