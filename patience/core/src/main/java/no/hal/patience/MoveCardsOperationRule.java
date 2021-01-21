package no.hal.patience;

import java.util.Collections;
import java.util.List;

import no.hal.patience.util.CardsPredicate;

public class MoveCardsOperationRule implements PilesOperationRule {

    private String sourceCategoryOrName;
    private String targetCategoryOrName;
    private final int count;

    private final boolean reversed;
    private final boolean turning;

    public MoveCardsOperationRule(String sourceCategoryOrName, String targetCategoryOrName, int count, boolean reversed, boolean turning) {
        this.sourceCategoryOrName = sourceCategoryOrName;
        this.targetCategoryOrName = targetCategoryOrName;
        this.count = count;
        this.reversed = reversed;
        this.turning = turning;
    }

    public int getCount() {
        return count;
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

    protected boolean canApply(List<Card> cards1, int cardCount, List<Card> cards2) {
        if (cards1BeforeConstraint != null && (! cards1BeforeConstraint.test(cards1))) {
            return false;
        }
        if (cards2BeforeConstraint != null && (! cards2BeforeConstraint.test(cards1))) {
            return false;
        }
    
        List<Card> movedCards = Pile.getTopCards(cards1, cardCount);
        if (movedCardsConstraint != null && (! movedCardsConstraint.test(movedCards))) {
            return false;
        }
        List<Card> pile1 = Pile.removedCards(cards1, cards1.size() - cardCount, cards1.size());
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

    protected boolean canApply(Pile pile1, int cardCount, Pile pile2) {
        return canApply(pile1.getAllCards(), cardCount, pile2.getAllCards());
    }

    //

    @Override
    public MoveCardsOperation accept(Pile source, int sourcePos) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MoveCardsOperation accept(Pile source, int sourcePos, Pile target, int targetPos) {
        // TODO Auto-generated method stub
        return null;
    }
}