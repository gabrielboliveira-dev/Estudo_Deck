package com.example.estudodeck.infrastructure.security;

import com.example.estudodeck.infrastructure.persistence.entities.UserJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataUserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserContext {

    private final SpringDataUserRepository userRepository;

    public UserContext(SpringDataUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserJpaEntity getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found"));
    }

    public UUID getAuthenticatedUserId() {
        return getAuthenticatedUser().getId();
    }
}
