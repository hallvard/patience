package no.hal.patience;

import java.util.List;
import java.util.function.Predicate;

@FunctionalInterface
public interface CardsPredicate extends Predicate<List<Card>> {

    default public boolean test(Card card1, Card card2) {
        return test(List.of(card1, card2));
    }

    public static CardsPredicate of(Predicate<List<Card>> pred) {
        return cards -> pred.test(cards);
    }

    default public CardsPredicate and(CardsPredicate other) {
        return cards -> test(cards) && other.test(cards);
    }

    default public CardsPredicate or(CardsPredicate other) {
        return cards -> test(cards) || other.test(cards);
    }

    default public CardsPredicate negate() {
        return cards -> ! test(cards);
    }
}
