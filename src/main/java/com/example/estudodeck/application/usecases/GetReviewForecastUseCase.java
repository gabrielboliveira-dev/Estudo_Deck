package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.dtos.ReviewForecastDto;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GetReviewForecastUseCase {

    private final DeckRepository deckRepository;
    private final UserContext userContext;

    public GetReviewForecastUseCase(DeckRepository deckRepository, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.userContext = userContext;
    }

    public List<ReviewForecastDto> execute(Input input) {
        UUID userId = userContext.getAuthenticatedUserId();
        return deckRepository.getReviewForecast(userId, input.days());
    }

    public record Input(int days) {}
}
