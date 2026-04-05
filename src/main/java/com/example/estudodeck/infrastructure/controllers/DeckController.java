package com.example.estudodeck.infrastructure.controllers;

import com.example.estudodeck.application.usecases.CreateDeckUseCase;
import com.example.estudodeck.application.usecases.DeleteDeckUseCase;
import com.example.estudodeck.application.usecases.UpdateDeckUseCase;
import com.example.estudodeck.domain.Deck;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/decks")
public class DeckController {

    private final CreateDeckUseCase createDeckUseCase;
    private final UpdateDeckUseCase updateDeckUseCase;
    private final DeleteDeckUseCase deleteDeckUseCase;


    public DeckController(CreateDeckUseCase createDeckUseCase, UpdateDeckUseCase updateDeckUseCase, DeleteDeckUseCase deleteDeckUseCase) {
        this.createDeckUseCase = createDeckUseCase;
        this.updateDeckUseCase = updateDeckUseCase;
        this.deleteDeckUseCase = deleteDeckUseCase;
    }

    @PostMapping
    public Deck create(@RequestBody CreateDeckRequest request) {
        return createDeckUseCase.execute(new CreateDeckUseCase.Input(request.name(), null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable UUID id, @RequestBody UpdateDeckRequest request) {
        updateDeckUseCase.execute(new UpdateDeckUseCase.Input(id, request.name()));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteDeckUseCase.execute(new DeleteDeckUseCase.Input(id));
        return ResponseEntity.noContent().build();
    }

    public record CreateDeckRequest(String name) {}
    public record UpdateDeckRequest(String name) {}
}
