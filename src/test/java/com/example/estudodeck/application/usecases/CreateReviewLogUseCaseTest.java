package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.ReviewLogGateway;
import com.example.estudodeck.domain.ReviewLog;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateReviewLogUseCaseTest {

    @InjectMocks
    private CreateReviewLogUseCase createReviewLogUseCase;

    @Mock
    private ReviewLogGateway reviewLogGateway;

    @Mock
    private UserContext userContext;

    @Test
    void execute_ShouldCreateLogAndSave() {
        // Given
        UUID userId = UUID.randomUUID();
        when(userContext.getAuthenticatedUserId()).thenReturn(userId);

        // When
        createReviewLogUseCase.execute();

        // Then
        ArgumentCaptor<ReviewLog> logCaptor = ArgumentCaptor.forClass(ReviewLog.class);
        verify(reviewLogGateway).save(logCaptor.capture());
        
        ReviewLog savedLog = logCaptor.getValue();
        assertEquals(userId, savedLog.getUserId());
    }
}
