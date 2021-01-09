package patience;

import java.util.Arrays;
import java.util.Iterator;

import javafx.scene.paint.Color;

public enum Suit {

	spades, hearts, diamonds, clubs;

	public Color getColor() {
		switch (this) {
		case spades: case clubs: return Color.BLACK;
		case hearts: case diamonds: return Color.RED;
		}
		return null;
	}
	
	public boolean isOppositeColor(Suit suit) {
		return getColor() != suit.getColor();
	}
	
	public static Iterator<Suit> iterator() {
		return Arrays.asList(Suit.values()).iterator();
	}
}
