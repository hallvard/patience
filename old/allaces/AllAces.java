package patience.allaces;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import patience.Patience;
import patience.Pile;

public class AllAces extends Patience {

	private static final int STACK_COUNT = 4;

	private final List<AllAcesStack> allAcesStacks = new ArrayList<AllAcesStack>();
	private final Pile pile;

	public AllAces() {
		super();
		for (int i = 0; i < STACK_COUNT; i++) {
			allAcesStacks.add(new AllAcesStack(allAcesStacks));
		}
		pile = new Pile(null);
		deal();
	}

	public Iterator<AllAcesStack> getAllAcesStacks() {
		return allAcesStacks.iterator();
	}

	public Pile getPile() {
		return pile;
	}

	@Override
	public void doDeal() {
		for (final AllAcesStack stack : allAcesStacks) {
			getDeck().deal(stack, 1);
		}
	}

	@Override
	public Boolean isFinished() {
		if (getDeck().getCardCount() > 0) {
			return null;
		}
		for (final AllAcesStack stack : allAcesStacks) {
			if (stack.getCardCount() != 1 || stack.getCard(0).getFace() != 1) {
				return false;
			}
		}
		return true;
	}
}
