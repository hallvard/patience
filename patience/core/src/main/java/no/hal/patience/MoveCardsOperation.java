package no.hal.patience;

import java.util.Collections;
import java.util.List;

public class MoveCardsOperation implements PilesOperation {

    private Pile source;
    private Pile target;
    private final int count;
    private final boolean reversed;
    private final boolean turning;

    public MoveCardsOperation(Pile source, Pile target, int count, boolean reversed, boolean turning) {
        this.source = source;
        this.target = target;
        this.count = count;
        this.reversed = reversed;
        this.turning = turning;
    }

    public int getCount() {
        return count;
    }

    //

    @Override
    public boolean canApply() {
        List<Card> movedCards = source.getTopCards(count);
        List<Card> newSourceCards = source.removedCards(source.getCardCount() - count, source.getCardCount());
        if (! source.validateConstraint(newSourceCards)) {
            return false;
        }
        if (reversed) {
            Collections.reverse(movedCards);
        }
        List<Card> newTargetCards = target.addedCards(movedCards);
        if (! target.validateConstraint(newTargetCards)) {
            return false;
        }
        return true;
    }

    @Override
    public void apply() {
        List<Card> movedCards = source.getTopCards(count);
        source.removeCards(source.getCardCount() - count, source.getCardCount());
        if (reversed) {
            Collections.reverse(movedCards);
        }
        if (turning) {
            for (Card card : movedCards) {
                card.turn();
            }
        }
        target.addCards(movedCards);
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