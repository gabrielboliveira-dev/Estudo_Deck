package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.dtos.DeckHierarchyDto;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetDeckHierarchyUseCaseTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private GetDeckHierarchyUseCase useCase;

    @Test
    void shouldReturnHierarchicalStructure() {
        UUID userId = UUID.randomUUID();
        UUID rootId = UUID.randomUUID();
        UUID childId = UUID.randomUUID();

        Deck rootDeck = mock(Deck.class);
        when(rootDeck.getId()).thenReturn(rootId);
        when(rootDeck.getName()).thenReturn("Root");
        when(rootDeck.getParentId()).thenReturn(null);

        Deck childDeck = mock(Deck.class);
        when(childDeck.getId()).thenReturn(childId);
        when(childDeck.getName()).thenReturn("Child");
        when(childDeck.getParentId()).thenReturn(rootId);

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.findAllByUserId(userId)).thenReturn(List.of(rootDeck, childDeck));

        List<DeckHierarchyDto> result = useCase.execute();

        assertEquals(1, result.size());
        assertEquals(rootId, result.get(0).getId());
        assertEquals(1, result.get(0).getChildren().size());
        assertEquals(childId, result.get(0).getChildren().get(0).getId());
    }
}