package patience.util;

import patience.Card;
import patience.CardStack;

public class SingleCardStack extends CardStack {

	public SingleCardStack (final Card card) {
		if (card != null) {
			cards.add(card);
		}
	}

	@Override
	public boolean canMoveFrom(final int fromPos, final int count, final CardStack target, final int toPos) {
		return count == getCardCount();
	}

	@Override
	public boolean canMoveTo(final int toPos, final CardStack source, final int fromPos, final int count) {
		if (count != 1 || toPos != 0 || getCardCount() > 0) {
			return false;
		}
		return true;
	}

	@Override
	public void moveCards(final int fromPos, final int count, final CardStack target, final int toPos) {
		moveCards(fromPos, count, target, toPos, false);
	}
}
