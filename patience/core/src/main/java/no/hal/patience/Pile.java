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

    private CardsPredicate constraint;
    private List<Card> cards;

	protected Pile(CardsPredicate constraint, final Collection<Card> initialCards) {
        this.constraint = constraint;
        setAll(initialCards);
	}

    public void addConstraint(CardsPredicate constraint) {
        if (this.constraint == CardsPredicate.whatever) {
            this.constraint = constraint;
        } else {
            this.constraint = this.constraint.and(constraint);
        }
    }

    private void checkConstraints(List<Card> newCards) {
        if (! constraint.test(newCards)) {
            throw new IllegalArgumentException(newCards + " is not legal for " + this);
        }
    }

    private List<Card> setAll(List<Card> newCards) {
        checkConstraints(newCards);
        List<Card> oldCards = this.cards;
        this.cards = newCards;
        return oldCards;
    }

    //
    
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

    public List<Card> replaceCards(int start, int end, Collection<Card> replacementCards) {
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
        return setAll(newCards);
    }

    public List<Card> addCards(Collection<Card> cards) {
        List<Card> newCards = new ArrayList<>(this.cards);
        newCards.addAll(cards);
        return setAll(newCards);
    }

    public List<Card> insertCards(int pos, Collection<Card> cards) {
        List<Card> newCards = new ArrayList<>(this.cards);
        newCards.addAll(pos, cards);
        return setAll(newCards);
    }

    public List<Card> removeCards(int start, int end) {
        return replaceCards(start, end, Collections.emptyList());
    }

    public List<Card> setAll(Collection<Card> cards) {
        return setAll(new ArrayList<>(cards));
    }

    //

    public List<Card> shuffle() {
        List<Card> newCards = new ArrayList<>(this.cards);
        Collections.shuffle(newCards);
        return setAll(newCards);
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
