package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.BasicCardContent;
import com.example.estudodeck.domain.ClozeCardContent;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Flashcard;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ExportDeckToCsvUseCase {

    private final DeckRepository deckRepository;
    private final UserContext userContext;

    public ExportDeckToCsvUseCase(DeckRepository deckRepository, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.userContext = userContext;
    }

    public String execute(Input input) {
        UUID userId = userContext.getAuthenticatedUserId();
        Deck deck = deckRepository.findByIdAndUserId(input.deckId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found"));

        return deck.getCards().stream()
                .map(this::formatCardAsCsvRow)
                .collect(Collectors.joining("\n"));
    }

    private String formatCardAsCsvRow(Flashcard card) {
        StringBuilder sb = new StringBuilder();
        if (card.getContent() instanceof BasicCardContent basic) {
            sb.append("BASIC,");
            sb.append(escapeCsvField(basic.getQuestion())).append(",");
            sb.append(escapeCsvField(basic.getAnswer())).append(",");
            sb.append("");
        } else if (card.getContent() instanceof ClozeCardContent cloze) {
            sb.append("CLOZE,");
            sb.append(",,"); // Empty question and answer
            sb.append(escapeCsvField(cloze.getText()));
        }
        return sb.toString();
    }

    private String escapeCsvField(String data) {
        if (data.contains(",") || data.contains("\"") || data.contains("\n")) {
            return "\"" + data.replace("\"", "\"\"") + "\"";
        }
        return data;
    }

    public record Input(UUID deckId) {}
}
