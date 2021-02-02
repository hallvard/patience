package no.hal.patience;

import java.util.Arrays;
import java.util.Iterator;

public enum SuitKind {

	spades(ColorKind.black), hearts(ColorKind.red), diamonds(ColorKind.red), clubs(ColorKind.black);
    
    private final ColorKind color;

    private SuitKind(ColorKind color) {
        this.color = color;
    }

    public ColorKind getColor() {
        return color;
    }

    public String getShortName() {
        return String.valueOf(Character.toUpperCase(name().charAt(0)));
    }

	public boolean isOppositeColor(SuitKind suit) {
		return getColor() != suit.getColor();
	}
	
	public static Iterator<SuitKind> iterator() {
        return Arrays.asList(values()).iterator();
    }

    public static SuitKind of(String name) {
        for (SuitKind suit : values()) {
            if (suit.name().startsWith(name.toLowerCase())) {
                return suit;
            }
        }
        throw new IllegalArgumentException(name + " does not name a SuitKind");
    }
}
