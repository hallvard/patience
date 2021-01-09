package patience;


public abstract class Patience {

	private Deck deck;

	protected Patience() {
	}

	public Deck getDeck() {
		if (deck == null) {
			deck = new Deck();
		}
		return deck;
	}

	public boolean canDeal() {
		return getDeck().getCardCount() > 0;
	}

	public void deal() {
		if (canDeal()) {
			doDeal();
		}
	}
	protected void doDeal() {
	}

	public abstract Boolean isFinished();
}
