package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.dtos.RetentionStatsDto;
import com.example.estudodeck.domain.CardMaturity;
import com.example.estudodeck.infrastructure.persistence.entities.ReviewOutcomeJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataReviewOutcomeRepository;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetRetentionStatsUseCase {

    private final SpringDataReviewOutcomeRepository repository;
    private final UserContext userContext;

    public GetRetentionStatsUseCase(SpringDataReviewOutcomeRepository repository, UserContext userContext) {
        this.repository = repository;
        this.userContext = userContext;
    }

    public RetentionStatsDto execute() {
        UUID userId = userContext.getAuthenticatedUserId();
        List<ReviewOutcomeJpaEntity> allOutcomes = repository.findByUserId(userId);

        long youngSuccess = allOutcomes.stream().filter(o -> o.getMaturity() == CardMaturity.YOUNG && o.isSuccess()).count();
        long youngTotal = allOutcomes.stream().filter(o -> o.getMaturity() == CardMaturity.YOUNG).count();

        long matureSuccess = allOutcomes.stream().filter(o -> o.getMaturity() == CardMaturity.MATURE && o.isSuccess()).count();
        long matureTotal = allOutcomes.stream().filter(o -> o.getMaturity() == CardMaturity.MATURE).count();

        double youngRetention = (youngTotal == 0) ? 0.0 : (double) youngSuccess / youngTotal * 100.0;
        double matureRetention = (matureTotal == 0) ? 0.0 : (double) matureSuccess / matureTotal * 100.0;

        return new RetentionStatsDto(youngRetention, matureRetention);
    }
}
