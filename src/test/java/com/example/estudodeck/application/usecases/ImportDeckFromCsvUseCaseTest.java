package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportDeckFromCsvUseCaseTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private ImportDeckFromCsvUseCase useCase;

    @Test
    void shouldParseCsvAndAddCardsToDeck() {
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        Deck mockDeck = mock(Deck.class);

        String csvContent = "BASIC,Question 1,Answer 1,\nCLOZE,,,This is a {{c1::test}}\n";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.of(mockDeck));

        useCase.execute(new ImportDeckFromCsvUseCase.Input(deckId, inputStream));

        verify(mockDeck, times(2)).addCard(any());
        verify(deckRepository).save(mockDeck);
    }
}