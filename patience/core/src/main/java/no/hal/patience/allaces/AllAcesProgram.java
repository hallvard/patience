package patience.allaces;

import patience.CardStackMap;
import patience.acmui.GCardStack;
import patience.acmui.GCardStack.Options;
import patience.acmui.GCardStacks;
import patience.acmui.PatienceProgram;
import acm.graphics.GDimension;

public class AllAcesProgram extends PatienceProgram<AllAces> {

	@Override
	protected AllAces createPatience() {
		return new AllAces();
	}

	public void run() {
		setSize(600, 400);
		AllAces allAces = getPatience();

		Options defaults = new Options().setScaling(0.5).setSpacing(10.0);
		
		stacks.putCardStacks(CardStackMap.keys(1, 4), allAces.getAllAcesStacks());
		CardStacksView allAcesGCardStacks = CardStacksView.create(stacks.iterator(1, 4), defaults.copy().setShowingOffset(new GDimension(0.0, 30.0)));

		GCardStack deckCardStack = new GCardStack(allAces.getDeck());
		deckCardStack.setOptions(defaults.copy().setHidingOffset(new GDimension(0.0, 2.0)));

		stacks.putCardStack("-", allAces.getPile());
		
		deckCardStack.setLocation(40.0, 10.0);
		allAcesGCardStacks.locate(rightOf(deckCardStack, defaults.getSpacing()), at(deckCardStack));

		add(deckCardStack);
		add(allAcesGCardStacks);

		addMouseListeners();
	}
}
