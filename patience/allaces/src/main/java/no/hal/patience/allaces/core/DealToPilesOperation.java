package no.hal.patience.allaces.core;

import java.util.List;

import no.hal.patience.Card;
import no.hal.patience.Pile;
import no.hal.patience.PilesOperation;

public class DealToPilesOperation implements PilesOperation {

    private final Pile deck;
    private final Pile[] piles;
    private int cardCount;
    private Boolean faceDown;

    public DealToPilesOperation(Pile deck, int cardCount, Pile... piles) {
        this.deck = deck;
        this.cardCount = cardCount;
        this.piles = piles.clone();
    }

    public DealToPilesOperation faceDown(boolean faceDown) {
        this.faceDown = faceDown;
        return this;
    }

    @Override
    public boolean canApply() {
        return deck.getCardCount() >= cardCount;
    }

    @Override
    public void apply() {
        List<Card> cards = deck.takeCards(cardCount);
        // distribute cards among piles
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (faceDown != null) {
                card.setFaceDown(faceDown);
            }
            piles[i % piles.length].addCards(List.of(card));
        }
    }
}
