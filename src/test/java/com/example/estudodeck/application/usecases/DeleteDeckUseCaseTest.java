package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteDeckUseCaseTest {

    @InjectMocks
    private DeleteDeckUseCase deleteDeckUseCase;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @Test
    void execute_ShouldDeleteDeck() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        DeleteDeckUseCase.Input input = new DeleteDeckUseCase.Input(deckId);

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.of(mock(Deck.class)));

        // When
        deleteDeckUseCase.execute(input);

        // Then
        verify(deckRepository).deleteByIdAndUserId(deckId, userId);
    }

    @Test
    void execute_WhenDeckNotFound_ShouldThrowException() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        DeleteDeckUseCase.Input input = new DeleteDeckUseCase.Input(deckId);

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> deleteDeckUseCase.execute(input));
        verify(deckRepository, never()).deleteByIdAndUserId(any(), any());
    }
}
