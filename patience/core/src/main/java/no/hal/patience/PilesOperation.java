package no.hal.patience;

import java.util.List;
import java.util.function.BiFunction;

import no.hal.patience.util.Pair;

public interface PilesOperation {

    boolean canApply(List<Card> cards1, List<Card> cards2);
    Pair<List<Card>, List<Card>> apply(List<Card> cards1, List<Card> cards2);
    
    default boolean canApply(Pile pile1, Pile pile2) {
        return canApply(pile1.getAllCards(), pile2.getAllCards());
    }

    default Pair<List<Card>, List<Card>> apply(Pile pile1, Pile pile2) {
        return apply(pile1.getAllCards(), pile2.getAllCards());
    }

    //

    public static PilesOperation of(BiFunction<List<Card>, List<Card>, Pair<List<Card>, List<Card>>> op) {
        return new PilesOperation() {

			@Override
			public boolean canApply(List<Card> cards1, List<Card> cards2) {
                try {
                    op.apply(cards1, cards2);
                    return true;
                } catch (Exception e) {
                    return false;
                }
			}

			@Override
			public Pair<List<Card>, List<Card>> apply(List<Card> cards1, List<Card> cards2) {
                return op.apply(cards1, cards2);
			}
        };
    }
}