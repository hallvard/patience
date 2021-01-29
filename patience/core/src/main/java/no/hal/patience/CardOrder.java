package no.hal.patience;

import java.util.function.Function;

public interface CardOrder extends Function<CardKind, Integer> {

    default public int ordinal(CardKind cardKind) {
        return apply(cardKind);
    }

    default public int ordinal(Card card) {
        return ordinal(card.getCardKind());
    }

    //

    public static CardOrder of(Function<CardKind, Integer> fun) {
        return new CardOrder() {
            public Integer apply(CardKind cardKind) {
                return fun.apply(cardKind);
            }
        };
    }

    public static CardOrder aceIs(int aceOrdinal) {
        return of(cardKind -> cardKind.getFace() == 0 ? aceOrdinal : cardKind.getFace());
    }

    public static CardOrder identity() {
        return of(cardKind -> cardKind.getFace());
    }

    public static CardOrder aceIs1() {
        return aceIs(1);
    }

    public static CardOrder aceIs14() {
        return aceIs(14);
    }

    public static CardOrder startsAt(int startFace) {
        return of(cardKind -> (cardKind.getFace() + 13 - startFace) % 13 + 1);
    }

    //

    default boolean equal(Card card1, Card card2) {
        return ordinal(card1) == ordinal(card2);
    }

    default boolean lessThan(Card card1, Card card2) {
        return ordinal(card1) < ordinal(card2);
    }

    default boolean lessThanEqual(Card card1, Card card2) {
        return ordinal(card1) <= ordinal(card2);
    }

    default boolean greaterThan(Card card1, Card card2) {
        return ordinal(card1) > ordinal(card2);
    }

    default boolean greaterThanEqual(Card card1, Card card2) {
        return ordinal(card1) >= ordinal(card2);
    }
}
