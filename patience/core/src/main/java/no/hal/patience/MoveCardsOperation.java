package no.hal.patience;

import java.util.Collections;
import java.util.List;

import no.hal.patience.util.Pair;

public class MoveCardsOperation implements PilesOperation {

    private final int count;
    private final boolean reversed;
    private final boolean turning;

    public MoveCardsOperation(int count, boolean reversed, boolean turning) {
        this.count = count;
        this.reversed = reversed;
        this.turning = turning;
    }

    @Override
    public Pair<List<Card>, List<Card>> apply(List<Card> t, List<Card> u) {
        List<Card> topCards = Pile.getTopCards(t, count);
        List<Card> pile1 = Pile.removedCards(t, t.size() - count, t.size());
        if (reversed) {
            Collections.reverse(topCards);
        }
        if (turning) {
            for (Card card : topCards) {
                card.turn();
            }
        }
        List<Card> pile2 = Pile.addedCards(u, topCards);
        return new Pair<List<Card>,List<Card>>(pile1, pile2);
    }
    
    //

    static void moveCards(Pile source, Pile target, int cardCount, boolean reverse, boolean turn) {
        List<Card> cards = source.takeCards(cardCount, reverse);
        if (turn) {
            for (Card card : cards) {
                card.turn();
            }
        }
        target.addCards(cards);
    }

    public static void moveCards(Pile source, Pile target, int cardCount) {
        moveCards(source, target, cardCount, false, false);
    }

    public static void moveCard(Pile source, Pile target) {
        moveCards(source, target, 1);
    }

    public static void moveCardsReversed(Pile source, Pile target, int cardCount) {
        moveCards(source, target, cardCount, true, false);
    }

    public static void moveCardsTurning(Pile source, Pile target, int cardCount) {
        moveCards(source, target, cardCount, false, true);
    }

    public static void moveCardsReversedTurning(Pile source, Pile target, int cardCount) {
        moveCards(source, target, cardCount, true, true);
    }
}