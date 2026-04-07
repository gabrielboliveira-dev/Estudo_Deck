package com.example.estudodeck.infrastructure.gateways;

import com.example.estudodeck.application.gateways.ReviewOutcomeGateway;
import com.example.estudodeck.domain.ReviewOutcome;
import com.example.estudodeck.infrastructure.persistence.entities.ReviewOutcomeJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataReviewOutcomeRepository;
import org.springframework.stereotype.Component;

@Component
public class ReviewOutcomeGatewayImpl implements ReviewOutcomeGateway {

    private final SpringDataReviewOutcomeRepository repository;

    public ReviewOutcomeGatewayImpl(SpringDataReviewOutcomeRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(ReviewOutcome outcome) {
        ReviewOutcomeJpaEntity entity = new ReviewOutcomeJpaEntity();
        entity.setId(outcome.getId());
        entity.setFlashcardId(outcome.getFlashcardId());
        entity.setUserId(outcome.getUserId());
        entity.setSuccess(outcome.isSuccess());
        entity.setMaturity(outcome.getMaturity());
        entity.setTimestamp(outcome.getTimestamp());
        repository.save(entity);
    }
}
