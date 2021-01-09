package patience.theidiot;

import java.util.Arrays;

import patience.Card;
import patience.CardStack;
import patience.Pile;
import patience.util.PileDealPatience;

class TheIdiot extends PileDealPatience {

	private final int suitStacksStartFace;
	private final TheIdiotStack[] idiotStacks = new TheIdiotStack[4];
	private final Pile extraPile;

	public TheIdiot() {
		super(3, -1);
		getPile().revealTopCards(3);
		final Card topCard = getDeck().getTopCard();
		suitStacksStartFace = topCard.getFace();
		getDeck().deal(getSuitStack(topCard.getSuit()), 1);
		extraPile = new Pile(null);
		getDeck().deal(extraPile, 12);
		for (int i = 0; i < idiotStacks.length; i++) {
			idiotStacks[i] = new TheIdiotStack(suitStacksStartFace, extraPile);
			getDeck().deal(idiotStacks[i], 1);
		}
	}

	@Override
	protected int getSuitStacksStartFace() {
		return suitStacksStartFace;
	}

	public Iterable<TheIdiotStack> getIdiotStacks() {
		return Arrays.asList(idiotStacks);
	}

	@Override
	public int getDealSize() {
		return getDeck().getCardCount() >= 3 ? 3 : 1;
	}

	public CardStack getExtraPile() {
		return extraPile;
	}
}
