package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.dtos.StreakDto;
import com.example.estudodeck.application.gateways.ReviewLogGateway;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCurrentStreakUseCaseTest {

    @Mock
    private ReviewLogGateway reviewLogGateway;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private GetCurrentStreakUseCase useCase;

    @Test
    void shouldReturnZeroWhenNoReviews() {
        UUID userId = UUID.randomUUID();
        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(reviewLogGateway.findDistinctReviewDatesByUserId(userId)).thenReturn(new ArrayList<>());

        StreakDto result = useCase.execute();

        assertEquals(0, result.streak());
    }

    @Test
    void shouldCalculateActiveStreakIncludingToday() {
        UUID userId = UUID.randomUUID();
        LocalDate today = LocalDate.now();
        List<LocalDate> dates = new ArrayList<>(Arrays.asList(today, today.minusDays(1), today.minusDays(2), today.minusDays(5)));

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(reviewLogGateway.findDistinctReviewDatesByUserId(userId)).thenReturn(dates);

        StreakDto result = useCase.execute();

        assertEquals(3, result.streak());
    }
}