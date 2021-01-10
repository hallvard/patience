package no.hal.patience;

public class Card {

	private final CardKind cardKind;

	public Card(CardKind cardKind) {
		this.cardKind = cardKind;
	}

	@Override
	public String toString() {
		// concatenate suit and face
		// the same as suit.concat(String.valueOf(face)), but simpler
		return getSuit().name();
	}

	public SuitKind getSuit() {
		return cardKind.getSuit();
	}

	public int getFace() {
		return cardKind.getFace();
	}

	public boolean isOppositeColor(final Card other) {
		return getSuit().isOppositeColor(other.getSuit());
	}
}
