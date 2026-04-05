package com.example.estudodeck.infrastructure.controllers;

import com.example.estudodeck.application.usecases.AddTagToDeckUseCase;
import com.example.estudodeck.application.usecases.RemoveTagFromDeckUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/decks/{deckId}/tags")
public class TagController {

    private final AddTagToDeckUseCase addTagToDeckUseCase;
    private final RemoveTagFromDeckUseCase removeTagFromDeckUseCase;

    public TagController(AddTagToDeckUseCase addTagToDeckUseCase, RemoveTagFromDeckUseCase removeTagFromDeckUseCase) {
        this.addTagToDeckUseCase = addTagToDeckUseCase;
        this.removeTagFromDeckUseCase = removeTagFromDeckUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> addTag(@PathVariable UUID deckId, @RequestBody TagRequest request) {
        addTagToDeckUseCase.execute(new AddTagToDeckUseCase.Input(deckId, request.tagName()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> removeTag(@PathVariable UUID deckId, @RequestBody TagRequest request) {
        removeTagFromDeckUseCase.execute(new RemoveTagFromDeckUseCase.Input(deckId, request.tagName()));
        return ResponseEntity.noContent().build();
    }

    public record TagRequest(String tagName) {}
}
