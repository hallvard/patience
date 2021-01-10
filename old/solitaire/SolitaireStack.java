package patience.solitaire;

import patience.CardStack;
import patience.Deck;
import patience.util.OppositeColorStack;

class SolitaireStack extends OppositeColorStack {

	public SolitaireStack(final Deck source, final int cardCount) {
		super(13);
		source.deal(this, cardCount - 1);
		source.deal(this, 1);
	}

	@Override
	public void moveCards(final int fromPos, final int count, final CardStack target, final int toPos) {
		super.moveCards(fromPos, count, target, toPos);
	}
}
