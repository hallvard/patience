package patience.util;

import java.util.Iterator;

import patience.Card;
import patience.CardStack;
import patience.Suit;

/**
 * Card stack that can only contain one suit.
 * Can also control card order, increasing or decreasing face values.
 * @author hal
 *
 */
public class SuitStack extends CardStack {

	private final Suit suit;
	private final int startFace;
	private int moveCount = 1;
	private boolean faceAbove = true, faceBelow = false;

	public SuitStack(final Suit suit, final int startFace) {
		this.suit = suit;
		this.startFace = startFace;
	}
	public SuitStack(final Suit suit) {
		this(suit, 1);
	}
	@Override
	public Suit getSuit() {
		return (suit != null ? suit : (getCardCount() > 0 ? getTopCard().getSuit() : null));
	}

	public void setMoveCount(final int moveCount) {
		this.moveCount = moveCount;
	}
	public void setFaceAbove(final boolean faceAbove) {
		this.faceAbove = faceAbove;
	}
	public void setFaceBelow(final boolean faceBelow) {
		this.faceBelow = faceBelow;
	}

	@Override
	public boolean canMoveFrom(final int fromPos, final int count, final CardStack target, final int toPos) {
		return false;
	}

	@Override
	public boolean canMoveTo(final int toPos, final CardStack source, final int fromPos, final int count) {
		if (this.moveCount > 0 && count != this.moveCount) {
			return false;
		}
		if (! CardStack.checkTopMoveTo(this, toPos)) {
			return false;
		}
		final Card card = source.getCard(fromPos);
		if (getCardCount() == 0) {
			return (getSuit() == null || card.getSuit() == getSuit()) && (startFace < 0 || card.getFace() == startFace);
		}
		final Card previousCard = getTopCard();
		if (! (card.getSuit() == previousCard.getSuit() && ((faceAbove && card.isFaceAbove(previousCard)) || (faceBelow && card.isFaceBelow(previousCard))))) {
			return false;
		}
		return true;
	}

	@Override
	public void moveCards(final int fromPos, final int count, final CardStack target, final int toPos) {
		throw new UnsupportedOperationException("Cannot move cards from a SuitStack");
	}

	//

	public static SuitStack[] createSuitStacks(final int startFace) {
		final Suit[] suits = Suit.values();
		final SuitStack[] suitStacks = new SuitStack[suits.length];
		for (int i = 0; i < suitStacks.length; i++) {
			suitStacks[i] = new SuitStack(suits[i], startFace);
		}
		return suitStacks;
	}


	public static boolean isFinished(final Iterator<SuitStack> suitStacks) {
		while (suitStacks.hasNext()) {
			final SuitStack suitStack = suitStacks.next();
			if (suitStack.getCardCount() != 13) {
				return false;
			}
		}
		return true;
	}
}
