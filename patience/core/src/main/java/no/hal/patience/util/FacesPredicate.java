package no.hal.patience.util;

import java.util.List;

import no.hal.patience.Card;
import no.hal.patience.CardOrder;
import no.hal.patience.CardsPredicate;

public abstract class FacesPredicate implements CardsPredicate {

    private final int face;
    private final CardOrder cardOrder;

    public FacesPredicate(int face, CardOrder cardOrder) {
        this.face = face;
        this.cardOrder = cardOrder;
    }

    public FacesPredicate(int face) {
        this(face, CardOrder.identity());
    }

    protected abstract boolean test(int initialFace, int actualFace, int pos);

    @Override
    public boolean test(Card card1, Card card2) {
        int face = this.face;
        if (face < 0) {
            face = cardOrder.ordinal(card1.getCardKind());
        }
        return test(face, cardOrder.ordinal(card1.getCardKind()), 0)
            && test(face, cardOrder.ordinal(card2.getCardKind()), 1);
    }

    @Override
    public boolean test(List<Card> cards) {
        if (cards.size() == 0) {
            return true;
        }
        int face = this.face;
        if (face < 0) {
            face = cardOrder.ordinal(cards.get(0).getCardKind());
        }
        for (int i = 0; i < cards.size(); i++) {
            if (! test(face, cardOrder.ordinal(cards.get(i).getCardKind()), i)) {
                return false;
            }
        }
        return true;
    }

    //

    static FacesPredicate stepping(int face, int d, CardOrder cardOrder) {
        return new FacesPredicate(face, cardOrder) {
            protected boolean test(int initialFace, int actualFace, int pos) {
                return actualFace - initialFace == pos * d;
            }
        };
    }

    public static FacesPredicate increasingFrom(int face, CardOrder cardOrder) {
        return stepping(face, 1, cardOrder);
    }
    public static FacesPredicate increasingFrom(int face) {
        return increasingFrom(face, CardOrder.identity());
    }

    public static FacesPredicate increasing(CardOrder cardOrder) {
        return stepping(-1, 1, cardOrder);
    }
    public static FacesPredicate increasing() {
        return increasing(CardOrder.identity());
    }

    public static FacesPredicate decreasingFrom(int face, CardOrder cardOrder) {
        return stepping(face, -1, cardOrder);
    }
    public static FacesPredicate decreasingFrom(int face) {
        return decreasingFrom(face, CardOrder.identity());
    }

    public static FacesPredicate decreasing(CardOrder cardOrder) {
        return stepping(-1, -1, cardOrder);
    }
    public static FacesPredicate decreasing() {
        return decreasing(CardOrder.identity());
    }

    public static FacesPredicate sameAs(int face, CardOrder cardOrder) {
        return stepping(face, 0, cardOrder);
    }
    public static FacesPredicate sameAs(int face) {
        return sameAs(face, CardOrder.identity());
    }

    public static FacesPredicate same(CardOrder cardOrder) {
        return stepping(-1, 0, cardOrder);
    }
    public static FacesPredicate same() {
        return same(CardOrder.identity());
    }
}