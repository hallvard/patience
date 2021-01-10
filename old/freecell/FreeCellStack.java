package patience.freecell;

import patience.CardStack;
import patience.Deck;
import patience.util.OppositeColorStack;

class FreeCellStack extends OppositeColorStack {

	public FreeCellStack(final Deck deck, final int cardCount) {
		super(13);
		deck.deal(this, cardCount);
	}

	@Override
	public boolean canMoveFrom(final int fromPos, final int count, final CardStack target, final int toPos) {
		return super.canMoveFrom(fromPos, count, target, toPos) && count == 1;
	}
}
