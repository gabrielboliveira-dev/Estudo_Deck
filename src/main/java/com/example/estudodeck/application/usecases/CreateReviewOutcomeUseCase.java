package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.ReviewOutcomeGateway;
import com.example.estudodeck.domain.CardMaturity;
import com.example.estudodeck.domain.ReviewOutcome;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateReviewOutcomeUseCase {

    private final ReviewOutcomeGateway gateway;

    public CreateReviewOutcomeUseCase(ReviewOutcomeGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(Input input) {
        ReviewOutcome outcome = ReviewOutcome.create(input.flashcardId(), input.userId(), input.success(), input.maturity());
        gateway.save(outcome);
    }

    public record Input(UUID flashcardId, UUID userId, boolean success, CardMaturity maturity) {}
}
