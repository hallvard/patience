package patience.fx;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import patience.Deck;
import patience.Pile;

public class CardsExampleController {

	@FXML CardStackView cardStack1;
	@FXML CardStackView cardStack2;

	@FXML
	void initialize() {
		final Deck deck = new Deck(100);
		final Pile pile = new Pile(deck);
		deck.deal(pile, 6);
		pile.revealTopCards(4);
		cardStack2.setCardStack(pile);
	}

	@FXML
	void mousePressed(final MouseEvent event) {
		final Node node = event.getPickResult().getIntersectedNode();
		if (node instanceof CardView && cardStack1.getCards().contains(node)) {
			cardStack1.startDragging(new Point2D(event.getX(), event.getY()), (CardView) node, event.isShiftDown() ? 1 : -1);
		}
	}
	@FXML
	void mouseDragged(final MouseEvent event) {
		cardStack1.drag(new Point2D(event.getX(), event.getY()));
	}
	@FXML
	void mouseReleased(final MouseEvent event) {
		cardStack1.stopDragging(new Point2D(event.getX(), event.getY()));
	}
}
