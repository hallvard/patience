package no.hal.patience;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import no.hal.patience.util.Cards;
import no.hal.patience.util.CardsListener;
import no.hal.patience.util.CardsPredicate;

public class Pile implements Iterable<Card>, Cards {

    private CardsPredicate constraint;
    
    private List<Card> cards;

    private Collection<CardsListener<Pile>> cardsListeners;

	protected Pile(CardsPredicate constraint, final Collection<Card> initialCards) {
        this.constraint = constraint;
        setAllCards(initialCards);
	}

    @Override
    public String toString() {
        return getClass().getSimpleName() + " of " + cards;
    }

    public void addConstraint(CardsPredicate constraint) {
        if (this.constraint == CardsPredicate.whatever) {
            this.constraint = constraint;
        } else {
            this.constraint = this.constraint.and(constraint);
        }
    }

    public boolean validateConstraint(List<Card> newCards) {
        return constraint.test(newCards);
    }

    public void checkConstraints(List<Card> newCards) {
        if (! validateConstraint(newCards)) {
            throw new IllegalArgumentException(newCards + " is invalid for " + constraint);
        }
    }

    public void addCardsListener(CardsListener<Pile> listener) {
        if (cardsListeners == null) {
            cardsListeners = new ArrayList<>();
        }
        cardsListeners.add(listener);
    }

    public void removeCardsListener(CardsListener<Pile> listener) {
        if (cardsListeners != null) {
            cardsListeners.remove(listener);
        }
    }

    protected List<Card> setAllCards(List<Card> newCards) {
        checkConstraints(newCards);
        List<Card> oldCards = this.cards;
        this.cards = newCards;
        fireCardsChanged(oldCards, newCards);
        return oldCards;
    }

    protected void fireCardsChanged(List<Card> oldCards, List<Card> newCards) {
        if (cardsListeners != null) {
            for (CardsListener<Pile> listener : cardsListeners) {
                listener.cardsChanged(this, oldCards, newCards);
            }
        }
    }

    //

    public static Pile empty(CardsPredicate... predicates) {
        CardsPredicate cardsPredicate = CardsPredicate.whatever;
        for (var predicate : predicates) {
            cardsPredicate = cardsPredicate.and(predicate);
        }
        return new Pile(cardsPredicate, Collections.emptyList());
    }

    public static Pile of(final Collection<Card> initialCards) {
        return new Pile(CardsPredicate.whatever, initialCards);
    }

    public static Pile of(final Card... initialCards) {
        return new Pile(CardsPredicate.whatever, Arrays.asList(initialCards));
    }

    public static Pile of(final CardKind... initialCards) {
        return new Pile(CardsPredicate.whatever, Card.cards(initialCards));
    }

    public static Pile of(final String... initialCards) {
        return new Pile(CardsPredicate.whatever, Card.cards(initialCards));
    }

    public static Pile deck(boolean faceDown) {
        Pile deck = Pile.of(CardKind.values());
        deck.cards.stream().forEach(card -> card.setFaceDown(faceDown));
        deck.shuffle();
        return deck;
    }
    public static Pile deck() {
        return deck(false);
    }

