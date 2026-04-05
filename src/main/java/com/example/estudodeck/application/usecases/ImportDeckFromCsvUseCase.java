package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.*;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class ImportDeckFromCsvUseCase {

    private final DeckRepository deckRepository;
    private final UserContext userContext;

    public ImportDeckFromCsvUseCase(DeckRepository deckRepository, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.userContext = userContext;
    }

    public void execute(Input input) {
        UUID userId = userContext.getAuthenticatedUserId();
        Deck deck = deckRepository.findByIdAndUserId(input.deckId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found"));

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input.fileStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",", -1); // -1 to include trailing empty strings
                if (columns.length < 4) continue;

                CardType type = CardType.valueOf(columns[0]);
                CardContent content = switch (type) {
                    case BASIC -> new BasicCardContent(columns[1], columns[2]);
                    case CLOZE -> new ClozeCardContent(columns[3]);
                };
                deck.addCard(Flashcard.createNew(content));
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to process CSV file", e);
        }

        deckRepository.save(deck);
    }

    public record Input(UUID deckId, InputStream fileStream) {}
}
