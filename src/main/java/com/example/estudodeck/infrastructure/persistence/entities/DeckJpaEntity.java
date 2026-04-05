package com.example.estudodeck.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "decks")
@Getter
@Setter
public class DeckJpaEntity {

    @Id
    private UUID id;
    private String name;

    @Column(name = "parent_id")
    private UUID parentId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    private boolean isPublic = false;

    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<FlashcardJpaEntity> cards = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "deck_tags",
            joinColumns = @JoinColumn(name = "deck_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<TagJpaEntity> tags = new HashSet<>();
}
