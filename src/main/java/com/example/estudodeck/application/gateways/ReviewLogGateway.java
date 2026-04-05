package com.example.estudodeck.application.gateways;

import com.example.estudodeck.domain.ReviewLog;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReviewLogGateway {
    void save(ReviewLog reviewLog);
    List<LocalDate> findDistinctReviewDatesByUserId(UUID userId);
}
