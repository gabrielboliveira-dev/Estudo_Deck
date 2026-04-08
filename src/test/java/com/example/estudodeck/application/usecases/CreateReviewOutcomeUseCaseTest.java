package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.ReviewOutcomeGateway;
import com.example.estudodeck.domain.CardMaturity;
import com.example.estudodeck.domain.ReviewOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CreateReviewOutcomeUseCaseTest {

    @InjectMocks
    private CreateReviewOutcomeUseCase createReviewOutcomeUseCase;

    @Mock
    private ReviewOutcomeGateway reviewOutcomeGateway;

    @Test
    void execute_ShouldCreateOutcomeAndSave() {
        // Given
        CreateReviewOutcomeUseCase.Input input = new CreateReviewOutcomeUseCase.Input(
                UUID.randomUUID(),
                UUID.randomUUID(),
                true,
                CardMaturity.YOUNG
        );

        // When
        createReviewOutcomeUseCase.execute(input);

        // Then
        ArgumentCaptor<ReviewOutcome> outcomeCaptor = ArgumentCaptor.forClass(ReviewOutcome.class);
        verify(reviewOutcomeGateway).save(outcomeCaptor.capture());
        
        ReviewOutcome savedOutcome = outcomeCaptor.getValue();
        assertEquals(input.flashcardId(), savedOutcome.getFlashcardId());
        assertEquals(input.userId(), savedOutcome.getUserId());
        assertEquals(input.success(), savedOutcome.isSuccess());
        assertEquals(input.maturity(), savedOutcome.getMaturity());
    }
}
