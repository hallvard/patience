package patience.fourtimesfour;

import patience.CardStack;
import patience.Deck;
import patience.util.SuitStack;

class FourTimesFourStack extends SuitStack {

	public FourTimesFourStack(final Deck deck, final boolean fourtimesfour) {
		super(null, 13);
		setFaceAbove(false);
		setFaceBelow(true);
		setMoveCount(-1);
		if (fourtimesfour) {
			deck.deal(this, 4);
			deck.deal(this, 3);
		} else {
			deck.deal(this, 6);
		}
	}

	@Override
	public boolean canMoveFrom(final int fromPos, final int count, final CardStack target, final int toPos) {
		if (! CardStack.checkTopMoveFrom(this, fromPos, count)) {
			return false;
		}
		return true;
	}

	@Override
	public void moveCards(final int fromPos, final int count, final CardStack target, final int toPos) {
		moveCards(fromPos, count, target, toPos, false);
	}
}
