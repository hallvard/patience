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
        if (cardsListeners != null) {
            for (CardsListener<Pile> listener : cardsListeners) {
                listener.cardsChanged(this, oldCards, newCards);
            }
        }
        return oldCards;
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

    public static Pile deck() {
        Pile deck = Pile.of(CardKind.values());
        deck.shuffle();
        return deck;
    }

    //

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
    }
    
    public int getCardCount() {
        return cards.size();
    }

    private int adjustIndex(int index) {
        if (index > cards.size()) {
            index = cards.size();
        } else if (index < 0) {
            index = getCardCount() + index;
            if (index < 0) {
                index = 0;
            }
        }
        return index;
    }

    public List<Card> getCards(int start, int end) {
        start = adjustIndex(start);
        end = adjustIndex(end);
        List<Card> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            result.add(cards.get(i));
        }
        return result;
    }

    public List<Card> getTopCards(int count) {
        return getCards(getCardCount() - count, getCardCount());
    }

    public List<Card> getBottomCards(int count) {
        return getCards(0, count);
    }
    
    public List<Card> getAllCards() {
        return getCards(0, getCardCount());
    }

    List<Card> replacedCards(int start, int end, Collection<Card> replacementCards) {
        start = adjustIndex(start);
        end = adjustIndex(end);
        List<Card> newCards = new ArrayList<>(cards.size() - (end - start) + replacementCards.size());
        for (int i = 0; i < start; i++) {
            newCards.add(cards.get(i));
        }
        for (Card replacementCard : replacementCards) {
            newCards.add(replacementCard);
        }
        for (int i = end; i < cards.size(); i++) {
            newCards.add(cards.get(i));
        }
        return newCards;
    }
    public List<Card> replaceCards(int start, int end, Collection<Card> replacementCards) {
        return setAllCards(replacedCards(start, end, replacementCards));
    }

    List<Card> addedCards(Collection<Card> cards) {
        List<Card> newCards = new ArrayList<>(this.cards);
        newCards.addAll(cards);
        return newCards;
    }
    public List<Card> addCards(Collection<Card> cards) {
        return setAllCards(addedCards(cards));
    }

    List<Card> insertedCards(int pos, Collection<Card> cards) {
        List<Card> newCards = new ArrayList<>(this.cards);
        newCards.addAll(pos, cards);
        return newCards;
    }
    public List<Card> insertCards(int pos, Collection<Card> cards) {
        return setAllCards(insertedCards(pos, cards));
    }

    List<Card> removedCards(int start, int end) {
        return replacedCards(start, end, Collections.emptyList());
    }
    public List<Card> removeCards(int start, int end) {
        return setAllCards(removedCards(start, end));
    }

    public List<Card> setAllCards(Collection<Card> cards) {
        return setAllCards(new ArrayList<>(cards));
    }

    //

    public List<Card> shuffle() {
        List<Card> newCards = new ArrayList<>(this.cards);
        Collections.shuffle(newCards);
        return setAllCards(newCards);
    }

    private List<Card> takeCards(int count, boolean reverse) {
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

    //

    private static void moveCards(Pile source, Pile target, int cardCount, boolean reverse, boolean turn) {
        List<Card> cards = source.takeCards(cardCount, reverse);
        if (turn) {
            for (Card card : cards) {
                card.turn();
            }
        }
        target.addCards(cards);
    }

    public static void moveCards(Pile source, Pile target, int cardCount) {
        moveCards(source, target, cardCount, false, false);
    }

    public static void moveCard(Pile source, Pile target) {
        moveCards(source, target, 1);
    }

    public static void moveCardsReversed(Pile source, Pile target, int cardCount) {
        moveCards(source, target, cardCount, true, false);
    }

    public static void moveCardsTurning(Pile source, Pile target, int cardCount) {
        moveCards(source, target, cardCount, false, true);
    }

    public static void moveCardsReversedTurning(Pile source, Pile target, int cardCount) {
        moveCards(source, target, cardCount, true, true);
    }

    //

	public static boolean canMoveTopCards(Pile source, int cardCount, Pile target) {
		return false;
	}

	public static void moveTopCards(Pile source, int cardCount, Pile target) {
	}
}
