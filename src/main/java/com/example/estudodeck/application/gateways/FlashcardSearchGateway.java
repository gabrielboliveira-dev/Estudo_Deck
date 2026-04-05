package com.example.estudodeck.application.gateways;

import com.example.estudodeck.application.dtos.SearchResultDto;
import java.util.List;

public interface FlashcardSearchGateway {
    List<SearchResultDto> searchByTerm(String query);
}
