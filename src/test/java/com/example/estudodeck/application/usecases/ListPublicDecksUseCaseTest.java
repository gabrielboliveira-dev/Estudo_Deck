package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListPublicDecksUseCaseTest {

    @Mock
    private DeckRepository deckRepository;

    @InjectMocks
    private ListPublicDecksUseCase useCase;

    @Test
    void shouldReturnPublicDecks() {
        List<Deck> mockDecks = List.of(mock(Deck.class), mock(Deck.class));
        when(deckRepository.findAllPublic()).thenReturn(mockDecks);

        List<Deck> result = useCase.execute();

        assertEquals(2, result.size());
    }
}