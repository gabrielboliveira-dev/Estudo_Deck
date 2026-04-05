package com.example.estudodeck.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClozeCardContent implements CardContent {

    private final String text;

    public ClozeCardContent(String text) {
        if (text == null || text.isBlank() || !text.contains("{{c1::")) {
            throw new IllegalArgumentException("Cloze text must contain a cloze deletion (e.g., {{c1::text}})");
        }
        this.text = text;
    }

    @Override
    public String getPrompt() {
        return text.replaceAll("\\{\\{c1::(.*?)\\}\\}", "[...]");
    }

    @Override
    public String getFullContent() {
        return text.replaceAll("\\{\\{c1::(.*?)\\}\\}", "$1");
    }

    @Override
    public CardType getType() {
        return CardType.CLOZE;
    }

    public String getText() {
        return text;
    }
}
