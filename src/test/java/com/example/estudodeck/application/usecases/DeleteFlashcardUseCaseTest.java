package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.BasicCardContent;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Flashcard;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteFlashcardUseCaseTest {

    @InjectMocks
    private DeleteFlashcardUseCase deleteFlashcardUseCase;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @Test
    void execute_ShouldRemoveFlashcardAndSave() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        
        Deck mockDeck = Deck.create("Test Deck", null, userId);
        Flashcard flashcard = Flashcard.createNew(new BasicCardContent("Q", "A"));
        mockDeck.addCard(flashcard);

        DeleteFlashcardUseCase.Input input = new DeleteFlashcardUseCase.Input(deckId, flashcard.getId());

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.of(mockDeck));

        // When
        deleteFlashcardUseCase.execute(input);

        // Then
        verify(deckRepository).save(mockDeck);
        assertTrue(mockDeck.getCards().isEmpty());
    }

    @Test
    void execute_WhenDeckNotFound_ShouldThrowException() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        UUID flashcardId = UUID.randomUUID();
        DeleteFlashcardUseCase.Input input = new DeleteFlashcardUseCase.Input(deckId, flashcardId);

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> deleteFlashcardUseCase.execute(input));
        verify(deckRepository, never()).save(any());
    }
}
