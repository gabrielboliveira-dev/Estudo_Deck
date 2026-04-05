package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.dtos.StreakDto;
import com.example.estudodeck.application.gateways.ReviewLogGateway;
import com.example.estudodeck.infrastructure.security.UserContext;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class GetCurrentStreakUseCase {

    private final ReviewLogGateway reviewLogGateway;
    private final UserContext userContext;

    public GetCurrentStreakUseCase(ReviewLogGateway reviewLogGateway, UserContext userContext) {
        this.reviewLogGateway = reviewLogGateway;
        this.userContext = userContext;
    }

    public StreakDto execute() {
        UUID userId = userContext.getAuthenticatedUserId();
        List<LocalDate> reviewDates = reviewLogGateway.findDistinctReviewDatesByUserId(userId);
        reviewDates.sort(Comparator.reverseOrder());

        int streak = 0;
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // Check if there's a review today or yesterday to start the count
        if (reviewDates.isEmpty() || (!reviewDates.get(0).equals(today) && !reviewDates.get(0).equals(yesterday))) {
            return new StreakDto(0);
        }

        // If the most recent review is today, it's part of the streak
        if (reviewDates.get(0).equals(today)) {
            streak = 1;
        } else { // The most recent review is yesterday
            streak = 1;
        }

        LocalDate lastDate = reviewDates.get(0);

        for (int i = 1; i < reviewDates.size(); i++) {
            LocalDate currentDate = reviewDates.get(i);
            if (lastDate.minusDays(1).equals(currentDate)) {
                streak++;
                lastDate = currentDate;
            } else {
                break; // Streak is broken
            }
        }

        return new StreakDto(streak);
    }
}
