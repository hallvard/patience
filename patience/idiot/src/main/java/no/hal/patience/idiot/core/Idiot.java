package no.hal.patience.idiot.core;

import java.util.Arrays;
import java.util.List;

import no.hal.patience.Card;
import no.hal.patience.MoveCardsOperation;
import no.hal.patience.MoveCardsOperationRule;
import no.hal.patience.Patience;
import no.hal.patience.Pile;
import no.hal.patience.SuitKind;
import no.hal.patience.util.CardsPredicate;
import no.hal.patience.util.FacesPredicate;
import no.hal.patience.util.SuitsPredicate;

public class Idiot extends Patience<Idiot.PileKinds> {
    
    private final Pile[] stacks = new Pile[SuitKind.values().length];
    private Pile extras;
    private final Pile[] suits = new Pile[SuitKind.values().length];

    public enum PileKinds {
        suits, stacks, extras, deck, deck2;
    }

    @Override
    public void initPiles() {
        Pile deck = Pile.deck();
        List<Card> suitStartCard = deck.takeCards(1);
        CardsPredicate constraint = SuitsPredicate.same().and(FacesPredicate.increasingFrom(suitStartCard.get(0).getFace()));
        for (var suit : SuitKind.values()) {
            suits[suit.ordinal()] = Pile.empty(constraint);
        }
        suits[0].addCards(suitStartCard);
        putPiles(PileKinds.suits, Arrays.asList(suits));

        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = Pile.empty(SuitsPredicate.alernatingColor().and(FacesPredicate.decreasing()));
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

    @Override
    public void initPilesOperationRules() {
        super.initPilesOperationRules();
        addPilesOperationRules(
            new MoveCardsOperationRule<PileKinds>(PileKinds.deck, PileKinds.deck2, 3, true, true),
            new MoveCardsOperationRule<PileKinds>(PileKinds.extras, PileKinds.stacks),
            new MoveCardsOperationRule<PileKinds>(PileKinds.extras, PileKinds.suits),
            new MoveCardsOperationRule<PileKinds>(PileKinds.deck2, PileKinds.stacks, 1),
            new MoveCardsOperationRule<PileKinds>(PileKinds.deck2, PileKinds.suits, 1),
            new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.stacks),
            new MoveCardsOperationRule<PileKinds>(PileKinds.stacks, PileKinds.suits, 1)
        );
    }

    @Override
    public boolean updatePilesOperations() {
        return getPile(PileKinds.deck).getCardCount() > 0;
    }

    public static void main(String[] args) {
        Idiot idiot = new Idiot();
        idiot.initPiles();
        System.out.println(idiot);
    }
}