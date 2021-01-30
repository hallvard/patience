package no.hal.patience;

import java.util.Collections;
import java.util.List;

public class MoveCardsOperation implements PilesOperation {

    public static class Options {
        private boolean reversed = false;
        private boolean turning = false;
        private int autoRevealTopCards = 0;

        public boolean isReversed() {
            return reversed;
        }

        public boolean isTurning() {
            return turning;
        }

        public int getAutoRevealTopCards() {
            return autoRevealTopCards;
        }

        public boolean isAutoTurningRevealedTopCard() {
            return autoRevealTopCards > 0;
        }

        public Options reversed(boolean reversed) {
            this.reversed = reversed;
            return this;
        }
        public Options reversed() {
            return reversed(true);
        }

        public Options turning(boolean turning) {
            this.turning = turning;
            return this;
        }
        public Options turning() {
            return turning(true);
        }

        public Options autoRevealTopCards(int autoRevealTopCards) {
            this.autoRevealTopCards = autoRevealTopCards;
            return this;
        }
        public Options autoRevealTopCard() {
            return autoRevealTopCards(1);
        }
    }

    private Pile source;
    private final int count;
    private Pile target;
    private final int targetPos;

    private Options options = new Options();

    public MoveCardsOperation(Pile source, int count, Pile target, int targetPos) {
        this.source = source;
        this.count = count;
        this.target = target;
        this.targetPos = targetPos;
    }

    public Pile getSource() {
        return source;
    }

    public int getCount() {
        return count;
    }

    public Pile getTarget() {
        return target;
    }

    public int getTargetPos() {
        return targetPos;
    }

    public MoveCardsOperation options(Options options) {
        this.options = options;
        return this;
    }

    //

    @Override
    public boolean canApply() {
        List<Card> movedCards = source.getTopCards(count);
        List<Card> newSourceCards = source.removedCards(source.getCardCount() - count, source.getCardCount());
        if (! source.validateConstraint(newSourceCards)) {
            return false;
        }
        if (options.isReversed()) {
            Collections.reverse(movedCards);
        }
        // handle special case, where we're moving within pile
        List<Card> targetCards = (target == source ? newSourceCards : target.getAllCards());
        List<Card> newTargetCards = Pile.insertedCards(targetCards, targetPos, movedCards);
        if (! target.validateConstraint(newTargetCards)) {
            return false;
        }
        return true;
    }

    @Override
    public void apply() {
        List<Card> movedCards = source.getTopCards(count);
        source.removeCards(source.getCardCount() - count, source.getCardCount());
        if (options.isReversed()) {
            Collections.reverse(movedCards);
        }
        if (options.isTurning()) {
            for (Card card : movedCards) {
                card.turn();
            }
        }
        if (options.isAutoTurningRevealedTopCard()) {
            source.revealTopCards(options.getAutoRevealTopCards());
        }
        target.insertCards(targetPos, movedCards);
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