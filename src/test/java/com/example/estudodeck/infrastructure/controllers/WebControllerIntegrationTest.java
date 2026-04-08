package com.example.estudodeck.infrastructure.controllers;

import com.example.estudodeck.application.dtos.DeckHierarchyDto;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.application.usecases.*;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WebController.class)
@AutoConfigureMockMvc(addFilters = false)
class WebControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private DeckRepository deckRepository;
    @MockBean private CreateDeckUseCase createDeckUseCase;
    @MockBean private DeleteDeckUseCase deleteDeckUseCase;
    @MockBean private CreateFlashcardUseCase createFlashcardUseCase;
    @MockBean private DeleteFlashcardUseCase deleteFlashcardUseCase;
    @MockBean private ReviewFlashcardUseCase reviewFlashcardUseCase;
    @MockBean private UnsuspendFlashcardUseCase unsuspendFlashcardUseCase;
    @MockBean private GetDeckHierarchyUseCase getDeckHierarchyUseCase;
    @MockBean private GetReviewForecastUseCase getReviewForecastUseCase;
    @MockBean private AddTagToDeckUseCase addTagToDeckUseCase;
    @MockBean private RemoveTagFromDeckUseCase removeTagFromDeckUseCase;
    @MockBean private ExportDeckToCsvUseCase exportDeckToCsvUseCase;
    @MockBean private ImportDeckFromCsvUseCase importDeckFromCsvUseCase;
    @MockBean private UserContext userContext;

    @Test
    void shouldRenderIndexPageWithDeckTree() throws Exception {

        when(getDeckHierarchyUseCase.execute()).thenReturn(List.of());

        mockMvc.perform(get("/decks"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("deckTree"));
    }

    @Test
    void shouldCreateDeckAndRedirect() throws Exception {
        mockMvc.perform(post("/decks")
                        .param("name", "Java Advanced"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decks"));
    }
}