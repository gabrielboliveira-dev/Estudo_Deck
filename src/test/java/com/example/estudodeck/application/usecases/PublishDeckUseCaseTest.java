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
class PublishDeckUseCaseTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private PublishDeckUseCase useCase;

    @Test
    void shouldPublishDeck() {
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        Deck mockDeck = mock(Deck.class);

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.of(mockDeck));

        useCase.execute(new PublishDeckUseCase.Input(deckId));

        verify(mockDeck).publish();
        verify(deckRepository).save(mockDeck);
    }

    @Test
    void shouldThrowWhenDeckNotFound() {
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                useCase.execute(new PublishDeckUseCase.Input(deckId))
        );
    }
}