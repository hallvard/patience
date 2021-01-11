package no.hal.patience;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Card {

	private final CardKind cardKind;

	public Card(CardKind cardKind) {
		this.cardKind = cardKind;
	}

    //

    public static Card valueOf(CardKind cardKind) {
        return new Card(cardKind);
    }

    public static Card valueOf(String s) {
        return new Card(CardKind.valueOf(s));
    }

    public static List<Card> cards(final CardKind... initialCards) {
        return Arrays.stream(initialCards).map(Card::new).collect(Collectors.toList());
    }

    public static List<Card> cards(final String... initialCards) {
        return Arrays.stream(initialCards).map(Card::valueOf).collect(Collectors.toList());
    }

    //

	@Override
	public String toString() {
		return getSuit().name() + "-" + getFace();
	}

    public CardKind getCardKind() {
        return cardKind;
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
