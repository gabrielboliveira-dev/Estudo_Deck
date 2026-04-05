package com.example.estudodeck.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class Deck {

    private final UUID id;
    private String name;
    private final List<Flashcard> cards;
    private UUID parentId;
    private final Set<Tag> tags;
    private final UUID userId;
    private boolean isPublic;

    private Deck(String name, UUID parentId, UUID userId) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.parentId = parentId;
        this.userId = userId;
        this.isPublic = false;
        this.cards = new ArrayList<>();
        this.tags = new HashSet<>();
    }

    public static Deck create(String name, UUID parentId, UUID userId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Deck name is required.");
        }
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required.");
        }
        return new Deck(name, parentId, userId);
    }

    public void publish() {
        this.isPublic = true;
    }

    public Deck cloneForUser(UUID newUserId) {
        Deck clonedDeck = new Deck(this.name, null, newUserId); // Cloned decks are always root decks for the new user

        List<Flashcard> clonedCards = this.cards.stream()
                .map(card -> Flashcard.createNew(card.getContent()))
                .collect(Collectors.toList());
        clonedDeck.cards.addAll(clonedCards);

        this.tags.forEach(clonedDeck::addTag);

        return clonedDeck;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    public void addCard(Flashcard card) {
        if (card == null) {
            throw new IllegalArgumentException("Cannot add a null card.");
        }
        this.cards.add(card);
    }

    public void removeCard(UUID flashcardId) {
        this.cards.removeIf(card -> card.getId().equals(flashcardId));
    }

    public void changeName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Deck name is required.");
        }
        this.name = newName;
    }

    public List<Flashcard> getPendingCards() {
        LocalDateTime now = LocalDateTime.now();
        return cards.stream()
                .filter(c -> !c.isSuspended() && (c.getNextDueDate().isBefore(now) || c.getNextDueDate().isEqual(now)))
                .toList();
    }

    public List<Flashcard> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public static Deck restore(UUID id, String name, UUID parentId, UUID userId, boolean isPublic, List<Flashcard> cards, Set<Tag> tags) {
        return new Deck(id, name, parentId, userId, isPublic, cards, tags);
    }

    private Deck(UUID id, String name, UUID parentId, UUID userId, boolean isPublic, List<Flashcard> cards, Set<Tag> tags) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.userId = userId;
        this.isPublic = isPublic;
        this.cards = new ArrayList<>(cards);
        this.tags = new HashSet<>(tags);
    }
}
