package com.example.estudodeck.infrastructure.controllers;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.infrastructure.cram.CramSessionManager;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CramController.class)
@AutoConfigureMockMvc(addFilters = false)
class CramControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private DeckRepository deckRepository;
    @MockBean private CramSessionManager cramSessionManager;
    @MockBean private UserContext userContext;

    @Test
    void shouldRedirectToDecksIfSessionIsNotActiveOnReview() throws Exception {

        when(cramSessionManager.isActive()).thenReturn(false);

        mockMvc.perform(get("/cram/review"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decks"));
    }

    @Test
    void shouldRedirectToFinishedIfNoMoreCards() throws Exception {
        when(cramSessionManager.isActive()).thenReturn(true);
        when(cramSessionManager.getNextCard()).thenReturn(null);

        mockMvc.perform(get("/cram/review"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cram/finished"));
    }
}