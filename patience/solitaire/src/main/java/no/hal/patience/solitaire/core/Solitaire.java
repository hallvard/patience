package no.hal.patience.solitaire.core;

import java.util.Arrays;
import java.util.List;

import no.hal.patience.AbstractMoveCardsOperationRule;
import no.hal.patience.Card;
import no.hal.patience.CardOrder;
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

public class Solitaire extends Patience<Solitaire.PileKinds> {
    
    private final Pile[] suits = new Pile[SuitKind.values().length];
    private final Pile[] stacks = new Pile[7];

    public enum PileKinds {
        suits, stacks, deck, deck2;
    }

    @Override
    public void initPiles() {
        CardsPredicate constraint = SuitsPredicate.same().and(FacesPredicate.increasingFrom(1));
        for (var suit : SuitKind.values()) {
            suits[suit.ordinal()] = Pile.empty(constraint);
        }
        putPiles(PileKinds.suits, suits);
        
        Pile deck = Pile.deck();
        for (int i = 0; i < stacks.length; i++) {
            List<Card> cards = deck.takeCards(i + 1);
            for (int j = 0; j < cards.size() - 1; j++) {
                cards.get(j).setFaceDown(true);
            }
            stacks[i] = Pile.of(cards);
        }
        putPiles(PileKinds.stacks, stacks);
        
        deck.getAllCards().stream().forEach(Card::turn);
        putPile(PileKinds.deck, deck);
        putPile(PileKinds.deck2, Pile.empty());

        MoveCardsOperation.moveCardsReversedTurning(deck, getPile(PileKinds.deck2), 3);
    }

    private AbstractMoveCardsOperationRule<PileKinds> dealRule = new MoveCardsOperationRule<PileKinds>(PileKinds.deck, PileKinds.deck2, -1)
            .options(new MoveCardsOperation.Options().reversed().turning());
            private PilesOperationRule<PileKinds> undealRule = new MoveCardsOperationRule<PileKinds>(PileKinds.deck2, PileKinds.deck, -1)
            .options(new MoveCardsOperation.Options().reversed().turning())
            .targetPreCondition(SizePredicate.empty())
            .sourcePostCondition(SizePredicate.empty());
            private AbstractMoveCardsOperationRule<PileKinds> deck2ToStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.deck2, PileKinds.stacks, 1)
    private PilesOperationRule<PileKinds> deck2ToEmptyStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.deck2, PileKinds.stacks)
            .movedCardsConstraint(FacesPredicate.sameAs(13).ofTopCard())
            .targetPreCondition(SizePredicate.empty());
            .combinedCardsConstraint(SuitsPredicate.alternatingColor().and(FacesPredicate.decreasing()));
    private PilesOperationRule<PileKinds> deck2ToSuitsRule = new MoveCardsOperationRule<PileKinds>(PileKinds.deck2, PileKinds.suits, 1);
    private PilesOperationRule<PileKinds> stacksToSuitsRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.suits, 1)
            .options(new MoveCardsOperation.Options().autoRevealTopCard())
            .movedCardsConstraint(CardsPredicate.faceUp());
    private PilesOperationRule<PileKinds> stacksToStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.stacks)
            .options(new MoveCardsOperation.Options().autoRevealTopCard())
            .movedCardsConstraint(CardsPredicate.faceUp())
            .combinedCardsConstraint(SuitsPredicate.alternatingColor().and(FacesPredicate.decreasing()));
    private PilesOperationRule<PileKinds> stacksToEmptyStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.stacks)
            .options(new MoveCardsOperation.Options().autoRevealTopCard())
            .movedCardsConstraint(CardsPredicate.faceUp().and(FacesPredicate.sameAs(13).ofBottomCard()))
            .targetPreCondition(SizePredicate.empty());
    @Override
    public void initPilesOperationRules() {
        super.initPilesOperationRules();
        dealRule.setDefaultCardCount(size -> size >= 3 ? 3 : 1);
        addPilesOperationRules(List.of(
            dealRule, undealRule,
            deck2ToStacksRule, deck2ToSuitsRule,
            stacksToStacksRule, stacksToSuitsRule
        ));
    }

    @Override
    public Boolean updatePilesOperations() {
        super.updatePilesOperations();
        return null;
    }

    public static void main(String[] args) {
        Solitaire solitaire = new Solitaire();
        solitaire.initPiles();
        System.out.println(solitaire);
    }
}