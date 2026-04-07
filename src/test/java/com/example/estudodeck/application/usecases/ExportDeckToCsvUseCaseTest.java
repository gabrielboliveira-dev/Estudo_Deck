package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.BasicCardContent;
import com.example.estudodeck.domain.ClozeCardContent;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExportDeckToCsvUseCaseTest {

    @InjectMocks
    private ExportDeckToCsvUseCase exportDeckToCsvUseCase;

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @Test
    void execute_ShouldExportDeckToCsv() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        ExportDeckToCsvUseCase.Input input = new ExportDeckToCsvUseCase.Input(deckId);

        Deck deck = Deck.create("Test Deck", null, userId);
        deck.addCard(Flashcard.createNew(new BasicCardContent("Q1", "A1")));
        deck.addCard(Flashcard.createNew(new ClozeCardContent("Text with {{c1::cloze}}")));

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.of(deck));

        // When
        String csv = exportDeckToCsvUseCase.execute(input);

        // Then
        String expectedCsv = "BASIC,Q1,A1,\nCLOZE,,,Text with {{c1::cloze}}";
        assertEquals(expectedCsv, csv);
    }

    @Test
    void execute_WhenDeckNotFound_ShouldThrowException() {
        // Given
        UUID userId = UUID.randomUUID();
        UUID deckId = UUID.randomUUID();
        ExportDeckToCsvUseCase.Input input = new ExportDeckToCsvUseCase.Input(deckId);

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findByIdAndUserId(deckId, userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> exportDeckToCsvUseCase.execute(input));
    }
}
