package patience.util;

import java.util.Arrays;

import patience.Patience;
import patience.Suit;

public abstract class SuitsPatience extends Patience {

	private SuitStack[] suitStacks;
	private final int startFace;

	public SuitsPatience(final int startFace) {
		this.startFace = startFace;
	}

	protected void createSuitStacks() {
		suitStacks = SuitStack.createSuitStacks(getSuitStacksStartFace());
	}

	protected int getSuitStacksStartFace() {
		return startFace;
	}

	public Iterable<SuitStack> getSuitStacks() {
		if (suitStacks == null) {
			createSuitStacks();
		}
		return Arrays.asList(suitStacks);
	}

	public SuitStack getSuitStack(final Suit suit) {
		if (suitStacks == null) {
			createSuitStacks();
		}
		return suitStacks[suit.ordinal()];
	}

	@Override
	public Boolean isFinished() {
		return SuitStack.isFinished(getSuitStacks().iterator());
	}
}
