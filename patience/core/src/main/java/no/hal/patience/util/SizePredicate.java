package no.hal.patience.util;

import java.util.List;

import no.hal.patience.Card;

public class SizePredicate implements CardsPredicate {

    private final int min, max;

    private SizePredicate(int min, int max) {
        this.min = min;
        this.max = max;
    }

    private SizePredicate(int size) {
        this(size, size);
    }

    @Override
    public boolean test(Card card1, Card card2) {
        return test(2);
    }

    protected boolean test(int size) {
        if (min >= 0 && size < min) {
            return false;
        }
        if (max >= 0 && size < max) {
            return false;
        }
        return true;
    }

    @Override
    public boolean test(List<Card> cards) {
        return test(cards.size());
    }

    //

    public static SizePredicate sizeBetween(int min, int max) {
        return new SizePredicate(min, max);
    }

    public static SizePredicate sizeIs(int size) {
        return new SizePredicate(size);
    }

    public static SizePredicate sizeIs1() {
        return sizeIs(1);
    }

    public static SizePredicate sizeIs13() {
        return sizeIs(13);
    }
}