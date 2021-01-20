package no.hal.patience;

import java.util.Collections;
import java.util.List;

import no.hal.patience.util.CardsPredicate;
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

    public int getCount() {
        return count;
    }

    public MoveCardsOperation withCount(int count) {
        MoveCardsOperation mco = new MoveCardsOperation(count, reversed, turning);
        mco.cards1BeforeConstraint = cards1BeforeConstraint;
        mco.cards2BeforeConstraint = cards2BeforeConstraint;
        mco.movedCardsConstraint = movedCardsConstraint;
        mco.combinedCardsConstraint = combinedCardsConstraint;
        mco.cards1AfterConstraint = cards1AfterConstraint;
        mco.cards2AfterConstraint = cards2AfterConstraint;
        return mco;
    }

    private CardsPredicate cards1BeforeConstraint = null;

    public void setCards1BeforeConstraint(CardsPredicate cards1BeforeConstraint) {
        this.cards1BeforeConstraint = cards1BeforeConstraint;
    }

    private CardsPredicate cards2BeforeConstraint = null;

    public void setCards2BeforeConstraint(CardsPredicate cards2BeforeConstraint) {
        this.cards2BeforeConstraint = cards2BeforeConstraint;
    }

    private CardsPredicate movedCardsConstraint = null;

    public void setMovedCardsConstraint(CardsPredicate movedCardsConstraint) {
        this.movedCardsConstraint = movedCardsConstraint;
    }

    private CardsPredicate combinedCardsConstraint = null;

    public void setCombinedCardsConstraint(CardsPredicate combinedCardsConstraint) {
        this.combinedCardsConstraint = combinedCardsConstraint;
    }

    private CardsPredicate cards1AfterConstraint = null;

    public void setCards1AfterConstraint(CardsPredicate cards1AfterConstraint) {
        this.cards1AfterConstraint = cards1AfterConstraint;
    }

    private CardsPredicate cards2AfterConstraint = null;

    public void setCards2AfterConstraint(CardsPredicate cards2AfterConstraint) {
        this.cards2AfterConstraint = cards2AfterConstraint;
    }

    //

    @Override
    public boolean canApply(List<Card> cards1, List<Card> cards2) {
        if (cards1BeforeConstraint != null && (! cards1BeforeConstraint.test(cards1))) {
            return false;
        }
        if (cards2BeforeConstraint != null && (! cards2BeforeConstraint.test(cards1))) {
            return false;
        }
    
        List<Card> movedCards = Pile.getTopCards(cards1, count);
        if (movedCardsConstraint != null && (! movedCardsConstraint.test(movedCards))) {
            return false;
        }
        List<Card> pile1 = Pile.removedCards(cards1, cards1.size() - count, cards1.size());
        if (combinedCardsConstraint != null && (! combinedCardsConstraint.test(Pile.getTopCard(cards2), Pile.getBottomCard(movedCards)))) {
            return false;
        }

        if (cards1AfterConstraint != null && (! cards1AfterConstraint.test(pile1))) {
            return false;
        }
        if (reversed) {
            Collections.reverse(movedCards);
        }
        if (cards2AfterConstraint != null && (! cards2AfterConstraint.test(Pile.addedCards(cards2, movedCards)))) {
            return false;
        }
        return true;
    }

    @Override
    public Pair<List<Card>, List<Card>> apply(List<Card> cards1, List<Card> cards2) {
        List<Card> movedCards = Pile.getTopCards(cards1, count);
        List<Card> pile1 = Pile.removedCards(cards1, cards1.size() - count, cards1.size());
        if (reversed) {
            Collections.reverse(movedCards);
        }
        if (turning) {
            for (Card card : movedCards) {
                card.turn();
            }
        }
        List<Card> pile2 = Pile.addedCards(cards2, movedCards);
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