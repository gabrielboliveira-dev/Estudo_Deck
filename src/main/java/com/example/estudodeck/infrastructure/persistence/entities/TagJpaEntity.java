package com.example.estudodeck.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tags")
@Getter
@Setter
public class TagJpaEntity {

    @Id
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;
}
