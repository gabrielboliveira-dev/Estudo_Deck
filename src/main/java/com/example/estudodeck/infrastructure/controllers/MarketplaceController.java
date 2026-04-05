package com.example.estudodeck.infrastructure.controllers;

import com.example.estudodeck.application.usecases.CloneDeckUseCase;
import com.example.estudodeck.application.usecases.ListPublicDecksUseCase;
import com.example.estudodeck.domain.Deck;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/marketplace")
public class MarketplaceController {

    private final ListPublicDecksUseCase listPublicDecksUseCase;
    private final CloneDeckUseCase cloneDeckUseCase;

    public MarketplaceController(ListPublicDecksUseCase listPublicDecksUseCase, CloneDeckUseCase cloneDeckUseCase) {
        this.listPublicDecksUseCase = listPublicDecksUseCase;
        this.cloneDeckUseCase = cloneDeckUseCase;
    }

    @GetMapping
    public String showMarketplace(Model model) {
        List<Deck> publicDecks = listPublicDecksUseCase.execute();
        model.addAttribute("publicDecks", publicDecks);
        return "marketplace";
    }

    @PostMapping("/clone/{deckId}")
    public String cloneDeck(@PathVariable UUID deckId) {
        cloneDeckUseCase.execute(new CloneDeckUseCase.Input(deckId));
        return "redirect:/decks";
    }
}
