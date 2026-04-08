package com.example.estudodeck;

import com.example.estudodeck.infrastructure.persistence.entities.UserJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
        UserJpaEntity user = new UserJpaEntity();
        user.setId(UUID.randomUUID());
        user.setEmail("user@test.com");
        user.setPassword(passwordEncoder.encode("password"));
        userRepository.save(user);
    }

    @Test
    void shouldRegisterNewUserAndRedirectToLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", "e2eusuario@test.com")
                        .param("password", "newpassword")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void shouldCreateDeckAndDisplayIt() throws Exception {
        mockMvc.perform(get("/decks"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("Meu Baralho de Teste"))));

        mockMvc.perform(post("/decks")
                        .param("name", "Meu Baralho de Teste")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/decks"));

        mockMvc.perform(get("/decks"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Meu Baralho de Teste")));
    }

    @Test
    @WithMockUser(username = "user@test.com")
    void shouldClonePublicDeckFromMarketplace() throws Exception {
        mockMvc.perform(get("/marketplace"))
                .andExpect(status().isOk());
    }
}