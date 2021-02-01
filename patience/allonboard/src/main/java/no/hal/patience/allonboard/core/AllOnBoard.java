package no.hal.patience.allonboard.core;

import java.util.ArrayList;
import java.util.List;

import no.hal.patience.Card;
import no.hal.patience.MoveCardsOperationRule;
import no.hal.patience.Patience;
import no.hal.patience.Pile;
import no.hal.patience.SuitKind;

public class AllOnBoard extends Patience<AllOnBoard.PileKinds> {

    private final Pile[][] slots = new Pile[SuitKind.values().length][14];

    public enum PileKinds {
        slots;
    }

    @Override
    public void initPiles() {
        // create empty piles
        List<Pile> allPiles = new ArrayList<>();
        // piles are added to all piles in order that matches
        // how PilesView distributes piles in children
        for (int colNum = 0; colNum < 14; colNum++) {
            for (int rowNum = 0; rowNum < slots.length; rowNum++) {
                Pile pile = Pile.empty();
                slots[rowNum][colNum] = pile;
                if (colNum == 0) {
                    pile.setConstraints(cs -> cs.size() == 1 && cs.get(0).getFace() == 1);
                }
                allPiles.add(pile);
            }
        }
        putPiles(PileKinds.slots, allPiles);
    
        Pile deck = Pile.deck();
        // add aces
        List<Card> aces = deck.takeCards(card -> card.getFace() == 1);
        for (var ace : aces) {
            int row = ace.getSuit().ordinal();
            slots[row][0].addCards(List.of(ace));
        }
        // deal remaining cards
        deal(deck.getAllCards());
    }

    protected void deal(List<Card> cards) {
        int cardNum = 0;
        for (int rowNum = 0; rowNum < slots.length; rowNum++) {
            boolean deal = false;
            // start at 1 since aces are already placed
            for (int colNum = 1; colNum < slots[rowNum].length; colNum++) {
                Pile pile = slots[rowNum][colNum];
                if (deal) {
                    pile.addCards(List.of(cards.get(cardNum)));
                    cardNum++;
                } else if (pile.isEmpty()) {
                    deal = true;
                }
                final int row = rowNum, col = colNum;
                pile.setConstraints(cs -> cs.isEmpty() || (cs.size() == 1 && isCorrectCard(row, col, cs.get(0))));
            }
        }
    }

    private boolean isCorrectCard(int rowNum, int colNum, Card card) {
        if (colNum > 0) {
            Pile pile = slots[rowNum][colNum - 1];
            if (! pile.isEmpty()) {
                Card left = pile.getTopCard();
                if (card.getSuit() == left.getSuit() && card.getFace() == left.getFace() + 1) {
                    return true;
                }
            }
        }
        return false;
    }

    protected List<Card> undeal() {
        List<Card> cards = new ArrayList<>();
        for (int rowNum = 0; rowNum < slots.length; rowNum++) {
            boolean undeal = false;
            for (int colNum = 1; colNum < slots[rowNum].length; colNum++) {
                Pile pile = slots[rowNum][colNum];
                if (undeal) {
                    cards.addAll(pile.takeCards(pile.getCardCount()));
                } else if (! pile.isEmpty()) {
                    Card topCard = pile.getTopCard();
                    if (! isCorrectCard(rowNum, colNum, topCard)) {
                        undeal = true;
                        colNum--;
                    }
                }
                pile.clearConstraints();
            }
        }
        return cards;
    }

    private MoveCardsOperationRule<PileKinds> slotsToSlots = new MoveCardsOperationRule<>(PileKinds.slots, PileKinds.slots, 1);

    @Override
    public void initPilesOperationRules() {
        super.initPilesOperationRules();
        addPilesOperationRules(List.of(slotsToSlots));
    }

    public static void main(String[] args) {
        AllOnBoard allOnBoard = new AllOnBoard();
        allOnBoard.initPiles();
        System.out.println(allOnBoard);
    }
}