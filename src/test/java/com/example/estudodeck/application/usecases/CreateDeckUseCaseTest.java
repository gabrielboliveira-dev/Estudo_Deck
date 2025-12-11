package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CreateDeckUseCaseTest {

    @Mock
    private DeckRepository repository;

    @InjectMocks
    private CreateDeckUseCase createDeckUseCase;

    @Test
    @DisplayName("Deve criar e salvar um baralho novo")
    void shouldCreateAndSaveDeck() {
        when(repository.save(any(Deck.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Deck createdDeck = createDeckUseCase.execute("Java Avançado");
        Assertions.assertNotNull(createdDeck.getId());
        Assertions.assertEquals("Java Avançado", createdDeck.getName());
        verify(repository, times(1)).save(any(Deck.class));
    }
}