package com.example.estudodeck.application.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class DeckHierarchyDto {
    private UUID id;
    private String name;
    private List<DeckHierarchyDto> children = new ArrayList<>();

    public DeckHierarchyDto(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
