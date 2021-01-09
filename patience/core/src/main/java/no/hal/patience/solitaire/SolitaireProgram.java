package patience.solitaire;

import patience.CardStackMap;
import patience.Suit;
import patience.acmui.GCardStack;
import patience.acmui.GCardStack.Options;
import patience.acmui.GCardStacks;
import patience.acmui.PatienceProgram;
import acm.graphics.GDimension;

public class SolitaireProgram extends PatienceProgram<Solitaire> {

	@Override
	protected Solitaire createPatience() {
		return new Solitaire();
	}

	public void run() {
		setSize(1200, 800);
		Solitaire solitaire = getPatience();

		Options defaults = new Options().setScaling(0.5).setSpacing(10.0);
		
		stacks.putCardStacks(Suit.iterator(), solitaire.getSuitStacks());
		Options options = defaults.copy().setShowingOffset(new GDimension(0.0, 1.0));
		CardStacksView suitGCardStacks = CardStacksView.create(stacks.iterator(Suit.iterator()), options);

		stacks.putCardStacks(CardStackMap.keys(1, 7), solitaire.getSolitaireStacks());
		options = defaults.copy()
			.setHidingOffset(new GDimension(0.0, 15.0))
			.setShowingOffset(new GDimension(0.0, 30.0));
		CardStacksView solitaireGCardStacks = CardStacksView.create(stacks.iterator(1, 7), options);

		GCardStack deckCardStack = new GCardStack(solitaire.getDeck());
		deckCardStack.setOptions(defaults.copy().setHidingOffset(new GDimension(0.0, 2.0)));

		stacks.putCardStack("-", solitaire.getPile());
		GCardStack pileCardStack = new GCardStack(solitaire.getPile());
		options = defaults.copy()
			.setHidingOffset(new GDimension(0.0, 2.0))
			.setShowingOffset(new GDimension(0.0, 30.0))
			.setShowLimit(3);
		pileCardStack.setOptions(options);

		solitaireGCardStacks.setLocation(40.0, 10.0);
		// suitGCardStacks is x-centered on solitaireGCardStacks
		suitGCardStacks.locate(centeredOn(solitaireGCardStacks), at(solitaireGCardStacks));
		// solitaireGCardStacks is below suitGCardStacks
		solitaireGCardStacks.locate(at(solitaireGCardStacks), below(suitGCardStacks, defaults.getSpacing() * 2));
		// deckCardStack is below solitaireGCardStacks
		deckCardStack.locate(at(solitaireGCardStacks), below(solitaireGCardStacks, defaults.getSpacing() * 5));
		// pileCardStack is right of deckGCardStacks
		pileCardStack.locate(rightOf(deckCardStack, defaults.getSpacing()), at(deckCardStack));

		add(suitGCardStacks);
		add(solitaireGCardStacks);
		add(deckCardStack);
		add(pileCardStack);

		addMouseListeners();
	}
}
