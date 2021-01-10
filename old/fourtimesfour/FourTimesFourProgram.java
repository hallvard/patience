package patience.fourtimesfour;

import patience.CardStackMap;
import patience.Suit;
import patience.acmui.GCardStack.Options;
import patience.acmui.GCardStacks;
import patience.acmui.PatienceProgram;
import acm.graphics.GDimension;

public class FourTimesFourProgram extends PatienceProgram<FourTimesFour> {

	@Override
	protected FourTimesFour createPatience() {
		return new FourTimesFour();
	}

	public void run() {
		setSize(1200, 800);
		
		FourTimesFour fourTimesFour = getPatience();

		Options defaults = new Options().setScaling(0.5).setSpacing(10.0);
		
		stacks.putCardStacks(Suit.iterator(), fourTimesFour.getSuitStacks());
		CardStacksView suitGCardStacks = CardStacksView.create(stacks.iterator(Suit.iterator()), defaults);

		stacks.putCardStacks(CardStackMap.keys(1, 8), fourTimesFour.getFourTimesFourStacks());
		Options options = defaults.copy()
			.setHidingOffset(new GDimension(0.0, 30.0))
			.setShowingOffset(new GDimension(0.0, 30.0));
		CardStacksView fourTimesFourGCardStacks = CardStacksView.create(stacks.iterator(1, 8), options);

		fourTimesFourGCardStacks.setLocation(40.0, 10.0);
		suitGCardStacks.locate(centeredOn(fourTimesFourGCardStacks), at(fourTimesFourGCardStacks));
		fourTimesFourGCardStacks.locate(at(fourTimesFourGCardStacks), below(suitGCardStacks, defaults.getSpacing() * 2));

		add(suitGCardStacks);
		add(fourTimesFourGCardStacks);

		addMouseListeners();
	}
}
