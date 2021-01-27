package no.hal.patience.idiot.core;

import java.util.Arrays;
import java.util.List;

import no.hal.patience.Card;
import no.hal.patience.CardOrder;
import no.hal.patience.MoveCardsOperation;
import no.hal.patience.MoveCardsOperationRule;
import no.hal.patience.Patience;
import no.hal.patience.Pile;
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

    private CardOrder cardOrder = null;

    @Override
    public void initPiles() {
        CardsPredicate constraint = SuitsPredicate.same().and(FacesPredicate.increasingFrom(1));
        for (var suit : SuitKind.values()) {
            suits[suit.ordinal()] = Pile.empty(constraint);
        }
        putPiles(PileKinds.suits, Arrays.asList(suits));
        
        Pile deck = Pile.deck();
        for (int rowNum = 0; ! deck.isEmpty(); rowNum++) {
            List<Card> cards = deck.takeTopCards(8);
            for (int colNum = 0; colNum < stacks.length; colNum++) {
                if (stacks[colNum] == null) {
                    stacks[colNum] = Pile.empty();
                }
                var card = cards.get(i);
                if (colNum < 4 && rowNum < 4) {
                    card.turn();
                }
                stacks[colNum].addCards(List.of(card));
            }
        }
        putPiles(PileKinds.stacks, Arrays.asList(stacks));
    }

    private MoveCardsOperationRule<PileKinds> stacksToStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.stacks);
    private MoveCardsOperationRule<PileKinds> stacksToSuitsRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.suits);

    @Override
    public void initPilesOperationRules() {
        super.initPilesOperationRules();
        addPilesOperationRules(List.of(
            stacksToStacksRule, stacksToSuitsRule
        ));
    }

    public static void main(String[] args) {
        FourXFour fourXFour = new FourXFour();
        fourXFour.initPiles();
        System.out.println(fourXFour);
    }
}