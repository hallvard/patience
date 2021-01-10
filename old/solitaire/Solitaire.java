package patience.solitaire;

import java.util.Arrays;
import java.util.Iterator;

import patience.util.PileDealPatience;

class Solitaire extends PileDealPatience {

	private SolitaireStack[] solitaireStacks = new SolitaireStack[7];
	
	public Solitaire() {
		super(3, -1);
		for (int i = 0; i < solitaireStacks.length; i++) {
			solitaireStacks[i] = new SolitaireStack(getDeck(), i + 1);
		}
	}
	
	public Iterator<SolitaireStack> getSolitaireStacks() {
		return Arrays.asList(solitaireStacks).iterator();
	}
}
