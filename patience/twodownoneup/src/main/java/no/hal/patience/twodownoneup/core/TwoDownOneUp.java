package no.hal.patience.twodownoneup.core;

import java.util.Arrays;
import java.util.List;

import no.hal.patience.Card;
import no.hal.patience.CardKind;
import no.hal.patience.MoveCardsOperation;
import no.hal.patience.MoveCardsOperationRule;
import no.hal.patience.Patience;
import no.hal.patience.Pile;
import no.hal.patience.PilesOperationRule;
import no.hal.patience.SuitKind;
import no.hal.patience.util.CardsPredicate;
import no.hal.patience.util.FacesPredicate;
import no.hal.patience.util.SuitsPredicate;

public class TwoDownOneUp extends Patience<TwoDownOneUp.PileKinds> {
    
    private static int CARDS_PR_STACK = 3;

    private final Pile[] suits = new Pile[SuitKind.values().length];
    private final Pile[] stacks = new Pile[(CardKind.values().length + CARDS_PR_STACK - 1) / CARDS_PR_STACK];

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
        for (int i = 0; i < stacks.length; i++) {
            List<Card> cards = deck.takeCards(CARDS_PR_STACK);
            for (int j = 0; j < cards.size() - 1; j++) {
                cards.get(j).setFaceDown(true);
            }
            stacks[i] = Pile.of(cards);
        }
        putPiles(PileKinds.stacks, stacks);
    }

    private PilesOperationRule<PileKinds> stacksToSuitsRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.suits)
            .options(new MoveCardsOperation.Options().reversed().autoRevealTopCard())
            .movedCardsConstraint(CardsPredicate.faceUp());
    private PilesOperationRule<PileKinds> stacksToStacksRule1 = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.stacks)
            .options(new MoveCardsOperation.Options().reversed().autoRevealTopCard())
            .movedCardsConstraint(CardsPredicate.faceUp().and(FacesPredicate.increasing()))
            .targetPreCondition(CardsPredicate.faceUp().ofTopCard())
            .combinedCardsConstraint(SuitsPredicate.same().and(FacesPredicate.decreasing()));
    private PilesOperationRule<PileKinds> stacksToStacksRule2 = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.stacks)
            .options(new MoveCardsOperation.Options().reversed().autoRevealTopCard())
            .movedCardsConstraint(CardsPredicate.faceUp().and(FacesPredicate.decreasing()))
            .targetPreCondition(CardsPredicate.faceUp().ofTopCard())
            .combinedCardsConstraint(SuitsPredicate.same().and(FacesPredicate.increasing()));

    @Override
    public void initPilesOperationRules() {
        super.initPilesOperationRules();
        addPilesOperationRules(List.of(
            stacksToSuitsRule,
            stacksToStacksRule1, stacksToStacksRule2
        ));
    }

    public static void main(String[] args) {
        TwoDownOneUp twoDownOneUp = new TwoDownOneUp();
        twoDownOneUp.initPiles();
        System.out.println(twoDownOneUp);
    }
}