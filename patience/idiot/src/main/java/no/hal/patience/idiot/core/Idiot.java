package no.hal.patience.idiot.core;

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

public class Idiot extends Patience<Idiot.PileKinds> {
    
    private final Pile[] stacks = new Pile[SuitKind.values().length];
    private Pile extras;
    private final Pile[] suits = new Pile[SuitKind.values().length];

    public enum PileKinds {
        suits, stacks, extras, deck, deck2;
    }

    private CardOrder cardOrder = null;

    @Override
    public void initPiles() {
        Pile deck = Pile.deck();
        List<Card> suitStartCard = deck.takeCards(1);
        cardOrder = CardOrder.startsAt(suitStartCard.get(0).getFace());
        CardsPredicate constraint = SuitsPredicate.same().and(FacesPredicate.increasingFrom(1, cardOrder));
        for (var suit : SuitKind.values()) {
            suits[suit.ordinal()] = Pile.empty(constraint);
        }
        suits[0].addCards(suitStartCard);
        putPiles(PileKinds.suits, Arrays.asList(suits));

        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = Pile.empty(SuitsPredicate.alternatingColor().and(FacesPredicate.decreasing(cardOrder)));
            MoveCardsOperation.moveCard(deck, stacks[i]);
        }
        putPiles(PileKinds.stacks, Arrays.asList(stacks));
        
        List<Card> extraCards = deck.takeCards(12);
        extras = Pile.of(extraCards);
        putPile(PileKinds.extras, extras);
        
        deck.getAllCards().stream().forEach(Card::turn);
        putPile(PileKinds.deck, deck);
        putPile(PileKinds.deck2, Pile.empty());

        MoveCardsOperation.moveCardsReversedTurning(getPile(PileKinds.deck), getPile(PileKinds.deck2), 3);
    }

    private AbstractMoveCardsOperationRule<PileKinds> dealRule = new MoveCardsOperationRule<PileKinds>(PileKinds.deck, PileKinds.deck2, -1)
            .options(new MoveCardsOperation.Options().reversed().turning())
            .targetPreCondition(SizePredicate.empty())
            .sourcePostCondition(SizePredicate.empty());
    private PilesOperationRule<PileKinds> undealRule = new MoveCardsOperationRule<PileKinds>(PileKinds.deck2, PileKinds.deck, -1)
            .options(new MoveCardsOperation.Options().reversed().turning());
    private PilesOperationRule<PileKinds> extrasToStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.extras, PileKinds.stacks, 1);
    private PilesOperationRule<PileKinds> extrasToSuitsRule = new MoveCardsOperationRule<PileKinds>(PileKinds.extras, PileKinds.suits, 1);
    private AbstractMoveCardsOperationRule<PileKinds> deck2ToStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.deck2, PileKinds.stacks, 1);
    private PilesOperationRule<PileKinds> deck2ToSuitsRule = new MoveCardsOperationRule<PileKinds>(PileKinds.deck2, PileKinds.suits, 1);
    private PilesOperationRule<PileKinds> stacksToStacksRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.stacks);
    private PilesOperationRule<PileKinds> stacksToSuitsRule = new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.suits, 1);

    @Override
    public void initPilesOperationRules() {
        super.initPilesOperationRules();
        dealRule.setDefaultCardCount(size -> size >= 3 ? 3 : 1);
        addPilesOperationRules(List.of(
            dealRule, undealRule,
            extrasToStacksRule, extrasToSuitsRule,
            deck2ToStacksRule, deck2ToSuitsRule,
            stacksToStacksRule, stacksToSuitsRule
        ));
    }

    @Override
    public Boolean updatePilesOperations() {
        super.updatePilesOperations();
        deck2ToStacksRule.targetPreCondition(SizePredicate.atLeast(getPile(PileKinds.extras).isEmpty() ? 0 : 1));
        if (getPile(PileKinds.deck).getCardCount() + getPile(PileKinds.deck2).getCardCount() > 0) {
            return true;
        }
        return null;
    }

    public static void main(String[] args) {
        Idiot idiot = new Idiot();
        idiot.initPiles();
        System.out.println(idiot);
    }
}