package no.hal.patience.allaces.core;

import java.util.Arrays;
import java.util.List;

import no.hal.patience.AbstractMoveCardsOperationRule;
import no.hal.patience.CardOrder;
import no.hal.patience.MoveCardsOperationRule;
import no.hal.patience.Patience;
import no.hal.patience.Pile;
import no.hal.patience.PilesOperation;
import no.hal.patience.PilesOperationRule;
import no.hal.patience.SuitKind;
import no.hal.patience.util.SizePredicate;

public class AllAces extends Patience<AllAces.PileKinds> {

    private final Pile[] stacks = new Pile[SuitKind.values().length];

    public enum PileKinds {
        stacks, deck, deck2;
    }

    @Override
    public void initPiles() {
        Pile deck = Pile.deck(true);
        putPile(PileKinds.deck, deck);
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = Pile.empty();
        }
        putPiles(PileKinds.stacks, stacks);
        putPile(PileKinds.deck2, Pile.empty());
        DealToPilesOperation.deal(deck, stacks.length, false, stacks);
    }

    private PilesOperationRule<PileKinds> deckToStacks = new AbstractMoveCardsOperationRule<PileKinds>(PileKinds.deck, PileKinds.stacks, 4) {
        @Override
        public PilesOperation createPilesOperation(Pile source, int cardCount, Pile target, int targetPos) {
            return new DealToPilesOperation(getPile(PileKinds.deck), cardCount, stacks).faceDown(false);
        }
    };

    private PilesOperationRule<PileKinds> stacksToEmptyPile = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.stacks, 1)
            .targetPreCondition(SizePredicate.empty())
            ;
            
    @Override
    public void initPilesOperationRules() {
        super.initPilesOperationRules();
        PilesOperationRule<PileKinds> stacksToDeck2 = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.deck2, 1)
                .movedCardsConstraint(new SameSuitLesserThanTopCardInOtherStackPredicate(CardOrder.aceIs14(), stacks));
        addPilesOperationRules(List.of(
            deckToStacks,
            stacksToDeck2,
            stacksToEmptyPile
        ));
    }

    public static void main(String[] args) {
        AllAces allAces = new AllAces();
        allAces.initPiles();
        System.out.println(allAces);
    }
}