package no.hal.patience;

import java.util.Random;

/**
 * Card stack initialized to all possible cards, and
 * that later typically represents the remaining, unused cards.
 * @author hal
 *
 */
public class Deck extends CardStack {

	public Deck(final int shuffleCount) {
		// iterate over all suits
		for (final CardKind cardKind: CardKind.values()) {
            final Card card = new Card(cardKind);
            cards.add(card);
		}
		shuffle(shuffleCount);
	}
	public Deck() {
		this(10);
	}

	@Override
	public boolean isFaceUp(final int index) {
		return false;
	}

	public void shuffle(final int shuffleCount) {
		final Random rand = new Random();
		for (int j = shuffleCount; j > 0; j--) {
			for (int i = 0; i < getCardCount(); i++) {
				final Card card = cards.get(i);
				final int n = rand.nextInt(cards.size());
				cards.set(i, cards.get(n));
				cards.set(n, card);
			}
		}
	}

	@Override
	public void moveCards(final int fromPos, final int count, final CardStack target, final int toPos) {
		moveCards(fromPos, count, target, toPos, true);
	}
	@Override
	public boolean canMoveFrom(final int fromPos, final int count, final CardStack target, final int toPos) {
		return false;
	}
	@Override
	public boolean canMoveTo(final int toPos, final CardStack source, final int fromPos, final int count) {
		return false;
	}

	public void deal(final CardStack target, final int count) {
		moveCards(getCardCount() - count, count, target, target.getCardCount(), true);
	}
	public void collect(final CardStack source, final int count) {
		source.moveCards(source.getCardCount() - count, count, this, this.getCardCount(), true);
	}
}
