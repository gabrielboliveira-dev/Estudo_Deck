package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CloneDeckUseCaseTest {

    @InjectMocks
    private CloneDeckUseCase cloneDeckUseCase;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @Test
    void execute_ShouldCloneDeckAndSave() {
        // Given
        UUID newOwnerId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        CloneDeckUseCase.Input input = new CloneDeckUseCase.Input(deckId);

        Deck publicDeck = Deck.create("Public Deck", null, UUID.randomUUID());
        publicDeck.publish();

        when(userContext.getAuthenticatedUserId()).thenReturn(newOwnerId);
        when(deckRepository.findPublicById(deckId)).thenReturn(Optional.of(publicDeck));

        // When
        cloneDeckUseCase.execute(input);

        // Then
        ArgumentCaptor<Deck> deckCaptor = ArgumentCaptor.forClass(Deck.class);
        verify(deckRepository).save(deckCaptor.capture());
        
        Deck clonedDeck = deckCaptor.getValue();
        assertEquals(publicDeck.getName(), clonedDeck.getName());
        assertEquals(newOwnerId, clonedDeck.getUserId());
        assertNotEquals(publicDeck.getId(), clonedDeck.getId());
        assertFalse(clonedDeck.isPublic());
    }

    @Test
    void execute_WhenDeckNotFound_ShouldThrowException() {
        // Given
        UUID newOwnerId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        CloneDeckUseCase.Input input = new CloneDeckUseCase.Input(deckId);

        when(userContext.getAuthenticatedUserId()).thenReturn(newOwnerId);
        when(deckRepository.findPublicById(deckId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> cloneDeckUseCase.execute(input));
        verify(deckRepository, never()).save(any());
    }
}
