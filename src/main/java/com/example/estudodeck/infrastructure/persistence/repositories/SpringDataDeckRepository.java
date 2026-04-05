package com.example.estudodeck.infrastructure.persistence.repositories;

import com.example.estudodeck.infrastructure.persistence.entities.DeckJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataDeckRepository extends JpaRepository<DeckJpaEntity, UUID> {
    Optional<DeckJpaEntity> findByIdAndUserId(UUID id, UUID userId);
    List<DeckJpaEntity> findAllByUserId(UUID userId);
    void deleteByIdAndUserId(UUID id, UUID userId);

    List<DeckJpaEntity> findAllByIsPublicTrue();
    Optional<DeckJpaEntity> findByIdAndIsPublicTrue(UUID id);
}
