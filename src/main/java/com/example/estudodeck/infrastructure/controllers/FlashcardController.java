package com.example.estudodeck.infrastructure.controllers;

import com.example.estudodeck.application.usecases.CreateFlashcardUseCase;
import com.example.estudodeck.application.usecases.DeleteFlashcardUseCase;
import com.example.estudodeck.application.usecases.ReviewFlashcardUseCase;
import com.example.estudodeck.application.usecases.UnsuspendFlashcardUseCase;
import com.example.estudodeck.domain.CardType;
import com.example.estudodeck.domain.Flashcard;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/decks/{deckId}/flashcards")
public class FlashcardController {

    private final CreateFlashcardUseCase createFlashcardUseCase;
    private final ReviewFlashcardUseCase reviewFlashcardUseCase;
    private final DeleteFlashcardUseCase deleteFlashcardUseCase;
    private final UnsuspendFlashcardUseCase unsuspendFlashcardUseCase;

    public FlashcardController(CreateFlashcardUseCase createFlashcardUseCase, ReviewFlashcardUseCase reviewFlashcardUseCase, DeleteFlashcardUseCase deleteFlashcardUseCase, UnsuspendFlashcardUseCase unsuspendFlashcardUseCase) {
        this.createFlashcardUseCase = createFlashcardUseCase;
        this.reviewFlashcardUseCase = reviewFlashcardUseCase;
        this.deleteFlashcardUseCase = deleteFlashcardUseCase;
        this.unsuspendFlashcardUseCase = unsuspendFlashcardUseCase;
    }

    @PostMapping
    public Flashcard create(@PathVariable UUID deckId, @RequestBody CreateFlashcardRequest request) {
        CreateFlashcardUseCase.Input input = new CreateFlashcardUseCase.Input(deckId, request.type(), request.contentFields());
        return createFlashcardUseCase.execute(input);
    }

    @PostMapping("/{flashcardId}/review")
    public ResponseEntity<Void> review(@PathVariable UUID deckId, @PathVariable UUID flashcardId, @RequestBody ReviewRequest request) {
        reviewFlashcardUseCase.execute(new ReviewFlashcardUseCase.Input(deckId, flashcardId, request.quality()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{flashcardId}")
    public ResponseEntity<Void> delete(@PathVariable UUID deckId, @PathVariable UUID flashcardId) {
        deleteFlashcardUseCase.execute(new DeleteFlashcardUseCase.Input(deckId, flashcardId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{flashcardId}/unsuspend")
    public ResponseEntity<Void> unsuspend(@PathVariable UUID deckId, @PathVariable UUID flashcardId) {
        unsuspendFlashcardUseCase.execute(new UnsuspendFlashcardUseCase.Input(deckId, flashcardId));
        return ResponseEntity.ok().build();
    }

    public record CreateFlashcardRequest(CardType type, Map<String, String> contentFields) {}
    public record ReviewRequest(int quality) {}
}
