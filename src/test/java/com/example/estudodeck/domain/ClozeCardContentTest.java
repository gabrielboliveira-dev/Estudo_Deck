package com.example.estudodeck.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClozeCardContentTest {

    @Test
    void shouldCreatePromptFromCloze() {
        ClozeCardContent content = new ClozeCardContent("A capital do Brasil é {{c1::Brasília}}.");
        assertEquals("A capital do Brasil é [...].", content.getPrompt());
    }

    @Test
    void shouldGetFullContentFromCloze() {
        ClozeCardContent content = new ClozeCardContent("A capital do Brasil é {{c1::Brasília}}.");
        assertEquals("A capital do Brasil é Brasília.", content.getFullContent());
    }

    @Test
    void shouldThrowExceptionIfClozeIsMissing() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ClozeCardContent("A capital do Brasil é Brasília.");
        });
    }
}
