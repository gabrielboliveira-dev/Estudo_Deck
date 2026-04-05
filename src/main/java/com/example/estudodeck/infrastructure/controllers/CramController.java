package com.example.estudodeck.infrastructure.controllers;

import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Flashcard;
import com.example.estudodeck.infrastructure.cram.CramSessionManager;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/cram")
public class CramController {

    private final DeckRepository deckRepository;
    private final CramSessionManager cramSessionManager;
    private final UserContext userContext;

    public CramController(DeckRepository deckRepository, CramSessionManager cramSessionManager, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.cramSessionManager = cramSessionManager;
        this.userContext = userContext;
    }

    @PostMapping("/start/{deckId}")
    public String startCramSession(@PathVariable UUID deckId) {
        UUID userId = userContext.getAuthenticatedUserId();
        Deck deck = deckRepository.findByIdAndUserId(deckId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck not found"));
        cramSessionManager.start(deck);
        return "redirect:/cram/review";
    }

    @GetMapping("/review")
    public String reviewNextCard(Model model) {
        if (!cramSessionManager.isActive()) {
            return "redirect:/decks";
        }

        Flashcard card = cramSessionManager.getNextCard();
        if (card == null) {
            return "redirect:/cram/finished";
        }

        model.addAttribute("deckName", cramSessionManager.getDeckName());
        model.addAttribute("card", card);
        return "cram-review";
    }

    @PostMapping("/next")
    public String postReviewNextCard() {
        if (!cramSessionManager.isActive()) {
            return "redirect:/decks";
        }
        return "redirect:/cram/review";
    }

    @GetMapping("/finished")
    public String finished(Model model) {
        if (!cramSessionManager.isActive()) {
            return "redirect:/decks";
        }
        model.addAttribute("deckName", cramSessionManager.getDeckName());
        cramSessionManager.end(); // End session after showing the finished page
        return "cram-finished";
    }

    @PostMapping("/end")
    public String endSession() {
        cramSessionManager.end();
        return "redirect:/decks";
    }
}
