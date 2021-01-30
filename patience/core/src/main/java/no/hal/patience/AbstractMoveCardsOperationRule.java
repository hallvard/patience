package no.hal.patience;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import no.hal.patience.util.CardsPredicate;

public abstract class AbstractMoveCardsOperationRule<P extends Enum<P>> implements PilesOperationRule<P> {

    private Enum<P> sourcePileKind;
    private Enum<P> targetPileKind;
    private final int count;

    private MoveCardsOperation.Options options = new MoveCardsOperation.Options();

    public AbstractMoveCardsOperationRule(Enum<P> sourcePileKind, Enum<P> targetPileKind, int count) {
        this.sourcePileKind = sourcePileKind;
        this.targetPileKind = targetPileKind;
        this.count = count;
    }
    public AbstractMoveCardsOperationRule(Enum<P> sourcePileKind, Enum<P> targetPileKind) {
        this(sourcePileKind, targetPileKind, -1);
    }

    public Enum<P> getSourcePileKind() {
        return sourcePileKind;
    }

    public Enum<P> getTargetPileKind() {
        return targetPileKind;
    }

    public int getCount() {
        return count;
    }

    public MoveCardsOperation.Options getOptions() {
        return options;
    }

    public AbstractMoveCardsOperationRule<P> options(MoveCardsOperation.Options options) {
        this.options = options;
        return this;
    }

    private CardsPredicate sourcePreCondition = null;

    public AbstractMoveCardsOperationRule<P> sourcePreCondition(CardsPredicate sourcePreCondition) {
        this.sourcePreCondition = sourcePreCondition;
        return this;
    }

    private CardsPredicate targetPreConditon = null;

    public AbstractMoveCardsOperationRule<P> targetPreCondition(CardsPredicate targetPreConditon) {
        this.targetPreConditon = targetPreConditon;
        return this;
    }

    private CardsPredicate movedCardsConstraint = null;

    public AbstractMoveCardsOperationRule<P> movedCardsConstraint(CardsPredicate movedCardsConstraint) {
        this.movedCardsConstraint = movedCardsConstraint;
        return this;
    }

    private CardsPredicate combinedCardsConstraint = null;

    public AbstractMoveCardsOperationRule<P> combinedCardsConstraint(CardsPredicate combinedCardsConstraint) {
        this.combinedCardsConstraint = combinedCardsConstraint;
        return this;
    }

    private CardsPredicate sourcePostCondition = null;

    public AbstractMoveCardsOperationRule<P> sourcePostCondition(CardsPredicate sourcePostCondition) {
        this.sourcePostCondition = sourcePostCondition;
        return this;
    }

    private CardsPredicate targetPostCondition = null;

    public AbstractMoveCardsOperationRule<P> targetPostCondition(CardsPredicate targetPostCondition) {
        this.targetPostCondition = targetPostCondition;
        return this;
    }

    public Function<Pile, Integer> defaultPileCardCount = null;

    public AbstractMoveCardsOperationRule<P> defaultPileCardCount(Function<Pile, Integer> defaultPileCardCount) {
        this.defaultPileCardCount = defaultPileCardCount;
        return this;
    }
    
    public Function<Integer, Integer> defaultCardCount = null;

    public void setDefaultCardCount(Function<Integer, Integer> defaultCardCount) {
        this.defaultCardCount = defaultCardCount;
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
        List<Card> movedCards = source.getTopCards(cardCount);
        if (checkSourceConstraints) {
            if (movedCardsConstraint != null && (! movedCardsConstraint.test(movedCards))) {
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
        if (getOptions().isReversed()) {
            Collections.reverse(movedCards);
        }
        if (checkTargetConstraints) {
            List<Card> postTargetCards = target.insertedCards(targetPos, movedCards);
            // test the pile's own constraint
            if (! target.validateConstraint(postTargetCards)) {
                return false;
            }
            // test this rule's constraint
            if (targetPostCondition != null && (! targetPostCondition.test(postTargetCards))) {
                return false;
            }
        }
        if (checkSourceConstraints && checkTargetConstraints) {
            if (source == target && targetPos >= source.getCardCount() - cardCount) {
                return false;
            }
            if (combinedCardsConstraint != null && (! combinedCardsConstraint.test(target.getTopCard(), Pile.getBottomCard(movedCards)))) {
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

    public abstract PilesOperation createPilesOperation(Pile source, int cardCount, Pile target, int targetPos);

    @Override
    public PilesOperation accept(Patience<P> patience, Pile source, int cardCount) {
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
            if (validateConstraints(patience, source, cardCount, target, target.getCardCount(), true, true)) {
                possibleTargets.add(target);
            }
        }
        if (patience.hasPiles(targetPileKind)) {
            Collection<Pile> targets = patience.getPiles(targetPileKind);
            for (var pile : targets) {
                if (validateConstraints(patience, source, cardCount, pile, pile.getCardCount(), true, true)) {
                    possibleTargets.add(pile);
                }
            }
        }
        // System.out.println("Possible targets (" + possibleTargets.size() + "): " + possibleTargets);
        if (possibleTargets.size() >= 1) {
            target = possibleTargets.get(0);
        }
        if (target != null) {
            return createPilesOperation(source, cardCount, target, target.getCardCount());
        }
        return null;
    }

    @Override
    public PilesOperation accept(Patience<P> patience, Pile source, int cardCount, Pile target, int targetPos) {
        if (cardCount < 0) {
            cardCount = getDefaultCardCount(source);
        }
        if (! validateConstraints(patience, source, cardCount, target, targetPos, true, true)) {
            return null;
        }
        return createPilesOperation(source, cardCount, target, targetPos);
    }
}