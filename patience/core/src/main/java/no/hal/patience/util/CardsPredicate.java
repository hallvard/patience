package no.hal.patience.util;

import java.util.List;
import java.util.function.Predicate;

import no.hal.patience.Card;

@FunctionalInterface
public interface CardsPredicate extends Predicate<List<Card>> {

    default boolean test(Card card1, Card card2) {
        return card1 != null && card2 != null && test(List.of(card1, card2));
    }

    public static CardsPredicate faceUp() {
        return cards -> cards.stream().allMatch(Predicate.not(Card::isFaceDown));
    }

    public static CardsPredicate faceDown() {
        return cards -> cards.stream().allMatch(Card::isFaceDown);
    }

    public static CardsPredicate of(Predicate<List<Card>> pred) {
        return cards -> pred.test(cards);
    }

    default CardsPredicate ofCards(int start, int end) {
        return cards -> start >= 0 && end <= cards.size() && start <= end && test(cards.subList(start, end));
    }

    default CardsPredicate ofBottomCards(int count) {
        return cards -> count <= cards.size() && test(cards.subList(0, count));
    }
    default CardsPredicate ofBottomCard() {
        return ofBottomCards(1);
    }

    default CardsPredicate ofTopCards(int count) {
        return cards -> count <= cards.size() && test(cards.subList(cards.size() - count, cards.size()));
    }
    default CardsPredicate ofTopCard() {
        return ofTopCards(1);
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
