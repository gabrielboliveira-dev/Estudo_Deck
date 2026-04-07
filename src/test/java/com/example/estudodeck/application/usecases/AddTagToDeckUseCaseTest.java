package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Tag;
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
public class AddTagToDeckUseCaseTest {

    @InjectMocks
    private AddTagToDeckUseCase addTagToDeckUseCase;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @Test
    void execute_ShouldAddTagToDeckAndSave() {
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        String tagName = "Java";
        AddTagToDeckUseCase.Input input = new AddTagToDeckUseCase.Input(deckId, tagName);

        Deck mockDeck = Deck.create("Test Deck", null, userId);

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.of(mockDeck));

        addTagToDeckUseCase.execute(input);

        ArgumentCaptor<Deck> deckCaptor = ArgumentCaptor.forClass(Deck.class);
        verify(deckRepository).save(deckCaptor.capture());

        Deck savedDeck = deckCaptor.getValue();
        assertEquals(1, savedDeck.getTags().size());
        Tag savedTag = savedDeck.getTags().iterator().next();

        assertEquals(tagName.toLowerCase(), savedTag.getName());
    }

    @Test
    void execute_WhenDeckNotFound_ShouldThrowException() {
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        AddTagToDeckUseCase.Input input = new AddTagToDeckUseCase.Input(deckId, "Tag");

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> addTagToDeckUseCase.execute(input));
        verify(deckRepository, never()).save(any());
    }
}