package com.example.estudodeck.infrastructure.gateways;

import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Flashcard;
import com.example.estudodeck.infrastructure.persistence.entities.DeckJpaEntity;
import com.example.estudodeck.infrastructure.persistence.entities.FlashcardJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataDeckRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DeckRepositoryGateway implements DeckRepository {

    private final SpringDataDeckRepository springRepository;

    public DeckRepositoryGateway(SpringDataDeckRepository springRepository) {
        this.springRepository = springRepository;
    }

    @Override
    public Deck save(Deck deck) {
        DeckJpaEntity entity = mapToEntity(deck);
        DeckJpaEntity savedEntity = springRepository.save(entity);

        return mapToDomain(savedEntity);
    }

    @Override
    public Optional<Deck> findById(UUID id) {
        Optional<DeckJpaEntity> entity = springRepository.findById(id);
        return entity.map(this::mapToDomain);
    }


    private DeckJpaEntity mapToEntity(Deck deck) {
        DeckJpaEntity entity = new DeckJpaEntity(deck.getId(), deck.getName(), null);

        List<FlashcardJpaEntity> cardEntities = deck.getCards().stream()
                .map(card -> new FlashcardJpaEntity(
                        card.getId(),
                        card.getQuestion(),
                        card.getAnswer(),
                        card.getRepetitions(),
                        card.getEasinessFactor(),
                        card.getIntervalInDays(),
                        card.getNextDueDate(),
                        entity
                )).collect(Collectors.toList());

        entity.setCards(cardEntities);
        return entity;
    }

    private Deck mapToDomain(DeckJpaEntity entity) {

        List<Flashcard> cards = entity.getCards().stream()
                .map(c -> Flashcard.restore(
                        c.getId(),
                        c.getQuestion(),
                        c.getAnswer(),
                        c.getRepetitions(),
                        c.getEasinessFactor(),
                        c.getIntervalInDays(),
                        c.getNextDueDate()
                )).collect(Collectors.toList());

        return Deck.restore(entity.getId(), entity.getName(), cards);
    }
}