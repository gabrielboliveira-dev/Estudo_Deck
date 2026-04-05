package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.dtos.HeatmapDataDto;
import com.example.estudodeck.infrastructure.persistence.entities.ReviewLogJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataReviewLogRepository;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GetHeatmapDataUseCase {

    private final SpringDataReviewLogRepository reviewLogRepository;
    private final UserContext userContext;

    public GetHeatmapDataUseCase(SpringDataReviewLogRepository reviewLogRepository, UserContext userContext) {
        this.reviewLogRepository = reviewLogRepository;
        this.userContext = userContext;
    }

    public HeatmapDataDto execute(Input input) {
        UUID userId = userContext.getAuthenticatedUserId();
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(input.daysInPast());

        Map<LocalDate, Long> dailyCounts = reviewLogRepository.findByUserIdAndDateRange(userId, startDate, endDate).stream()
                .collect(Collectors.groupingBy(
                        ReviewLogJpaEntity::getReviewDate,
                        Collectors.counting()
                ));

        return new HeatmapDataDto(startDate, endDate, dailyCounts);
    }

    public record Input(int daysInPast) {}
}
