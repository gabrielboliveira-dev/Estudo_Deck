package com.example.estudodeck.infrastructure.controllers;

import com.example.estudodeck.application.dtos.DeckHierarchyDto;
import com.example.estudodeck.application.dtos.ReviewForecastDto;
import com.example.estudodeck.application.exceptions.ResourceNotFoundException;
import com.example.estudodeck.application.gateways.DeckRepository;
import com.example.estudodeck.application.usecases.*;
import com.example.estudodeck.domain.CardType;
import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Flashcard;
import com.example.estudodeck.infrastructure.security.UserContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/decks")
public class WebController {

    private final DeckRepository deckRepository;
    private final CreateDeckUseCase createDeckUseCase;
    private final DeleteDeckUseCase deleteDeckUseCase;
    private final CreateFlashcardUseCase createFlashcardUseCase;
    private final DeleteFlashcardUseCase deleteFlashcardUseCase;
    private final ReviewFlashcardUseCase reviewFlashcardUseCase;
    private final UnsuspendFlashcardUseCase unsuspendFlashcardUseCase;
    private final GetDeckHierarchyUseCase getDeckHierarchyUseCase;
    private final GetReviewForecastUseCase getReviewForecastUseCase;
    private final AddTagToDeckUseCase addTagToDeckUseCase;
    private final RemoveTagFromDeckUseCase removeTagFromDeckUseCase;
    private final ExportDeckToCsvUseCase exportDeckToCsvUseCase;
    private final ImportDeckFromCsvUseCase importDeckFromCsvUseCase;
    private final UserContext userContext;

    public WebController(DeckRepository deckRepository, CreateDeckUseCase createDeckUseCase, DeleteDeckUseCase deleteDeckUseCase, CreateFlashcardUseCase createFlashcardUseCase, DeleteFlashcardUseCase deleteFlashcardUseCase, ReviewFlashcardUseCase reviewFlashcardUseCase, UnsuspendFlashcardUseCase unsuspendFlashcardUseCase, GetDeckHierarchyUseCase getDeckHierarchyUseCase, GetReviewForecastUseCase getReviewForecastUseCase, AddTagToDeckUseCase addTagToDeckUseCase, RemoveTagFromDeckUseCase removeTagFromDeckUseCase, ExportDeckToCsvUseCase exportDeckToCsvUseCase, ImportDeckFromCsvUseCase importDeckFromCsvUseCase, UserContext userContext) {
        this.deckRepository = deckRepository;
        this.createDeckUseCase = createDeckUseCase;
        this.deleteDeckUseCase = deleteDeckUseCase;
        this.createFlashcardUseCase = createFlashcardUseCase;
        this.deleteFlashcardUseCase = deleteFlashcardUseCase;
        this.reviewFlashcardUseCase = reviewFlashcardUseCase;
        this.unsuspendFlashcardUseCase = unsuspendFlashcardUseCase;
        this.getDeckHierarchyUseCase = getDeckHierarchyUseCase;
        this.getReviewForecastUseCase = getReviewForecastUseCase;
        this.addTagToDeckUseCase = addTagToDeckUseCase;
        this.removeTagFromDeckUseCase = removeTagFromDeckUseCase;
        this.exportDeckToCsvUseCase = exportDeckToCsvUseCase;
        this.importDeckFromCsvUseCase = importDeckFromCsvUseCase;
        this.userContext = userContext;
    }

    @GetMapping
    public String index(Model model) {
        List<DeckHierarchyDto> deckTree = getDeckHierarchyUseCase.execute();
        model.addAttribute("deckTree", deckTree);
        return "index";
    }

    @GetMapping("/new")
    public String newDeckForm(Model model) {
        UUID userId = userContext.getAuthenticatedUserId();
        model.addAttribute("allDecks", deckRepository.findAllByUserId(userId));
        return "new-deck";
    }

    @PostMapping
    public String createDeck(@RequestParam String name, @RequestParam(required = false) UUID parentId) {
        createDeckUseCase.execute(new CreateDeckUseCase.Input(name, parentId));
        return "redirect:/decks";
    }

