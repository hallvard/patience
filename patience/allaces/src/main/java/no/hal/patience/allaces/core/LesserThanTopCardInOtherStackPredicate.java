package no.hal.patience.allaces.core;

import java.util.List;

import no.hal.patience.Card;
import no.hal.patience.CardOrder;
import no.hal.patience.Pile;
import no.hal.patience.util.CardsPredicate;

public class LesserThanTopCardInOtherStackPredicate implements CardsPredicate {

    private CardOrder cardOrder;
    private final Pile[] stacks;

    public LesserThanTopCardInOtherStackPredicate(CardOrder cardOrder, Pile... stacks) {
        this.cardOrder = cardOrder;
        this.stacks = stacks.clone();
    }

    @Override
    public boolean test(List<Card> cards) {
        for (var stack : stacks) {
            Card topCard = stack.getTopCard();
            if (topCard != null && cardOrder.lessThan(cards.get(cards.size() - 1), topCard)) {
                return true;
            }
        }
        return false;
    }
}