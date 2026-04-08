package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.dtos.ReviewForecastDto;
import com.example.estudodeck.application.gateways.DeckRepository;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetReviewForecastUseCaseTest {

    @Mock
    private DeckRepository deckRepository;

    @Mock
    private UserContext userContext;

    @InjectMocks
    private GetReviewForecastUseCase useCase;

    @Test
    void shouldDelegateToRepository() {
        UUID userId = UUID.randomUUID();
        List<ReviewForecastDto> mockForecast = List.of(new ReviewForecastDto(LocalDate.now(), 10));

        when(userContext.getAuthenticatedUserId()).thenReturn(userId);
        when(deckRepository.getReviewForecast(userId, 30)).thenReturn(mockForecast);

        List<ReviewForecastDto> result = useCase.execute(new GetReviewForecastUseCase.Input(30));

        assertEquals(1, result.size());
        assertEquals(10, result.get(0).count());
    }
}