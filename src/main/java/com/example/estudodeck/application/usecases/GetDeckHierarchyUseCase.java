package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.dtos.DeckHierarchyDto;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GetDeckHierarchyUseCase {

    private final DeckRepository deckRepository;
    private final UserContext userContext;

    public GetDeckHierarchyUseCase(DeckRepository deckRepository, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.userContext = userContext;
    }

    public List<DeckHierarchyDto> execute() {
        UUID userId = userContext.getAuthenticatedUserId();
        List<Deck> allDecks = deckRepository.findAllByUserId(userId);
        Map<UUID, DeckHierarchyDto> dtoMap = allDecks.stream()
                .collect(Collectors.toMap(Deck::getId, deck -> new DeckHierarchyDto(deck.getId(), deck.getName())));

        List<DeckHierarchyDto> rootDecks = new ArrayList<>();

        allDecks.forEach(deck -> {
            DeckHierarchyDto currentDto = dtoMap.get(deck.getId());
            if (deck.getParentId() == null) {
                rootDecks.add(currentDto);
            } else {
                DeckHierarchyDto parentDto = dtoMap.get(deck.getParentId());
                if (parentDto != null) {
                    parentDto.getChildren().add(currentDto);
                } else {
                    // This case handles orphaned decks, adding them to the root to ensure they are not lost.
                    rootDecks.add(currentDto);
                }
            }
        });

        return rootDecks;
    }
}
