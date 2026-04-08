package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Tag;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RemoveTagFromDeckUseCaseTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private RemoveTagFromDeckUseCase useCase;

    @Test
    void shouldRemoveTagFromDeck() {
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        Deck mockDeck = mock(Deck.class);
        Tag mockTag = mock(Tag.class);

        when(mockTag.getName()).thenReturn("java");
        when(mockDeck.getTags()).thenReturn(Set.of(mockTag));
        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.of(mockDeck));

        useCase.execute(new RemoveTagFromDeckUseCase.Input(deckId, "java"));

        verify(mockDeck).removeTag(mockTag);
        verify(deckRepository).save(mockDeck);
    }
}