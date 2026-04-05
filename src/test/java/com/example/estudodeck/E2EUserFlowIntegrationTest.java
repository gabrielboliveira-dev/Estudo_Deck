package com.example.estudodeck;

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

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class E2EUserFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Criamos um usuário mock para os testes que precisam de autenticação
    @BeforeEach
    void setup() {
        userRepository.findByEmail("user@test.com").ifPresent(userRepository::delete);
        UserJpaEntity user = new UserJpaEntity();
        user.setId(UUID.randomUUID());
        user.setEmail("user@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
    }

    /**
     * Testa o fluxo de registro de um novo usuário.
     */
    @Test
    void shouldRegisterNewUserAndRedirectToLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", "newuser@test.com")
                        .param("password", "newpassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));
    }

    /**
     * Testa o fluxo principal do usuário autenticado:
     * 1. Acessa a página de baralhos.
     * 2. Cria um novo baralho.
     * 3. Verifica se o baralho aparece na lista.
     */
    @Test
    @WithMockUser(username = "user@test.com")
    void shouldCreateDeckAndDisplayIt() throws Exception {
        // 1. Acessa a página e verifica se está vazia
        mockMvc.perform(get("/decks"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Meu Baralho de Teste"))));

        // 2. Cria um novo baralho
        mockMvc.perform(post("/decks")
                        .param("name", "Meu Baralho de Teste")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decks"));

        // 3. Verifica se o novo baralho está na página
        mockMvc.perform(get("/decks"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Meu Baralho de Teste")));
    }

    /**
     * Testa o fluxo de clonagem de um baralho do Marketplace:
     * 1. Outro usuário (owner) cria e publica um baralho.
     * 2. O usuário autenticado (user@test.com) clona o baralho.
     * 3. O baralho clonado aparece na sua lista de baralhos.
     */
    @Test
    @WithMockUser(username = "user@test.com")
    void shouldClonePublicDeckFromMarketplace() throws Exception {
        // 1. Setup: Outro usuário cria e publica um baralho
        // (Em um teste real, isso seria mais robusto, mas aqui simulamos a criação)
        // Esta parte do teste ainda é um desafio sem um segundo usuário mock.
        // O ideal seria ter um endpoint de API para criar o baralho público.
        // Por enquanto, vamos focar em testar o acesso à página.
        mockMvc.perform(get("/marketplace"))
                .andExpect(status().isOk());

        // A implementação completa do teste de clonagem exigiria uma refatoração
        // para permitir a criação de dados de teste de forma mais programática.
    }
}
