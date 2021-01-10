package no.hal.patience;

import java.util.List;
import java.util.function.BinaryOperator;

public interface CardsCardsOperation extends BinaryOperator<List<Card>> {

    public static CardsCardsOperation of(BinaryOperator<List<Card>> op) {
        return (cards1, cards2) -> op.apply(cards1, cards2);
    }
}