    //

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }
    
    public int getCardCount() {
        return cards.size();
    }

    public boolean isEmpty() {
        return getCardCount() == 0;
    }

    static int adjustIndex(List<Card> cards, int index) {
        if (index > cards.size()) {
            index = cards.size();
        } else if (index < 0) {
            index = cards.size() + index;
            if (index < 0) {
                index = 0;
            }
        }
        return index;
    }

    static List<Card> getCards(List<Card> thisCards, int start, int end) {
        start = adjustIndex(thisCards, start);
        end = adjustIndex(thisCards, end);
        List<Card> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            result.add(thisCards.get(i));
        }
        return result;
    }
    public List<Card> getCards(int start, int end) {
        return Pile.getCards(this.cards, start, end);
    }

    static List<Card> getTopCards(List<Card> thisCards, int count) {
        return getCards(thisCards, thisCards.size() - count, thisCards.size());
    }
    public List<Card> getTopCards(int count) {
        return Pile.getTopCards(this.cards, count);
    }

    static Card getTopCard(List<Card> thisCards) {
        return thisCards.get(thisCards.size() - 1);
    }
    public Card getTopCard() {
        return Pile.getTopCard(this.cards);
    }

    static List<Card> getBottomCards(List<Card> thisCards, int count) {
        return Pile.getCards(thisCards, 0, count);
    }
    public List<Card> getBottomCards(int count) {
        return Pile.getBottomCards(this.cards, count);
    }

    static Card getBottomCard(List<Card> thisCards) {
        return thisCards.get(0);
    }
    public Card getBottomCard() {
        return Pile.getBottomCard(this.cards);
    }

    public List<Card> getAllCards() {
        return getCards(0, getCardCount());
    }

    //

    static List<Card> replacedCards(List<Card> thisCards, int start, int end, Collection<Card> replacementCards) {
        start = adjustIndex(thisCards, start);
        end = adjustIndex(thisCards, end);
        List<Card> newCards = new ArrayList<>(thisCards.size() - (end - start) + replacementCards.size());
        for (int i = 0; i < start; i++) {
            newCards.add(thisCards.get(i));
        }
        for (Card replacementCard : replacementCards) {
            newCards.add(replacementCard);
        }
        for (int i = end; i < thisCards.size(); i++) {
            newCards.add(thisCards.get(i));
        }
        return newCards;
    }

    List<Card> replacedCards(int start, int end, Collection<Card> replacementCards) {
        return Pile.replacedCards(this.cards, start, end, replacementCards);
    }

    public List<Card> replaceCards(int start, int end, Collection<Card> replacementCards) {
        return setAllCards(replacedCards(start, end, replacementCards));
    }

    //

    static List<Card> addedCards(List<Card> thisCards, Collection<Card> cards) {
        List<Card> newCards = new ArrayList<>(thisCards);
        newCards.addAll(cards);
        return newCards;
    }

    List<Card> addedCards(Collection<Card> cards) {
        return Pile.addedCards(this.cards, cards);
    }

    public List<Card> addCards(Collection<Card> cards) {
        return setAllCards(addedCards(cards));
    }

    //

    static List<Card> insertedCards(List<Card> thisCards, int pos, Collection<Card> cards) {
        List<Card> newCards = new ArrayList<>(thisCards);
        newCards.addAll(pos, cards);
        return newCards;
    }

    List<Card> insertedCards(int pos, Collection<Card> cards) {
        return Pile.insertedCards(this.cards, pos, cards);
    }

    public List<Card> insertCards(int pos, Collection<Card> cards) {
        return setAllCards(insertedCards(pos, cards));
    }

    //

    static List<Card> removedCards(List<Card> thisCards, int start, int end) {
        return replacedCards(thisCards, start, end, Collections.emptyList());
    }

    List<Card> removedCards(int start, int end) {
        return Pile.removedCards(this.cards, start, end);
    }

    public List<Card> removeCards(int start, int end) {
        return setAllCards(removedCards(start, end));
    }

    public void revealTopCards(int count) {
        int revealCount = 0;
        count = Math.min(count, cards.size());
        while (count > 0) {
            Card card = cards.get(cards.size() - count);
            if (card.isFaceDown()) {
                card.turn();
                revealCount++;
            }
            count--;
        }
        if (revealCount > 0) {
            fireCardsChanged(cards, cards);
        }
    }
    public void revealTopCard() {
        revealTopCards(1);
    }

    //

    public List<Card> setAllCards(Collection<Card> cards) {
        return setAllCards(new ArrayList<>(cards));
    }

    //

    public List<Card> shuffle() {
        List<Card> newCards = new ArrayList<>(this.cards);
        Collections.shuffle(newCards);
        return setAllCards(newCards);
    }

    List<Card> takeCards(int count, boolean reverse) {
        List<Card> topCards = getTopCards(count);
        removeCards(cards.size() - count, cards.size());
        if (reverse) {
            Collections.reverse(topCards);
        }
        return topCards;
    }

    public List<Card> takeCards(int count) {
        return takeCards(count, false);
    }

    public List<Card> takeCardsReversed(int count) {
        return takeCards(count, true);
    }

    public Card takeCard() {
        return takeCards(1).get(0);
    }
}
