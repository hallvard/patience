package no.hal.patience;

import java.util.function.Function;

public interface CardOrder extends Function<CardKind, Integer> {

    default public int ordinal(CardKind cardKind) {
        return apply(cardKind);
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
}
