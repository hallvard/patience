package no.hal.patience;

import java.util.List;
import java.util.function.BiFunction;

import no.hal.patience.util.Pair;

public interface PilesOperation extends BiFunction<List<Card>, List<Card>, Pair<List<Card>, List<Card>>> {

    public static PilesOperation of(BiFunction<List<Card>, List<Card>, Pair<List<Card>, List<Card>>> op) {
        return (cards1, cards2) -> op.apply(cards1, cards2);
    }

    default void apply(Pile pile1, Pile pile2) {
        Pair<List<Card>, List<Card>> result = apply(pile1.getAllCards(), pile2.getAllCards());
        pile1.setAllCards(result.get1());
        pile2.setAllCards(result.get2());
    }
}