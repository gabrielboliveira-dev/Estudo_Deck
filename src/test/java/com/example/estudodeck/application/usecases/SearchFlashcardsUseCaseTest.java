package com.example.estudodeck.application.usecases;

import com.example.estudodeck.application.dtos.SearchResultDto;
import com.example.estudodeck.application.gateways.FlashcardSearchGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchFlashcardsUseCaseTest {

    @Mock
    private FlashcardSearchGateway searchGateway;

    @InjectMocks
    private SearchFlashcardsUseCase useCase;

    @Test
    void shouldReturnEmptyListIfQueryIsBlank() {
        List<SearchResultDto> result = useCase.execute(new SearchFlashcardsUseCase.Input("   "));

        assertTrue(result.isEmpty());
        verify(searchGateway, never()).searchByTerm(any());
    }

    @Test
    void shouldCallGatewayWhenQueryIsValid() {
        List<SearchResultDto> mockResults = List.of(mock(SearchResultDto.class));
        when(searchGateway.searchByTerm("Spring")).thenReturn(mockResults);

        List<SearchResultDto> result = useCase.execute(new SearchFlashcardsUseCase.Input("Spring"));

        assertEquals(1, result.size());
        verify(searchGateway).searchByTerm("Spring");
    }
}