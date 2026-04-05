package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.dtos.SearchResultDto;
import com.example.estudodeck.application.gateways.FlashcardSearchGateway;

import java.util.List;

public class SearchFlashcardsUseCase {

    private final FlashcardSearchGateway searchGateway;

    public SearchFlashcardsUseCase(FlashcardSearchGateway searchGateway) {
        this.searchGateway = searchGateway;
    }

    public List<SearchResultDto> execute(Input input) {
        if (input.query() == null || input.query().isBlank()) {
            return List.of();
        }
        return searchGateway.searchByTerm(input.query());
    }

    public record Input(String query) {}
}
