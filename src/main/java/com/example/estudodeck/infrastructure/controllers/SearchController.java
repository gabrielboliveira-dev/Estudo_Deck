package com.example.estudodeck.infrastructure.controllers;

import com.example.estudodeck.application.dtos.SearchResultDto;
import com.example.estudodeck.application.usecases.SearchFlashcardsUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final SearchFlashcardsUseCase searchFlashcardsUseCase;

    public SearchController(SearchFlashcardsUseCase searchFlashcardsUseCase) {
        this.searchFlashcardsUseCase = searchFlashcardsUseCase;
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<SearchResultDto> results = searchFlashcardsUseCase.execute(new SearchFlashcardsUseCase.Input(query));
        model.addAttribute("query", query);
        model.addAttribute("results", results);
        return "search-results";
    }
}
