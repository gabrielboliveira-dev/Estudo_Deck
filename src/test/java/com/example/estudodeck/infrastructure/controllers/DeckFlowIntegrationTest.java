package com.example.estudodeck.infrastructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.estudodeck.infrastructure.persistence.entities.UserJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeckFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SpringDataUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String TEST_USER_EMAIL = "user@test.com";
    private final String TEST_USER_PASSWORD = "password";
    private UUID testUserId;

    @BeforeEach
    void setup() {
        userRepository.findByEmail(TEST_USER_EMAIL).ifPresent(userRepository::delete);
        UserJpaEntity user = new UserJpaEntity();
        user.setId(UUID.randomUUID());
        user.setEmail(TEST_USER_EMAIL);
        user.setPassword(passwordEncoder.encode(TEST_USER_PASSWORD));
        userRepository.save(user);
        this.testUserId = user.getId();
    }

    /**
     * Testa o fluxo principal:
     * 1. Cria um baralho.
     * 2. Adiciona um flashcard básico a ele.
     * 3. Verifica se o flashcard foi adicionado.
     */
    @Test
    @WithMockUser(username = TEST_USER_EMAIL)
    void shouldCreateDeckAndAddBasicFlashcard() throws Exception {
        // 1. Criar um baralho
        String deckName = "Java Concurrency";
        MvcResult createDeckResult = mockMvc.perform(post("/api/decks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"" + deckName + "\", \"parentId\": null}") // parentId is now required in Input
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(deckName))
                .andReturn();

        String responseBody = createDeckResult.getResponse().getContentAsString();
        UUID deckId = UUID.fromString(objectMapper.readTree(responseBody).get("id").asText());

        // 2. Adicionar um flashcard
        Map<String, String> contentFields = Map.of("question", "What is a Semaphore?", "answer", "A synchronization aid.");
        Map<String, Object> flashcardRequest = Map.of("type", "BASIC", "contentFields", contentFields);

        mockMvc.perform(post("/api/decks/{deckId}/flashcards", deckId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flashcardRequest))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.prompt").value("What is a Semaphore?"));
    }

    /**
     * Testa a revisão de um flashcard:
     * 1. Cria um baralho e um flashcard.
     * 2. Realiza uma revisão (envia uma nota de qualidade).
     * 3. O endpoint deve retornar sucesso.
     */
    @Test
    @WithMockUser(username = TEST_USER_EMAIL)
    void shouldReviewFlashcard() throws Exception {
        // Setup: Criar deck e flashcard
        MvcResult deckResult = mockMvc.perform(post("/api/decks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"History\", \"parentId\": null}")
                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        UUID deckId = UUID.fromString(objectMapper.readTree(deckResult.getResponse().getContentAsString()).get("id").asText());

        Map<String, Object> flashcardReq = Map.of("type", "BASIC", "contentFields", Map.of("question", "D-Day date?", "answer", "June 6, 1944"));
        MvcResult cardResult = mockMvc.perform(post("/api/decks/{deckId}/flashcards", deckId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(flashcardReq))
                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        UUID flashcardId = UUID.fromString(objectMapper.readTree(cardResult.getResponse().getContentAsString()).get("id").asText());

        // Teste: Processar a revisão
        mockMvc.perform(post("/api/decks/{deckId}/flashcards/{flashcardId}/review", deckId, flashcardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"quality\": 5}")
                .with(csrf()))
                .andExpect(status().isOk());
    }

    /**
     * Testa a exclusão e o tratamento de recurso não encontrado (404).
     * 1. Cria um baralho.
     * 2. Deleta o baralho via API.
     * 3. Tenta buscar a página de detalhes do baralho deletado e espera um status 404 Not Found.
     */
    @Test
    @WithMockUser(username = TEST_USER_EMAIL)
    void shouldReturn404WhenAccessingDeletedDeck() throws Exception {
        // Setup: Criar deck
        MvcResult deckResult = mockMvc.perform(post("/api/decks")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"To Be Deleted\", \"parentId\": null}")
                .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        UUID deckId = UUID.fromString(objectMapper.readTree(deckResult.getResponse().getContentAsString()).get("id").asText());

        // Teste: Deletar o deck
        mockMvc.perform(delete("/api/decks/{deckId}", deckId)
                .with(csrf()))
                .andExpect(status().isNoContent());

        // Verificação: Tentar acessar a página de detalhes do deck deletado e esperar um 404.
        mockMvc.perform(get("/decks/{deckId}", deckId))
                .andExpect(status().isNotFound());
    }
}
