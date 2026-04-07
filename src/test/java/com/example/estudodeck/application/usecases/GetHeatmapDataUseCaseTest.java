package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.dtos.HeatmapDataDto;
import com.example.estudodeck.infrastructure.persistence.entities.ReviewLogJpaEntity;
import com.example.estudodeck.infrastructure.persistence.repositories.SpringDataReviewLogRepository;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetHeatmapDataUseCaseTest {

    @Mock
    private SpringDataReviewLogRepository repository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private GetHeatmapDataUseCase useCase;

    @Test
    void shouldGroupReviewsByDate() {
        UUID userId = UUID.randomUUID();
        LocalDate today = LocalDate.now();

        ReviewLogJpaEntity log1 = new ReviewLogJpaEntity();
        log1.setReviewDate(today);
        ReviewLogJpaEntity log2 = new ReviewLogJpaEntity();
        log2.setReviewDate(today);

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(repository.findByUserIdAndDateRange(eq(userId), any(), any())).thenReturn(List.of(log1, log2));

        HeatmapDataDto result = useCase.execute(new GetHeatmapDataUseCase.Input(7));

        assertEquals(2L, result.dailyCounts().get(today));
    }
}