package com.example.estudodeck.infrastructure.persistence.repositories;

import com.example.estudodeck.infrastructure.persistence.entities.TagJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataTagRepository extends JpaRepository<TagJpaEntity, UUID> {
    Optional<TagJpaEntity> findByName(String name);
}
