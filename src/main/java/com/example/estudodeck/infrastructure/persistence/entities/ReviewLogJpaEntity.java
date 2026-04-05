package com.example.estudodeck.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "review_logs")
@Getter
@Setter
public class ReviewLogJpaEntity {

    @Id
    private UUID id;

    private LocalDate reviewDate;

    @Column(nullable = false)
    private UUID userId;
}
