package no.hal.patience;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Card stack that receives cards from a source,
 * typically a deck, where only the top one can be moved.
 * @author hal
 *
 */
public class Pile implements Iterable<Card> {

    private List<Card> cards;

	public Pile(final Collection<Card> initialCards) {
        this.cards = new ArrayList<>();
	}
    
    public Pile(final Card... initialCards) {
        this(Arrays.asList(initialCards));
    }

    private Collection<CardsPredicate> constraints = new ArrayList<>();

    public void addConstraints(CardsPredicate... constraints) {
        this.constraints.addAll(Arrays.asList(constraints));
    }

    private void setAll(List<Card> newCards) {
        if (! constraints.stream().allMatch(predicate -> predicate.test(this.cards))) {
            throw new IllegalArgumentException(newCards + " is not legal for " + this);
        }
        this.cards = newCards;
    }

    //

    public static Pile of(final CardKind... initialCards) {
        return new Pile(Card.cards(initialCards));
    }

    public static Pile of(final String... initialCards) {
        return new Pile(Card.cards(initialCards));
    }

    public static Pile deck() {
        return Pile.of(CardKind.values());
    }

    //

    @Override
    public Iterator<Card> iterator() {
        return cards.iterator();
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

    public void replaceCards(int start, int end, Collection<Card> replacementCards) {
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
        setAll(newCards);
    }

    public void addCards(Collection<Card> cards) {
        List<Card> newCards = new ArrayList<>(this.cards);
        newCards.addAll(cards);
        setAll(newCards);
    }

    public void insertCards(int pos, Collection<Card> cards) {
        List<Card> newCards = new ArrayList<>(this.cards);
        newCards.addAll(pos, cards);
        setAll(newCards);
    }

    public void removeCards(int start, int end) {
        replaceCards(start, end, Collections.emptyList());
    }

    public void setAll(Collection<Card> cards) {
        cards.clear();
        cards.addAll(cards);
    }

    //

    public void shuffle() {
        Collections.shuffle(cards);
    }

    //

	private int revealPos = 0;
	private boolean revealFromTop = false;

    public int getCardCount() {
        return cards.size();
    }

	public void revealCards(final int pos) {
		revealPos = pos;
		revealFromTop = false;
	}

	public void revealTopCards(final int pos) {
		revealPos = pos;
		revealFromTop = true;
	}

	public boolean isFaceUp(final int index) {
		return index >= (revealFromTop ? getCardCount() - revealPos : revealPos);
	}
}
