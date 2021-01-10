package patience.allaces;

import patience.Card;
import patience.CardStack;
import patience.Pile;

public class AllAcesStack extends CardStack {

	private final Iterable<AllAcesStack> cardStacks;

	public AllAcesStack(final Iterable<AllAcesStack> cardStacks) {
		this.cardStacks = cardStacks;
	}

	@Override
	public boolean canMoveFrom(final int fromPos, final int count, final CardStack target, final int toPos) {
		if (! CardStack.checkTopMoveFrom(this, fromPos, count)) {
			return false;
		}
		if (count != 1) {
			return false;
		}
		if (target instanceof Pile) {
			final Card card = getTopCard();
			for (final CardStack cardStack: cardStacks) {
				if (cardStack != this && cardStack.getCardCount() > 0) {
					final Card topCard = cardStack.getTopCard();
					if (topCard.getSuit() == card.getSuit() && (topCard.getFace() > card.getFace() || topCard.getFace() == 1)) {
						return true;
					}
				}
			}
			return false;
		} else {
			return target.getCardCount() == 0;
		}
	}

	@Override
	public boolean canMoveTo(final int toPos, final CardStack source, final int fromPos, final int count) {
		return getCardCount() == 0;
	}

	@Override
	public void moveCards(final int fromPos, final int count, final CardStack target, final int toPos) {
		moveCards(fromPos, count, target, toPos, false);
	}
}
