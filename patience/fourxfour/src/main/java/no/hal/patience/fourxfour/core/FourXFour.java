package no.hal.patience.fourxfour.core;

import java.util.Arrays;
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

public class FourXFour extends Patience<FourXFour.PileKinds> {
    
    private final Pile[] suits = new Pile[SuitKind.values().length];
    private final Pile[] stacks = new Pile[8];

    public enum PileKinds {
        suits, stacks;
    }

    @Override
    public void initPiles() {
        CardsPredicate constraint = SuitsPredicate.same().and(FacesPredicate.increasingFrom(1));
        for (var suit : SuitKind.values()) {
            suits[suit.ordinal()] = Pile.empty(constraint);
        }
        putPiles(PileKinds.suits, Arrays.asList(suits));
        
        Pile deck = Pile.deck();
        for (int rowNum = 0; ! deck.isEmpty(); rowNum++) {
            List<Card> cards = deck.takeCards(stacks.length);
            for (int colNum = 0; colNum < cards.size(); colNum++) {
                if (stacks[colNum] == null) {
                    stacks[colNum] = Pile.empty();
                }
                var card = cards.get(colNum);
                if (colNum < 4 && rowNum < 4) {
                    card.turn();
                }
                stacks[colNum].addCards(List.of(card));
            }
        }
        putPiles(PileKinds.stacks, stacks);
    }

    private PilesOperationRule<PileKinds> stacksToSuitsRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.suits)
            .options(new MoveCardsOperation.Options().reversed().autoRevealTopCard())
            .movedCardsConstraint(CardsPredicate.faceUp());
    private PilesOperationRule<PileKinds> stacksToStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.stacks)
            .options(new MoveCardsOperation.Options().autoRevealTopCard())
            .movedCardsConstraint(CardsPredicate.faceUp())
            .targetPreCondition(CardsPredicate.faceUp().ofTopCard())
            .combinedCardsConstraint(SuitsPredicate.same().and(FacesPredicate.decreasing()));
    private PilesOperationRule<PileKinds> stacksToEmptyStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.stacks)
            .options(new MoveCardsOperation.Options().autoRevealTopCard())
            .movedCardsConstraint(CardsPredicate.faceUp().and(FacesPredicate.sameAs(13).ofBottomCard()))
            .targetPreCondition(SizePredicate.empty());

    @Override
    public void initPilesOperationRules() {
        super.initPilesOperationRules();
        addPilesOperationRules(List.of(
            stacksToSuitsRule,
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
        FourXFour fourXFour = new FourXFour();
        fourXFour.initPiles();
        System.out.println(fourXFour);
    }
}