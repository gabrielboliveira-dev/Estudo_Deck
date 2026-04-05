package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.gateways.ReviewLogGateway;
import com.example.estudodeck.domain.ReviewLog;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateReviewLogUseCase {

    private final ReviewLogGateway reviewLogGateway;
    private final UserContext userContext;

    public CreateReviewLogUseCase(ReviewLogGateway reviewLogGateway, UserContext userContext) {
        this.reviewLogGateway = reviewLogGateway;
        this.userContext = userContext;
    }

    public void execute() {
        UUID userId = userContext.getAuthenticatedUserId();
        ReviewLog log = ReviewLog.create(userId);
        reviewLogGateway.save(log);
    }
}
