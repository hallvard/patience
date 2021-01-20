package no.hal.patience.util;

import java.util.List;

import no.hal.patience.Card;

public interface CardsListener<T extends Cards> {
    void cardsChanged(T cards, List<Card> oldCards, List<Card> newCards);
}