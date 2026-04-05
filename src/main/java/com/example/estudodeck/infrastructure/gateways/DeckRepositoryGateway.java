package com.example.estudodeck.infrastructure.gateways;

import com.example.estudodeck.application.dtos.ReviewForecastDto;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.*;
import com.example.estudodeck.infrastructure.persistence.entities.DeckJpaEntity;
import com.example.estudodeck.infrastructure.persistence.entities.FlashcardJpaEntity;
import com.example.estudodeck.infrastructure.persistence.entities.TagJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataDeckRepository;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataFlashcardRepository;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataTagRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DeckRepositoryGateway implements DeckRepository {

    private final SpringDataDeckRepository springDeckRepository;
    private final SpringDataFlashcardRepository springFlashcardRepository;
    private final SpringDataTagRepository springTagRepository;

    public DeckRepositoryGateway(SpringDataDeckRepository springDeckRepository, SpringDataFlashcardRepository springFlashcardRepository, SpringDataTagRepository springTagRepository) {
        this.springDeckRepository = springDeckRepository;
        this.springFlashcardRepository = springFlashcardRepository;
        this.springTagRepository = springTagRepository;
    }

    @Override
    @Transactional
    public Deck save(Deck deck) {
        DeckJpaEntity entity = mapToEntity(deck);
        DeckJpaEntity savedEntity = springDeckRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Deck> findByIdAndUserId(UUID id, UUID userId) {
        return springDeckRepository.findByIdAndUserId(id, userId).map(this::mapToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Deck> findAllByUserId(UUID userId) {
        return springDeckRepository.findAllByUserId(userId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteByIdAndUserId(UUID id, UUID userId) {
        springDeckRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewForecastDto> getReviewForecast(UUID userId, int daysInFuture) {
        LocalDateTime startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endDate = startDate.plusDays(daysInFuture).withHour(23).withMinute(59).withSecond(59);
        return springFlashcardRepository.getReviewForecast(userId, startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Deck> findAllPublic() {
        return springDeckRepository.findAllByIsPublicTrue().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Deck> findPublicById(UUID id) {
        return springDeckRepository.findByIdAndIsPublicTrue(id).map(this::mapToDomain);
    }

    private DeckJpaEntity mapToEntity(Deck deck) {
        DeckJpaEntity entity = springDeckRepository.findById(deck.getId()).orElse(new DeckJpaEntity());
        entity.setId(deck.getId());
        entity.setName(deck.getName());
        entity.setParentId(deck.getParentId());
        entity.setUserId(deck.getUserId());
        entity.setPublic(deck.isPublic());

        Set<TagJpaEntity> tagEntities = deck.getTags().stream().map(tag ->
            springTagRepository.findByName(tag.getName()).orElseGet(() -> {
                TagJpaEntity newTag = new TagJpaEntity();
                newTag.setId(tag.getId());
                newTag.setName(tag.getName());
                return newTag;
            })
        ).collect(Collectors.toSet());
        entity.setTags(tagEntities);

        List<FlashcardJpaEntity> cardEntities = deck.getCards().stream()
                .map(card -> {
                    FlashcardJpaEntity cardEntity = new FlashcardJpaEntity();
                    cardEntity.setId(card.getId());
                    cardEntity.setCardType(card.getContent().getType());
                    cardEntity.setRepetitions(card.getRepetitions());
                    cardEntity.setEasinessFactor(card.getEasinessFactor());
                    cardEntity.setIntervalInDays(card.getIntervalInDays());
                    cardEntity.setNextDueDate(card.getNextDueDate());
                    cardEntity.setLeechCount(card.getLeechCount());
                    cardEntity.setSuspended(card.isSuspended());
                    cardEntity.setDeck(entity);

                    if (card.getContent() instanceof BasicCardContent basic) {
                        cardEntity.setQuestion(basic.getQuestion());
                        cardEntity.setAnswer(basic.getAnswer());
                    } else if (card.getContent() instanceof ClozeCardContent cloze) {
                        cardEntity.setText(cloze.getText());
                    }
                    return cardEntity;
                }).collect(Collectors.toList());

        entity.setCards(cardEntities);
        return entity;
    }

    private Deck mapToDomain(DeckJpaEntity entity) {
        List<Flashcard> cards = entity.getCards().stream()
                .map(cardEntity -> {
                    CardContent content = switch (cardEntity.getCardType()) {
                        case BASIC -> new BasicCardContent(cardEntity.getQuestion(), cardEntity.getAnswer());
                        case CLOZE -> new ClozeCardContent(cardEntity.getText());
                    };
                    return Flashcard.restore(
                            cardEntity.getId(),
                            content,
                            cardEntity.getRepetitions(),
                            cardEntity.getEasinessFactor(),
                            cardEntity.getIntervalInDays(),
                            cardEntity.getNextDueDate(),
                            cardEntity.getLeechCount(),
                            cardEntity.getSuspended()
                    );
                }).collect(Collectors.toList());

        Set<Tag> tags = entity.getTags().stream()
                .map(tagEntity -> new Tag(tagEntity.getId(), tagEntity.getName()))
                .collect(Collectors.toSet());

        return Deck.restore(entity.getId(), entity.getName(), entity.getParentId(), entity.getUserId(), entity.isPublic(), cards, tags);
    }
}
