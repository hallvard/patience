package no.hal.patience.idiot.core;

import java.util.Arrays;
import java.util.List;

import no.hal.patience.Card;
import no.hal.patience.MoveCardsOperation;
import no.hal.patience.Patience;
import no.hal.patience.Pile;
import no.hal.patience.SuitKind;
import no.hal.patience.util.CardsPredicate;
import no.hal.patience.util.FacesPredicate;
import no.hal.patience.util.SuitsPredicate;

public class Idiot extends Patience {
    
    private final Pile[] stacks = new Pile[SuitKind.values().length];
    private Pile extras;
    private final Pile[] suits = new Pile[SuitKind.values().length];

    @Override
    public void initPiles() {
        Pile deck = Pile.deck();
        List<Card> suitStartCard = deck.takeCards(1);
        CardsPredicate constraint = SuitsPredicate.same().and(FacesPredicate.increasingFrom(suitStartCard.get(0).getFace()));
        for (var suit : SuitKind.values()) {
            suits[suit.ordinal()] = Pile.empty(constraint);
        }
        suits[0].addCards(suitStartCard);
        putPiles("suits", Arrays.asList(suits));

        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = Pile.empty(SuitsPredicate.alernatingColor().and(FacesPredicate.decreasing()));
            MoveCardsOperation.moveCard(deck, stacks[i]);
        }
        putPiles("stacks", Arrays.asList(stacks));
        
        List<Card> extraCards = deck.takeCards(12);
        extras = Pile.of(extraCards);
        putPile("extras", extras);
        
        deck.getAllCards().stream().forEach(Card::turn);
        putPile(Patience.DECK_PILE_NAME, deck);
        putPile("deck2", Pile.empty());

        deal();
    }

    private void deal() {
        MoveCardsOperation.moveCardsReversedTurning(getPile("deck"), getPile("deck2"), 3);
    }

    @Override
    public boolean canDeal() {
        return false;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    public static void main(String[] args) {
        Idiot idiot = new Idiot();
        idiot.initPiles();
        System.out.println(idiot);
    }
}