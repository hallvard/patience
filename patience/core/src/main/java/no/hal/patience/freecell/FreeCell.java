package patience.freecell;

import java.util.Arrays;
import java.util.Iterator;

import patience.CardStack;
import patience.util.SingleCardStack;
import patience.util.SuitsPatience;

class FreeCell extends SuitsPatience {

	private CardStack[] freeCells = new CardStack[4];
	private FreeCellStack[] freeCellStacks = new FreeCellStack[8];
	
	public FreeCell() {
		super(1);
		for (int i = 0; i < freeCells.length; i++) {
			freeCells[i] = new SingleCardStack(null);
		}
		for (int i = 0; i < freeCellStacks.length; i++) {
			freeCellStacks[i] = new FreeCellStack(getDeck(), i < (52 % 8) ? 7 : 6);
		}
	}
	
	public Iterator<CardStack> getFreeCells() {
		return Arrays.asList(freeCells).iterator();
	}

	public Iterator<FreeCellStack> getFreeCellStacks() {
		return Arrays.asList(freeCellStacks).iterator();
	}
}
