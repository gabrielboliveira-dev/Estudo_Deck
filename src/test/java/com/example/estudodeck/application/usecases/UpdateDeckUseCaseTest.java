package com.example.estudodeck.application.usecases;

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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateDeckUseCaseTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private UpdateDeckUseCase useCase;

    @Test
    void shouldUpdateDeckName() {
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        String newName = "Design Patterns Avançado";

        Deck mockDeck = mock(Deck.class);

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.of(mockDeck));

        useCase.execute(new UpdateDeckUseCase.Input(deckId, newName));

        verify(mockDeck).changeName(newName);
        verify(deckRepository).save(mockDeck);
    }
}