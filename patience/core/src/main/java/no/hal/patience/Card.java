package patience;

public class Card {

	private final Suit suit;
	private final int face;

	public Card(final Suit suit, final int face) {
		this.suit = suit;
		this.face = face;
	}

	@Override
	public String toString() {
		// concatenate suit and face
		// the same as suit.concat(String.valueOf(face)), but simpler
		return suit.name() + face;
	}

	public Suit getSuit() {
		return suit;
	}

	public int getFace() {
		return face;
	}

	public boolean isOppositeColor(final Card other) {
		return getSuit().isOppositeColor(other.getSuit());
	}

	public static int getFaceBelow(final int face) {
		return (face + 11) % 13 + 1;
	}
	public static int getFaceAbove(final int face) {
		return (face % 13 + 1);
	}

	public boolean isFaceBelow(final Card other) {
		return getFace() == getFaceBelow(other.getFace());
	}
	public boolean isFaceAbove(final Card other) {
		return getFace() == getFaceAbove(other.getFace());
	}
}
