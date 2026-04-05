package com.example.estudodeck.application.usecases;

import com.example.estudodeck.domain.BasicCardContent;
import com.example.estudodeck.domain.CardContent;
import com.example.estudodeck.domain.CardType;
import com.example.estudodeck.domain.ClozeCardContent;

import java.util.Map;

public class CardContentFactory {
    public static CardContent create(CardType type, Map<String, String> fields) {
        return switch (type) {
            case BASIC -> new BasicCardContent(fields.get("question"), fields.get("answer"));
            case CLOZE -> new ClozeCardContent(fields.get("text"));
            default -> throw new IllegalArgumentException("Invalid card type: " + type);
        };
    }
}
