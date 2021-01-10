package patience.allonboard;

import patience.Card;
import patience.CardStack;
import patience.util.SingleCardStack;

class AllOnBoardStack extends SingleCardStack {

	private final AllOnBoardStack previousStack;

	public AllOnBoardStack(final AllOnBoardStack previousStack) {
		super(null);
		this.previousStack = previousStack;
	}

	@Override
	public boolean canMoveTo(final int toPos, final CardStack source, final int fromPos, final int count) {
		if (! super.canMoveTo(toPos, source, fromPos, count)) {
			return false;
		}
		final Card card = source.getTopCard();
		if (previousStack == null) {
			return card.getFace() == 1;
		}
		if (previousStack.getCardCount() == 0) {
			return false;
		}
		final Card previousCard = this.previousStack.getTopCard();
		if (previousCard.getSuit() != card.getSuit() || (! previousCard.isFaceBelow(card))) {
			return false;
		}
		return true;
	}
}
