package patience.util;

import patience.Card;
import patience.CardStack;

/**
 * Card stack with alternating suit colors.
 * @author hal
 *
 */
public class OppositeColorStack extends CardStack {

	private int startFace = -1, endFace = -1;

	public OppositeColorStack(final int startFace, final int endFace) {
		this.startFace = startFace;
		this.endFace = endFace;
	}
	public OppositeColorStack(final int startFace) {
		this(startFace, startFace);
	}

	@Override
	public boolean canMoveFrom(final int fromPos, final int count, final CardStack target, final int toPos) {
		if (! CardStack.checkTopMoveFrom(this, fromPos, count)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean canMoveTo(final int toPos, final CardStack source, final int fromPos, final int count) {
		if (! CardStack.checkTopMoveTo(this, toPos)) {
			return false;
		}
		Card card = source.getCard(fromPos);
		if (getCardCount() == 0) {
			return (startFace < 0 || card.getFace() == startFace);
		}
		Card previousCard = getTopCard();
		if (previousCard.getFace() == endFace) {
			return false;
		}
		int pos = fromPos;
		do {
			card = source.getCard(pos);
			if (card.getFace() == endFace) {
				return false;
			}
			if (! (card.getSuit().isOppositeColor(previousCard.getSuit()) && card.isFaceBelow(previousCard))) {
				return false;
			}
			previousCard = card;
		} while (++pos < fromPos + count);
		return true;
	}

	@Override
	public void moveCards(final int fromPos, final int count, final CardStack target, final int toPos) {
		moveCards(fromPos, count, target, toPos, false);
	}
}
