package com.example.estudodeck.infrastructure.controllers;

import com.example.estudodeck.application.usecases.CreateFlashcardUseCase;
import com.example.estudodeck.application.usecases.DeleteFlashcardUseCase;
import com.example.estudodeck.application.usecases.ReviewFlashcardUseCase;
import com.example.estudodeck.application.usecases.UnsuspendFlashcardUseCase;
import com.example.estudodeck.domain.CardType;
import com.example.estudodeck.domain.Flashcard;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FlashcardController.class)
@AutoConfigureMockMvc(addFilters = false)
class FlashcardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean private CreateFlashcardUseCase createFlashcardUseCase;
    @MockBean private ReviewFlashcardUseCase reviewFlashcardUseCase;
    @MockBean private DeleteFlashcardUseCase deleteFlashcardUseCase;
    @MockBean private UnsuspendFlashcardUseCase unsuspendFlashcardUseCase;

    @Test
    void shouldCreateFlashcardAndReturn200() throws Exception {
        UUID deckId = UUID.randomUUID();
        FlashcardController.CreateFlashcardRequest request = new FlashcardController.CreateFlashcardRequest(
                CardType.BASIC, Map.of("question", "Q", "answer", "A")
        );

        when(createFlashcardUseCase.execute(any())).thenReturn(Flashcard.createNew(new com.example.estudodeck.domain.BasicCardContent("Q", "A")));

        mockMvc.perform(post("/api/decks/{deckId}/flashcards", deckId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(createFlashcardUseCase).execute(any(CreateFlashcardUseCase.Input.class));
    }

    @Test
    void shouldDeleteFlashcardAndReturn204() throws Exception {
        UUID deckId = UUID.randomUUID();
        UUID flashcardId = UUID.randomUUID();

        mockMvc.perform(delete("/api/decks/{deckId}/flashcards/{flashcardId}", deckId, flashcardId))
                .andExpect(status().isNoContent());

        verify(deleteFlashcardUseCase).execute(any(DeleteFlashcardUseCase.Input.class));
    }
}