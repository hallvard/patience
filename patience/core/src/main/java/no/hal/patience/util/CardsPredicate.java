package no.hal.patience.util;

import java.util.List;
import java.util.function.Predicate;

import no.hal.patience.Card;

@FunctionalInterface
public interface CardsPredicate extends Predicate<List<Card>> {

    default boolean test(Card card1, Card card2) {
        return test(List.of(card1, card2));
    }

    public static CardsPredicate of(Predicate<List<Card>> pred) {
        return cards -> pred.test(cards);
    }

    default CardsPredicate and(CardsPredicate other) {
        return cards -> test(cards) && other.test(cards);
    }

    default CardsPredicate or(CardsPredicate other) {
        return cards -> test(cards) || other.test(cards);
    }

    default CardsPredicate negate() {
        return cards -> ! test(cards);
    }

    //

    public static CardsPredicate whatever = cards -> true;
}