    @PostMapping("/{id}/delete")
    public String deleteDeck(@PathVariable UUID id) {
        deleteDeckUseCase.execute(new DeleteDeckUseCase.Input(id));
        return "redirect:/decks";
    }

    @GetMapping("/{id}")
    public String deckDetails(@PathVariable UUID id, Model model) {
        UUID userId = userContext.getAuthenticatedUserId();
        Deck deck = deckRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck with ID " + id + " not found."));
        List<ReviewForecastDto> forecast = getReviewForecastUseCase.execute(new GetReviewForecastUseCase.Input(30));
        
        model.addAttribute("deck", deck);
        model.addAttribute("forecast", forecast);
        return "deck-details";
    }

    @GetMapping("/{id}/export")
    public ResponseEntity<String> exportDeck(@PathVariable UUID id) {
        String csvContent = exportDeckToCsvUseCase.execute(new ExportDeckToCsvUseCase.Input(id));
        UUID userId = userContext.getAuthenticatedUserId();
        Deck deck = deckRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new ResourceNotFoundException("Deck not found"));
        String fileName = deck.getName().replaceAll("\\s+", "_") + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(csvContent);
    }

    @PostMapping("/{id}/import")
    public String importDeck(@PathVariable UUID id, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
            return "redirect:/decks/" + id;
        }
        try {
            importDeckFromCsvUseCase.execute(new ImportDeckFromCsvUseCase.Input(id, file.getInputStream()));
            redirectAttributes.addFlashAttribute("message", "Successfully imported from " + file.getOriginalFilename());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Failed to import file: " + e.getMessage());
        }
        return "redirect:/decks/" + id;
    }

    @PostMapping("/{id}/flashcards")
    public String createFlashcard(@PathVariable UUID id, @RequestParam CardType type, @RequestParam Map<String, String> fields) {
        createFlashcardUseCase.execute(new CreateFlashcardUseCase.Input(id, type, fields));
        return "redirect:/decks/" + id;
    }

    @PostMapping("/{deckId}/flashcards/{flashcardId}/delete")
    public String deleteFlashcard(@PathVariable UUID deckId, @PathVariable UUID flashcardId) {
        deleteFlashcardUseCase.execute(new DeleteFlashcardUseCase.Input(deckId, flashcardId));
        return "redirect:/decks/" + deckId;
    }

    @PostMapping("/{deckId}/flashcards/{flashcardId}/unsuspend")
    public String unsuspendFlashcard(@PathVariable UUID deckId, @PathVariable UUID flashcardId) {
        unsuspendFlashcardUseCase.execute(new UnsuspendFlashcardUseCase.Input(deckId, flashcardId));
        return "redirect:/decks/" + deckId;
    }

    @PostMapping("/{deckId}/tags")
    public String addTag(@PathVariable UUID deckId, @RequestParam String tagName) {
        addTagToDeckUseCase.execute(new AddTagToDeckUseCase.Input(deckId, tagName));
        return "redirect:/decks/" + deckId;
    }

    @PostMapping("/{deckId}/tags/delete")
    public String removeTag(@PathVariable UUID deckId, @RequestParam String tagName) {
        removeTagFromDeckUseCase.execute(new RemoveTagFromDeckUseCase.Input(deckId, tagName));
        return "redirect:/decks/" + deckId;
    }

    @GetMapping("/{id}/review")
    public String review(@PathVariable UUID id, Model model) {
        UUID userId = userContext.getAuthenticatedUserId();
        Deck deck = deckRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Deck with ID " + id + " not found."));
        
        List<Flashcard> pendingCards = deck.getPendingCards().stream()
                .filter(card -> !card.isSuspended())
                .collect(Collectors.toList());
        
        model.addAttribute("deck", deck);
        model.addAttribute("card", pendingCards.stream().findFirst().orElse(null));
        return "review";
    }

    @PostMapping("/{deckId}/review/{flashcardId}")
    public String processReview(@PathVariable UUID deckId, @PathVariable UUID flashcardId, @RequestParam int quality) {
        reviewFlashcardUseCase.execute(new ReviewFlashcardUseCase.Input(deckId, flashcardId, quality));
        return "redirect:/decks/" + deckId + "/review";
    }
}
