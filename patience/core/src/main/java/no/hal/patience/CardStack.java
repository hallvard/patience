package patience;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * General class for stacks of cards,
 * with rules for what cards that can moved from and to it.
 * Supports lower part that lie face down.
 * @author hal
 *
 */
public abstract class CardStack {

	protected List<Card> cards;

	public CardStack() {
		cards = new ArrayList<Card>();
	}

	@Override
	public String toString() {
		return cards.toString();
	}

	// support for suit-specific CardStacks
	public Suit getSuit() {
		return null;
	}

	public int getCardCount() {
		return cards.size();
	}

	public Card getCard(final int index) {
		return cards.get(index);
	}
	public Card getTopCard(final int index) {
		final int pos = cards.size() - index - 1;
		return cards.get(pos);
	}
	public Card getTopCard() {
		return getTopCard(0);
	}

	public boolean isFaceUp(final int index) {
		return true;
	}

	// listener methods

	private final List<CardStackListener> listeners = new ArrayList<CardStackListener>();

	public void addCardsListener(final CardStackListener listener) {
		listeners.add(listener);
	}

	public void removeCardsListener(final CardStackListener listener) {
		listeners.remove(listener);
	}

	// helper methods

	protected void fireCardsChanged(final int start, final int end) {
		for (final Iterator<CardStackListener> it = listeners.iterator(); it.hasNext();) {
			it.next().cardsTurned(this, start, end);
		}
	}
	protected void fireCardsAdded(final int start, final int end) {
		for (final Iterator<CardStackListener> it = listeners.iterator(); it.hasNext();) {
			it.next().cardsAdded(this, start, end);
		}
	}
	protected void fireCardsRemoved(final int start, final int end) {
		for (final Iterator<CardStackListener> it = listeners.iterator(); it.hasNext();) {
			it.next().cardsRemoved(this, start, end);
		}
	}

	//

	public static boolean canMoveCards(final CardStack source, final int fromPos, final int count, final CardStack target, final int toPos) {
		if (fromPos + count > source.cards.size() || toPos > target.cards.size()) {
			return false;
		}
		if (! checkFaceUp(source, fromPos, count, true)) {
			return false;
		}
		if (! source.canMoveFrom(fromPos, count, target, toPos)) {
			return false;
		}
		if (! target.canMoveTo(toPos, source, fromPos, count)) {
			return false;
		}
		return true;
	}
	public static boolean canMoveTopCards(final CardStack source, final int count, final CardStack target) {
		return canMoveCards(source, source.getCardCount() - count, count, target, target.getCardCount());
	}

	public static void moveCards(final CardStack source, final int fromPos, final int count, final CardStack target, final int toPos) {
		if (canMoveCards(source, fromPos, count, target, toPos)) {
			source.moveCards(fromPos, count, target, toPos);
		}
	}
	public static void moveTopCards(final CardStack source, final int count, final CardStack target) {
		moveCards(source, source.getCardCount() - count, count, target, target.getCardCount());
	}

	public static boolean checkFaceUp(final CardStack cardStack, final int pos, final int count, final boolean faceUp) {
		for (int i = pos; i < pos + count; i++) {
			if (cardStack.isFaceUp(i) != faceUp) {
				return false;
			}
		}
		return true;
	}
	public static boolean checkTopMoveFrom(final CardStack source, final int fromPos, final int count) {
		if (fromPos + count != source.getCardCount()) {
			return false;
		}
		return true;
	}
	public static boolean checkTopMoveTo(final CardStack target, final int toPos) {
		if (toPos != target.getCardCount()) {
			return false;
		}
		return true;
	}

	//

	public abstract boolean canMoveFrom(int fromPos, int count, CardStack target, int toPos);
	public abstract boolean canMoveTo(int toPos, CardStack source, int fromPos, int count);

	public abstract void moveCards(int fromPos, int count, CardStack target, int toPos);

	protected void moveCards(final int fromPos, final int count, final CardStack target, final int toPos, final boolean reversed) {
		for (int i = 0; i < count; i++) {
			final Card card = cards.remove(reversed ? fromPos + count - i - 1: fromPos);
			target.cards.add(toPos + i, card);
			fireCardsRemoved(fromPos, fromPos + count - 1);
			target.fireCardsAdded(toPos, toPos + count - 1);
		}
	}
}
