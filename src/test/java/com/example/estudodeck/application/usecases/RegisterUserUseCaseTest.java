package com.example.estudodeck.application.usecases;

import com.example.estudodeck.infrastructure.persistence.entities.UserJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private SpringDataUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterUserUseCase useCase;

    @Test
    void shouldRegisterNewUser() {
        String email = "test@test.com";
        String plainPassword = "password123";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(plainPassword)).thenReturn("encodedPassword");

        useCase.execute(new RegisterUserUseCase.Input(email, plainPassword));

        verify(userRepository).save(any(UserJpaEntity.class));
    }

    @Test
    void shouldThrowWhenEmailExists() {
        String email = "test@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new UserJpaEntity()));

        assertThrows(IllegalStateException.class, () ->
                useCase.execute(new RegisterUserUseCase.Input(email, "password123"))
        );

        verify(userRepository, never()).save(any());
    }
}