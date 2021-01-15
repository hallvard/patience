package no.hal.patience.idiot.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import no.hal.patience.Card;
import no.hal.patience.Patience;
import no.hal.patience.Pile;
import no.hal.patience.SuitKind;
import no.hal.patience.util.SuitsPredicate;

public class Idiot extends Patience {
    
    private final Pile[] stacks = new Pile[SuitKind.values().length];
    private Pile extraCards;
    private final Pile[] suits = new Pile[SuitKind.values().length];

    @Override
    public void initPiles() {
        for (var suit : SuitKind.values()) {
            suits[suit.ordinal()] = Pile.empty(SuitsPredicate.sameAs(suit));
        }
        putPiles("suits", Arrays.asList(suits));

        Pile deck = Pile.deck();
        List<Card> stackCards = deck.takeCards(stacks.length);
        for (int i = 0; i < stackCards.size(); i++) {
            Card card = stackCards.get(i);
            card.setFaceDown(true);
            stacks[i] = Pile.of(card);
        }
        putPiles("stacks", Arrays.asList(suits));
    
        extraCards = Pile.of(deck.takeCards(12));
        putPiles("extras", Arrays.asList(extraCards));

        putPile(Patience.DECK_PILE_NAME, deck);
        putPile("deck2", Pile.empty());
    }

    @Override
    public boolean canDeal() {
        return false;
    }

    @Override
    public boolean isFinished() {
        return false;
    }        
}