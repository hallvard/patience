package patience;

/**
 * Card stack that receives cards from a source,
 * typically a deck, where only the top one can be moved.
 * @author hal
 *
 */
public class Pile extends CardStack {

	private final CardStack source;

	public Pile(final CardStack source) {
		super();
		this.source = source;
	}

	private int revealPos = 0;
	private boolean revealFromTop = false;

	public void revealCards(final int pos) {
		revealPos = pos;
		revealFromTop = false;
	}

	public void revealTopCards(final int pos) {
		revealPos = pos;
		revealFromTop = true;
	}

	@Override
	public boolean isFaceUp(final int index) {
		return index >= (revealFromTop ? getCardCount() - revealPos : revealPos);
	}

	@Override
	public void moveCards(final int fromPos, final int count, final CardStack target, final int toPos) {
		moveCards(fromPos, count, target, toPos, true);
	}

	@Override
	public boolean canMoveFrom(final int fromPos, final int count, final CardStack target, final int toPos) {
		return (count == 1 && fromPos == getCardCount() - 1);
	}

	@Override
	public boolean canMoveTo(final int toPos, final CardStack source, final int fromPos, final int count) {
		return (this.source == null || source == this.source);
	}

	public void moveAllCards(final Deck deck) {
		moveCards(0, getCardCount(), deck, deck.getCardCount(), true);
	}
}
