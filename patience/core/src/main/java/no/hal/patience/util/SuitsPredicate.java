package no.hal.patience.util;

import java.util.List;

import no.hal.patience.Card;
import no.hal.patience.SuitKind;

public abstract class SuitsPredicate implements CardsPredicate {

    private final SuitKind suit;

    public SuitsPredicate(SuitKind suitKind) {
        this.suit = suitKind;
    }

    @Override
    public String toString() {
        return "[SuitsPredicate suit=" + suit + "]";
    }

    protected abstract boolean test(SuitKind initialSuitKind, SuitKind actualSuitKind, int pos);

    @Override
    public boolean test(Card card1, Card card2) {
        SuitKind suit = this.suit;
        if (suit == null) {
            suit = card1.getSuit();
        }
        return test(suit, card1.getSuit(), 0) && test(suit, card2.getSuit(), 1);
    }

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
        return true;
    }

    //

    static SuitsPredicate sameSuit(SuitKind suitKind) {
        return new SuitsPredicate(suitKind) {
            protected boolean test(SuitKind initialSuitKind, SuitKind actualSuitKind, int pos) {
                return initialSuitKind == actualSuitKind;
            }
        };
    }
    
    public static SuitsPredicate same() {
        return sameSuit(null);
    }

    public static SuitsPredicate sameAs(SuitKind suitKind) {
        return sameSuit(suitKind);
    }

    public static SuitsPredicate alternatingColor() {
        return new SuitsPredicate(null) {
            protected boolean test(SuitKind initialSuitKind, SuitKind actualSuitKind, int pos) {
                return (initialSuitKind.getColor() == actualSuitKind.getColor() == (pos % 2 == 0));
            }
        };
    }
}