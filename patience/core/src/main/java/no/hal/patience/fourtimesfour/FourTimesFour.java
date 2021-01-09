package patience.fourtimesfour;

import java.util.Arrays;
import java.util.Iterator;

import patience.util.SuitsPatience;

class FourTimesFour extends SuitsPatience {

	private FourTimesFourStack[] fourTimesFourStacks = new FourTimesFourStack[8];
	
	public FourTimesFour() {
		super(1);
		for (int i = 0; i < fourTimesFourStacks.length; i++) {
			fourTimesFourStacks[i] = new FourTimesFourStack(getDeck(), i < 4);
		}
	}
	
	public Iterator<FourTimesFourStack> getFourTimesFourStacks() {
		return Arrays.asList(fourTimesFourStacks).iterator();
	}
}
