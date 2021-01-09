package patience.freecell;

import patience.CardStackMap;
import patience.Suit;
import patience.acmui.GCardStack.Options;
import patience.acmui.GCardStacks;
import patience.acmui.PatienceProgram;
import acm.graphics.GDimension;

public class FreeCellProgram extends PatienceProgram<FreeCell> {

	@Override
	protected FreeCell createPatience() {
		return new FreeCell();
	}

	public void run() {
		setSize(1200, 800);

		FreeCell freeCell = getPatience();

		Options defaults = new Options().setScaling(0.5).setSpacing(10.0);

		stacks.putCardStacks(CardStackMap.keys("f", 1, 4), freeCell.getFreeCells());
		CardStacksView freeCellGCardStacks = CardStacksView.create(stacks.iterator(CardStackMap.keys("f", 1, 4)), defaults);

		stacks.putCardStacks(Suit.iterator(), freeCell.getSuitStacks());
		CardStacksView suitGCardStacks = CardStacksView.create(stacks.iterator(Suit.iterator()), defaults);

		stacks.putCardStacks(CardStackMap.keys(1, 8), freeCell.getFreeCellStacks());
		Options options = defaults.copy().setShowingOffset(new GDimension(0.0, 40.0));
		CardStacksView freeCellStackGCardStacks = CardStacksView.create(stacks.iterator(1, 8), options);

		freeCellGCardStacks.setLocation(40.0, 10.0);
		// suitGCardStacks is right of freeCellGCardStacks
		suitGCardStacks.locate(rightOf(freeCellGCardStacks, defaults.getSpacing()), at(freeCellGCardStacks));
		// freeCellStackGCardStacks is below freeCellGCardStacks
		freeCellStackGCardStacks.locate(at(freeCellGCardStacks), below(freeCellGCardStacks, defaults.getSpacing() * 2));

		add(freeCellGCardStacks);
		add(suitGCardStacks);
		add(freeCellStackGCardStacks);

		addMouseListeners();
	}
}
