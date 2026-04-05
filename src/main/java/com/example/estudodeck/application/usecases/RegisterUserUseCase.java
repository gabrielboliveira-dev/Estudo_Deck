package com.example.estudodeck.application.usecases;

import com.example.estudodeck.infrastructure.persistence.entities.UserJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RegisterUserUseCase {

    private final SpringDataUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(SpringDataUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void execute(Input input) {
        if (userRepository.findByEmail(input.email()).isPresent()) {
            throw new IllegalStateException("Email already registered");
        }
        UserJpaEntity user = new UserJpaEntity();
        user.setId(UUID.randomUUID());
        user.setEmail(input.email());
        user.setPassword(passwordEncoder.encode(input.password()));
        userRepository.save(user);
    }

    public record Input(String email, String password) {}
}
