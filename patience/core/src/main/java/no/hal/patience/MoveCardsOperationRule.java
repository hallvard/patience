package no.hal.patience;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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

    public void setSourcePreCondition(CardsPredicate sourcePreCondition) {
        this.sourcePreCondition = sourcePreCondition;
    }

    private CardsPredicate targetPreConditon = null;

    public void setTargetPreConditon(CardsPredicate targetPreConditon) {
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

    public void setSourcePostCondition(CardsPredicate sourcePostCondition) {
        this.sourcePostCondition = sourcePostCondition;
    }

    private CardsPredicate targetPostCondition = null;

    public void setTargetPostCondition(CardsPredicate targetPostCondition) {
        this.targetPostCondition = targetPostCondition;
    }

    public Function<Pile, Integer> defaultPileCardCount = null;

    public void setDefaultPileCardCount(Function<Pile, Integer> defaultPileCardCount) {
        this.defaultPileCardCount = defaultPileCardCount;
    }
    
    public Function<Integer, Integer> defaultCardCount = null;

    public void setDefaultCardCount(Function<Integer, Integer> defaultCardCount) {
        this.defaultCardCount = defaultCardCount;
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

    protected boolean validateConstraints(Patience<P> patience, Pile source, int cardCount, Pile target, int targetPos, boolean checkSourceConstraints, boolean checkTargetConstraints) {
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
        List<Card> topCards = source.getTopCards(cardCount);
        if (checkSourceConstraints) {
            if (movedCardsConstraint != null && (! movedCardsConstraint.test(topCards))) {
                return false;
            }
            List<Card> postSourceCards = source.getCards(0, source.getCardCount() - cardCount);
            // test the pile's own constraint
            if (! source.validateConstraint(postSourceCards)) {
                return false;
            }
            // test this rule's constraint
            if (sourcePostCondition != null && (! sourcePostCondition.test(postSourceCards))) {
                return false;
            }
            if (this.count > 0 && this.count != cardCount) {
                return false;
            }
        }
        if (checkTargetConstraints) {
            List<Card> postTargetCards = target.insertedCards(targetPos, topCards);
            // test the pile's own constraint
            if (! target.validateConstraint(postTargetCards)) {
                return false;
            }
            // test this rule's constraint
            if (targetPostCondition != null && (! targetPostCondition.test(postTargetCards))) {
                return false;
            }
        }
        return true;
    }

    private int getDefaultCardCount(Pile source) {
        if (count > 0) {
            return count;
        }
        if (defaultPileCardCount != null) {
            return defaultPileCardCount.apply(source);
        }
        if (defaultCardCount != null) {
            return defaultCardCount.apply(source.getCardCount());
        }
        return 1;
    }

    @Override
    public MoveCardsOperation accept(Patience<P> patience, Pile source, int cardCount) {
        if (cardCount < 0) {
            cardCount = getDefaultCardCount(source);
        }
        if (! validateConstraints(patience, source, cardCount, null, -1, true, false)) {
            return null;
        }
        List<Pile> possibleTargets = new ArrayList<>();
        Pile target = null;
        if (patience.hasPile(targetPileKind)) {
            target = patience.getPile(targetPileKind);
            if (target != null && validateConstraints(patience, source, cardCount, target, target.getCardCount(), false, true)) {
                possibleTargets.add(target);
            }
        }
        if (patience.hasPiles(targetPileKind)) {
            Collection<Pile> targets = patience.getPiles(targetPileKind);
            for (var pile : targets) {
                if (validateConstraints(patience, source, cardCount, pile, pile.getCardCount(), false, true)) {
                    possibleTargets.add(pile);
                }
            }
        }
        // System.out.println("Possible targets (" + possibleTargets.size() + "): " + possibleTargets);
        if (possibleTargets.size() >= 1) {
            target = possibleTargets.get(0);
        }
        if (target == null) {
            return null;
        }
        MoveCardsOperation op = new MoveCardsOperation(source, target, cardCount, reversed, turning);
        return op;
    }

    @Override
    public MoveCardsOperation accept(Patience<P> patience, Pile source, int cardCount, Pile target, int targetPos) {
        if (cardCount < 0) {
            cardCount = getDefaultCardCount(source);
        }
        if (! validateConstraints(patience, source, cardCount, target, targetPos, true, true)) {
            return null;
        }
        MoveCardsOperation op = new MoveCardsOperation(source, target, cardCount, reversed, turning);
        return op;
    }
}