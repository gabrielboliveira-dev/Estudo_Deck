package com.example.estudodeck.infrastructure.cram;

import com.example.estudodeck.domain.Deck;
import com.example.estudodeck.domain.Flashcard;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
@SessionScope
public class CramSessionManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private Queue<Flashcard> cardQueue;
    @Getter
    private String deckName;
    @Getter
    private boolean active = false;

    public void start(Deck deck) {
        this.deckName = deck.getName();
        List<Flashcard> shuffledCards = new java.util.ArrayList<>(deck.getCards());
        Collections.shuffle(shuffledCards);
        this.cardQueue = new LinkedList<>(shuffledCards);
        this.active = true;
    }

    public Flashcard getNextCard() {
        return cardQueue.poll();
    }

    public boolean hasNext() {
        return cardQueue != null && !cardQueue.isEmpty();
    }

    public void end() {
        this.cardQueue = null;
        this.deckName = null;
        this.active = false;
    }
}
