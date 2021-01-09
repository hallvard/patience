package patience.util;

import patience.Pile;

public abstract class PileDealPatience extends SuitsPatience {

	private final Pile pile;
	private final int dealSize;
	private int redealCount;

	public PileDealPatience(final int dealSize, final int redealCount) {
		super(1);
		this.dealSize = dealSize;
		this.redealCount = redealCount;
		pile = new Pile(getDeck());
	}

	@Override
	public boolean canDeal() {
		return super.canDeal() || redealCount != 0;
	}

	@Override
	protected void doDeal() {
		if (getDeck().getCardCount() == 0) {
			pile.moveAllCards(getDeck());
			redealCount--;
		}
		getDeck().deal(getPile(), getDealSize());
	}

	protected int getDealSize() {
		return dealSize;
	}

	public Pile getPile() {
		return pile;
	}

	@Override
	public Boolean isFinished() {
		if (redealCount > 0 || getDeck().getCardCount() > 0) {
			return null;
		}
		return super.isFinished();
	}
}
