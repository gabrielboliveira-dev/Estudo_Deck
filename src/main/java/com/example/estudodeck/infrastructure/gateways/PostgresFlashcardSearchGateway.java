package com.example.estudodeck.infrastructure.gateways;

import com.example.estudodeck.application.dtos.SearchResultDto;
import com.example.estudodeck.application.gateways.FlashcardSearchGateway;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class PostgresFlashcardSearchGateway implements FlashcardSearchGateway {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SearchResultDto> searchByTerm(String searchTerm) {
        String sql = """
            SELECT
                f.id as flashcardId,
                COALESCE(f.question, f.text) as prompt,
                d.id as deckId,
                d.name as deckName
            FROM
                flashcards f
            JOIN
                decks d ON f.deck_id = d.id
            WHERE
                to_tsvector('portuguese', COALESCE(f.question, '') || ' ' || COALESCE(f.answer, '') || ' ' || COALESCE(f.text, ''))
                @@ plainto_tsquery('portuguese', :searchTerm)
        """;

        Query query = entityManager.createNativeQuery(sql, "SearchResultMapping");
        query.setParameter("searchTerm", searchTerm);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(row -> new SearchResultDto(
                        (UUID) row[0],
                        (String) row[1],
                        (UUID) row[2],
                        (String) row[3]
                ))
                .collect(Collectors.toList());
    }
}
