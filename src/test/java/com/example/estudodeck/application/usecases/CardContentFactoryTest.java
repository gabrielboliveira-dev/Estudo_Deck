package com.example.estudodeck.application.usecases;

import com.example.estudodeck.domain.BasicCardContent;
import com.example.estudodeck.domain.CardContent;
import com.example.estudodeck.domain.CardType;
import com.example.estudodeck.domain.ClozeCardContent;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CardContentFactoryTest {

    @Test
    void shouldCreateBasicCardContent() {
        Map<String, String> fields = Map.of("question", "Capital of France?", "answer", "Paris");

        CardContent content = CardContentFactory.create(CardType.BASIC, fields);

        assertTrue(content instanceof BasicCardContent);
        assertEquals("Capital of France?", ((BasicCardContent) content).getQuestion());
        assertEquals("Paris", ((BasicCardContent) content).getAnswer());
    }

    @Test
    void shouldCreateClozeCardContent() {
        Map<String, String> fields = Map.of("text", "The capital of France is {{c1::Paris}}.");

        CardContent content = CardContentFactory.create(CardType.CLOZE, fields);

        assertTrue(content instanceof ClozeCardContent);
        assertEquals("The capital of France is {{c1::Paris}}.", ((ClozeCardContent) content).getText());
    }
}