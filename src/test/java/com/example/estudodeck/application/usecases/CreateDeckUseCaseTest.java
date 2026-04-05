package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateDeckUseCaseTest {

    @InjectMocks
    private CreateDeckUseCase createDeckUseCase;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    // Mock User ID for testing purposes
    private final UUID MOCK_USER_ID = UUID.randomUUID();

    @Test
    public void shouldCreateDeck() {
        // Given
        String deckName = "Test Deck";
        CreateDeckUseCase.Input input = new CreateDeckUseCase.Input(deckName, null); // Use Input object

        when(userContext.getAuthenticatedUserId()).thenReturn(MOCK_USER_ID);
        Deck deck = Deck.create(deckName, null, MOCK_USER_ID); // Pass userId
        when(deckRepository.save(any(Deck.class))).thenReturn(deck);

        // When
        Deck createdDeck = createDeckUseCase.execute(input); // Use Input object

        // Then
        assertEquals(deckName, createdDeck.getName());
    }
}
