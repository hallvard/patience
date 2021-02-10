package no.hal.patience.freecell.core;

import java.util.List;

import no.hal.patience.Card;
import no.hal.patience.MoveCardsOperation;
import no.hal.patience.MoveCardsOperationRule;
import no.hal.patience.Patience;
import no.hal.patience.Pile;
import no.hal.patience.PilesOperationRule;
import no.hal.patience.SuitKind;
import no.hal.patience.util.CardsPredicate;
import no.hal.patience.util.FacesPredicate;
import no.hal.patience.util.SizePredicate;
import no.hal.patience.util.SuitsPredicate;

public class FreeCell extends Patience<FreeCell.PileKinds> {
    
    private final Pile[] suits = new Pile[SuitKind.values().length];
    private final Pile[] freeCells = new Pile[4];
    private final Pile[] stacks = new Pile[8];

    public enum PileKinds {
        suits, freeCells, stacks;
    }

    @Override
    public void initPiles() {
        CardsPredicate suitsConstraint = SuitsPredicate.same().and(FacesPredicate.increasingFrom(1));
        for (var suit : SuitKind.values()) {
            suits[suit.ordinal()] = Pile.empty(suitsConstraint);
        }
        putPiles(PileKinds.suits, suits);

        CardsPredicate freeCellsConstraint = SizePredicate.atMost(1);
        for (int i = 0; i < freeCells.length; i++) {
            freeCells[i] = Pile.empty(freeCellsConstraint);
        }
        putPiles(PileKinds.freeCells, freeCells);

        Pile deck = Pile.deck();
        for (int i = 0; i < stacks.length; i++) {
            List<Card> cards = deck.takeCards((deck.getCardCount() + stacks.length - i - 1) / (stacks.length - i));
            stacks[i] = Pile.of(cards);
        }
        putPiles(PileKinds.stacks, stacks);
    }

    private int getFeeCellsCount() {
        int count = 0;
        for (int i = 0; i < freeCells.length; i++) {
            if (freeCells[i].isEmpty()) {
                count++;
            }
        }
        return count;
    }

    private PilesOperationRule<PileKinds> stacksToSuitsRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.suits)
            .options(new MoveCardsOperation.Options().reversed());
    private PilesOperationRule<PileKinds> stacksToFreeCellsRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.freeCells, 1)
            .targetPreCondition(SizePredicate.empty());
    private PilesOperationRule<PileKinds> freeCellsToStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.freeCells, PileKinds.stacks, 1)
            .combinedCardsConstraint(SuitsPredicate.alternatingColor().and(FacesPredicate.decreasing()));
    private PilesOperationRule<PileKinds> stacksToStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.stacks, -1)
            .movedCardsConstraint(SuitsPredicate.alternatingColor().and(FacesPredicate.decreasing()).and(CardsPredicate.of(cards -> cards.size() <= getFeeCellsCount() + 1)))
            .combinedCardsConstraint(SuitsPredicate.alternatingColor().and(FacesPredicate.decreasing()));
    private PilesOperationRule<PileKinds> stacksToEmptyStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.stacks)
            .movedCardsConstraint(SuitsPredicate.alternatingColor().and(FacesPredicate.decreasing()).and(FacesPredicate.sameAs(13).ofBottomCard()))
            .targetPreCondition(SizePredicate.empty());

    @Override
    public void initPilesOperationRules() {
        super.initPilesOperationRules();
        addPilesOperationRules(List.of(
            stacksToSuitsRule,
            stacksToFreeCellsRule, freeCellsToStacksRule,
            stacksToStacksRule, stacksToEmptyStacksRule
        ));
    }

    @Override
    public Boolean updatePilesOperations() {
        if (everyCardCount(count -> count == 13, PileKinds.suits)) {
            clearPilesOperationRules();
            return true;
        }
        return super.updatePilesOperations();
    }

    public static void main(String[] args) {
        FreeCell freeCell = new FreeCell();
        freeCell.initPiles();
        System.out.println(freeCell);
    }
}