package no.hal.patience;

import java.util.List;
import java.util.function.BiFunction;

public interface CardsCardOperation extends BiFunction<List<Card>, Card, List<Card>> {
    
    public static CardsCardOperation of(BiFunction<List<Card>, Card, List<Card>> fun) {
        return (cards, card) -> fun.apply(cards, card);
    }
}