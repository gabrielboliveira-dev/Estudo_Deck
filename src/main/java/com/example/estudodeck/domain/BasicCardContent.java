package com.example.estudodeck.domain;

public class BasicCardContent implements CardContent {
    private String question;
    private String answer;

    public BasicCardContent(String question, String answer) {
        if (question == null || question.isBlank()) throw new IllegalArgumentException("Question cannot be empty");
        if (answer == null || answer.isBlank()) throw new IllegalArgumentException("Answer cannot be empty");
        this.question = question;
        this.answer = answer;
    }

    @Override
    public String getPrompt() {
        return this.question;
    }

    @Override
    public String getFullContent() {
        return this.answer;
    }

    @Override
    public CardType getType() {
        return CardType.BASIC;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
