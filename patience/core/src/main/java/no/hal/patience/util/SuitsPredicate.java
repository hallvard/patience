package no.hal.patience.util;

import java.util.List;

import no.hal.patience.Card;
import no.hal.patience.CardsPredicate;
import no.hal.patience.SuitKind;

public abstract class SuitsPredicate implements CardsPredicate {

    private final SuitKind suit;

    public SuitsPredicate(SuitKind suitKind) {
        this.suit = suitKind;
    }
    protected abstract boolean test(SuitKind expectedSuitKind, SuitKind actualSuitKind, int pos);

    @Override
    public boolean test(List<Card> cards) {
        if (cards.size() == 0) {
            return true;
        }
        SuitKind suit = this.suit;
        if (suit == null) {
            suit = cards.get(0).getSuit();
        }
        for (int i = 0; i < cards.size(); i++) {
            if (! test(suit, cards.get(i).getSuit(), i)) {
                return false;
            }
        }
        return false;
    }

    //

    public static SuitsPredicate sameSuit(SuitKind suitKind) {
        return new SuitsPredicate(suitKind) {
            protected boolean test(SuitKind expectedSuitKind, SuitKind actualSuitKind, int pos) {
                return expectedSuitKind == actualSuitKind;
            }
        };
    }
    
    public static SuitsPredicate sameSpecificSuit(SuitKind suitKind) {
        return sameSuit(null);
    }

    public static SuitsPredicate alernatingColor() {
        return new SuitsPredicate(null) {
            protected boolean test(SuitKind expectedSuitKind, SuitKind actualSuitKind, int pos) {
                return (expectedSuitKind.getColor() == actualSuitKind.getColor() == (pos % 2 == 0));
            }
        };
    }
}