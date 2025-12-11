package com.example.estudodeck.application.gateways;

import com.example.estudodeck.domain.Deck;
import java.util.Optional;
import java.util.UUID;

public interface DeckRepository {

    Deck save(Deck deck);

    Optional<Deck> findById(UUID id);
}