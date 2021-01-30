package no.hal.patience.allonboard.core;

import java.util.Arrays;
import java.util.List;

import no.hal.patience.AbstractMoveCardsOperationRule;
import no.hal.patience.Card;
import no.hal.patience.CardOrder;
import no.hal.patience.MoveCardsOperationRule;
import no.hal.patience.Patience;
import no.hal.patience.Pile;
import no.hal.patience.PilesOperation;
import no.hal.patience.PilesOperationRule;
import no.hal.patience.SuitKind;
import no.hal.patience.util.SizePredicate;

public class AllOnBoard extends Patience<AllOnBoard.PileKinds> {

    private final Pile[][] slots = new Pile[SuitKind.values().length][14];

    public enum PileKinds {
        spadesSlots, heartsSlots, diamondsSlots, clubsSlots;
    }

    @Override
    public void initPiles() {
        List<Card> cards = Pile.deck().getAllCards();
        int aceCount = 0;
        for (int cardNum = 0; cardNum < cards.size(); cardNum++) {
            Card card = cards.get(cardNum);
            Pile pile = Pile.of(List.of(card));
            if (card.getFace() == 1) {
                int row = card.getSuit().ordinal();
                slots[row][0] = pile;
                slots[row][1] = Pile.empty();
                aceCount++;
            } else {
                int slotCount = cardNum - aceCount;
                slots[slotCount / 12][slotCount % 12 + 2] = pile;
            }
        }
        putPiles(PileKinds.spadesSlots, Arrays.asList(slots[SuitKind.spades.ordinal()]));
        putPiles(PileKinds.heartsSlots, Arrays.asList(slots[SuitKind.hearts.ordinal()]));
        putPiles(PileKinds.diamondsSlots, Arrays.asList(slots[SuitKind.diamonds.ordinal()]));
        putPiles(PileKinds.clubsSlots, Arrays.asList(slots[SuitKind.clubs.ordinal()]));
    }

    @Override
    public void initPilesOperationRules() {
        super.initPilesOperationRules();
    }

    public static void main(String[] args) {
        AllOnBoard allOnBoard = new AllOnBoard();
        allOnBoard.initPiles();
        System.out.println(allOnBoard);
    }
}