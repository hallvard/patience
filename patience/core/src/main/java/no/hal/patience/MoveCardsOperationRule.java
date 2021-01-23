package no.hal.patience;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import no.hal.patience.util.CardsPredicate;

public class MoveCardsOperationRule<P extends Enum<P>> implements PilesOperationRule<P> {

    private Enum<P> sourcePileKind;
    private Enum<P> targetPileKind;
    private final int count;

    private final boolean reversed;
    private final boolean turning;

    public MoveCardsOperationRule(Enum<P> sourcePileKind, Enum<P> targetPileKind, int count, boolean reversed, boolean turning) {
        this.sourcePileKind = sourcePileKind;
        this.targetPileKind = targetPileKind;
        this.count = count;
        this.reversed = reversed;
        this.turning = turning;
    }
    public MoveCardsOperationRule(Enum<P> sourcePileKind, Enum<P> targetPileKind, int count) {
        this(sourcePileKind, targetPileKind, count, false, false);
    }
    public MoveCardsOperationRule(Enum<P> sourcePileKind, Enum<P> targetPileKind) {
        this(sourcePileKind, targetPileKind, -1);
    }

    public int getCount() {
        return count;
    }

    private CardsPredicate sourcePreCondition = null;

    public void setsourcePreCondition(CardsPredicate sourcePreCondition) {
        this.sourcePreCondition = sourcePreCondition;
    }

    private CardsPredicate targetPreConditon = null;

    public void settargetPreConditon(CardsPredicate targetPreConditon) {
        this.targetPreConditon = targetPreConditon;
    }

    private CardsPredicate movedCardsConstraint = null;

    public void setMovedCardsConstraint(CardsPredicate movedCardsConstraint) {
        this.movedCardsConstraint = movedCardsConstraint;
    }

    private CardsPredicate combinedCardsConstraint = null;

    public void setCombinedCardsConstraint(CardsPredicate combinedCardsConstraint) {
        this.combinedCardsConstraint = combinedCardsConstraint;
    }

    private CardsPredicate sourcePostCondition = null;

    public void setsourcePostCondition(CardsPredicate sourcePostCondition) {
        this.sourcePostCondition = sourcePostCondition;
    }

    private CardsPredicate targetPostCondition = null;

    public void settargetPostCondition(CardsPredicate targetPostCondition) {
        this.targetPostCondition = targetPostCondition;
    }

    protected boolean canApply(List<Card> source, int cardCount, List<Card> target) {
        if (sourcePreCondition != null && (! sourcePreCondition.test(source))) {
            return false;
        }
        if (targetPreConditon != null && (! targetPreConditon.test(source))) {
            return false;
        }
    
        List<Card> movedCards = Pile.getTopCards(source, cardCount);
        if (movedCardsConstraint != null && (! movedCardsConstraint.test(movedCards))) {
            return false;
        }
        List<Card> pile1 = Pile.removedCards(source, source.size() - cardCount, source.size());
        if (combinedCardsConstraint != null && (! combinedCardsConstraint.test(Pile.getTopCard(target), Pile.getBottomCard(movedCards)))) {
            return false;
        }

        if (sourcePostCondition != null && (! sourcePostCondition.test(pile1))) {
            return false;
        }
        if (reversed) {
            Collections.reverse(movedCards);
        }
        if (targetPostCondition != null && (! targetPostCondition.test(Pile.addedCards(target, movedCards)))) {
            return false;
        }
        return true;
    }

    protected boolean canApply(Pile pile1, int cardCount, Pile pile2) {
        return canApply(pile1.getAllCards(), cardCount, pile2.getAllCards());
    }

    //

    protected boolean validateConstraints(Patience<P> patience, Pile source, int sourcePos, Pile target, int targetPos, boolean checkSourceConstraints, boolean checkTargetConstraints) {
        if (checkSourceConstraints) {
            if (patience.getPileKind(source) != sourcePileKind) {
                return false;
            }
            if (sourcePreCondition != null && (! sourcePreCondition.test(source.getAllCards()))) {
                return false;
            }
        }
        if (checkTargetConstraints) {
            if (patience.getPileKind(target) != targetPileKind) {
                return false;
            }
            if (targetPreConditon != null && (! targetPreConditon.test(target.getAllCards()))) {
                return false;
            }
        }
        int cardCount = source.getCardCount() - sourcePos;
        List<Card> topCards = source.getTopCards(cardCount);
        List<Card> postSourceCards = source.getCards(0, sourcePos);
        List<Card> postTargetCards = target.insertedCards(targetPos, topCards);
        if (checkSourceConstraints) {
            if (movedCardsConstraint != null && (! movedCardsConstraint.test(topCards))) {
                return false;
            }
            if (sourcePostCondition != null && (! sourcePostCondition.test(postSourceCards))) {
                return false;
            }
            if (this.count > 0 && this.count != cardCount) {
                return false;
            }
        }
        if (checkTargetConstraints) {
            if (targetPostCondition != null && (! targetPostCondition.test(postTargetCards))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public MoveCardsOperation accept(Patience<P> patience, Pile source, int sourcePos) {
        if (! validateConstraints(patience, source, sourcePos, null, -1, true, false)) {
            return null;
        }
        List<Pile> possibleTargets = new ArrayList<>();
        Pile target = patience.getPile(targetPileKind);
        if (target != null && validateConstraints(patience, source, sourcePos, target, -1, false, true)) {
            possibleTargets.add(target);
        }
        Collection<Pile> targets = patience.getPiles(targetPileKind);
        for (var pile : targets) {
            if (validateConstraints(patience, source, sourcePos, pile, -1, false, true)) {
                possibleTargets.add(target);
            }
        }
        if (possibleTargets.size() == 1) {
            target = possibleTargets.get(0);
        }
        MoveCardsOperation op = new MoveCardsOperation(source, target, source.getCardCount() - sourcePos, reversed, turning);
        return op;
    }

    @Override
    public MoveCardsOperation accept(Patience<P> patience, Pile source, int sourcePos, Pile target, int targetPos) {
        if (! validateConstraints(patience, source, sourcePos, target, targetPos, true, true)) {
            return null;
        }
        MoveCardsOperation op = new MoveCardsOperation(source, target, source.getCardCount() - sourcePos, reversed, turning);
        return op;
    }
}