package patience.theidiot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import patience.CardStack;
import patience.fx.CardStackView;
import patience.fx.CardStacksView;
import patience.fx.PatienceController;
import patience.util.SuitStack;

public class TheIdiotController extends PatienceController<TheIdiot> {


	@Override
	protected TheIdiot createPatience() {
		return new TheIdiot();
	}

	@FXML
	Region parent;

	@FXML
	CardStacksView suitStacks;

	@FXML
	CardStacksView idiotStacks;

	@FXML
	CardStacksView deckPileStacks;

	@FXML
	CardStackView extraPile;

	@FXML
	@Override
	protected void initialize() {

		super.initialize();

		final TheIdiot idiot = getPatience();

		for (final SuitStack suitStack : idiot.getSuitStacks()) {
			final CardStackView cardStackView = new CardStackView();
			cardStackView.setFaceUpOffset(new Dimension2D(1.0, 0.0));
			cardStackView.setPrefWidth(100);
			cardStackView.setPrefHeight(250);
			cardStackView.setCardStack(suitStack);
			stacks.putCardStack(suitStack.getSuit(), suitStack);
			suitStacks.getCardStacks().add(cardStackView);
		}
		suitStacks.setPrefWidth(600);
		suitStacks.setPrefHeight(250);

		int idiotStackNum = 0;
		for (final CardStack idiotStack : idiot.getIdiotStacks()) {
			final CardStackView cardStackView = new CardStackView();
			cardStackView.setFaceUpOffset(new Dimension2D(0.0, 40.0));
			cardStackView.setPrefWidth(100);
			cardStackView.setPrefHeight(800);
			cardStackView.setCardStack(idiotStack);
			stacks.putCardStack(Integer.valueOf(idiotStackNum), idiotStack);
			idiotStackNum++;
			idiotStacks.getCardStacks().add(cardStackView);
		}
		idiotStacks.setPrefWidth(600);
		idiotStacks.setPrefHeight(800);

		idiot.deal();

		final CardStackView deckView = new CardStackView();
		deckView.setFaceDownOffset(new Dimension2D(1.0, 0.0));
		deckView.setPrefWidth(100);
		deckView.setPrefHeight(200);
		deckView.setCardStack(idiot.getDeck());

		final CardStackView pileView = new CardStackView();
		pileView.setFaceDownOffset(new Dimension2D(2.0, 0.0));
		pileView.setFaceUpOffset(new Dimension2D(40.0, 0.0));
		pileView.setPrefWidth(100);
		pileView.setPrefHeight(250);
		pileView.setCardStack(idiot.getPile());

		deckPileStacks.getCardStacks().add(deckView);
		deckPileStacks.getCardStacks().add(pileView);
		deckPileStacks.setPrefWidth(500);
		deckPileStacks.setPrefHeight(250);

		extraPile.setFaceUpOffset(new Dimension2D(2.0, 0.0));
		extraPile.setCardStack(idiot.getExtraPile());
	}


	@Override
	public Iterator<CardStackView> getSourceStacks() {
		final Collection<CardStackView> sourceStacks = new ArrayList<>();
		sourceStacks.addAll(idiotStacks.getCardStacks());
		sourceStacks.addAll(deckPileStacks.getCardStacks());
		sourceStacks.add(extraPile);
		return sourceStacks.iterator();
	}

	@Override
	public Iterator<CardStackView> getTargetStacks() {
		final Collection<CardStackView> sourceStacks = new ArrayList<>();
		sourceStacks.addAll(suitStacks.getCardStacks());
		sourceStacks.addAll(idiotStacks.getCardStacks());
		return sourceStacks.iterator();
	}

	@FXML
	void deckClicked(final MouseEvent e) {
		getPatience().deal();
		System.out.println(getPatience().getDeck() + " -> " + getPatience().getPile());
	}

	@Override
	protected void updateDragStatus(final CardStack source, final int sourceCardPos, final CardStack target) {
		System.out.println("Dragging #" + sourceCardPos + " " + source.getCard(sourceCardPos) + " to " + target);
	}
}
