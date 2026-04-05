package com.example.estudodeck.application.gateways;

import com.example.estudodeck.application.dtos.ReviewForecastDto;
import com.example.estudodeck.domain.Deck;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeckRepository {

    Deck save(Deck deck);

    Optional<Deck> findByIdAndUserId(UUID id, UUID userId);

    List<Deck> findAllByUserId(UUID userId);

    void deleteByIdAndUserId(UUID id, UUID userId);

    List<ReviewForecastDto> getReviewForecast(UUID userId, int daysInFuture);

    List<Deck> findAllPublic();

    Optional<Deck> findPublicById(UUID id);
}
