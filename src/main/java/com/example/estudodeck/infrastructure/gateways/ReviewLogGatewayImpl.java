package com.example.estudodeck.infrastructure.gateways;

import com.example.estudodeck.application.gateways.ReviewLogGateway;
import com.example.estudodeck.domain.ReviewLog;
import com.example.estudodeck.infrastructure.persistence.entities.ReviewLogJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataReviewLogRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class ReviewLogGatewayImpl implements ReviewLogGateway {

    private final SpringDataReviewLogRepository repository;

    public ReviewLogGatewayImpl(SpringDataReviewLogRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(ReviewLog reviewLog) {
        ReviewLogJpaEntity entity = new ReviewLogJpaEntity();
        entity.setId(reviewLog.getId());
        entity.setReviewDate(reviewLog.getReviewDate());
        entity.setUserId(reviewLog.getUserId());
        repository.save(entity);
    }

    @Override
    public List<LocalDate> findDistinctReviewDatesByUserId(UUID userId) {
        return repository.findDistinctReviewDatesByUserId(userId);
    }
}
