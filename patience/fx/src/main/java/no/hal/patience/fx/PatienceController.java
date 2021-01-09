package patience.fx;

import java.util.Iterator;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.NonInvertibleTransformException;
import patience.CardStack;
import patience.CardStackMap;
import patience.Patience;

public abstract class PatienceController<T extends Patience> {

	protected T patience;

	protected abstract T createPatience();

	public T getPatience() {
		return patience;
	}

	@FXML
	protected void initialize() {
		patience = createPatience();
	}

	protected CardStackMap stacks = new CardStackMap();

	public abstract Iterator<CardStackView> getSourceStacks();
	public abstract Iterator<CardStackView> getTargetStacks();

	//

	protected String moveTopCards(final CardStack source, final int cardCount, final CardStack target) {
		if (! CardStack.canMoveTopCards(source, cardCount, target)) {
			return "Illegal move";
		}
		CardStack.moveTopCards(source, cardCount, target);
		return null;
	}

	//

	public static CardStack findTarget(final CardStack source, final int cardCount, final Iterator<? extends CardStack> cardStacks) {
		while (cardStacks.hasNext()) {
			final CardStack cardStack = cardStacks.next();
			if (CardStack.canMoveTopCards(source, cardCount, cardStack)) {
				return cardStack;
			}
		}
		return null;
	}
	public static CardStack findTarget(final CardStack source, final int cardCount, final Iterator<? extends CardStack>... cardStacks) {
		CardStack target = null;
		for (int i = 0; i < cardStacks.length; i++) {
			final Iterator<? extends CardStack> iterator = cardStacks[i];
			target = findTarget(source, cardCount, iterator);
			if (target != null) {
				return target;
			}
		}
		return null;
	}

	public static CardStack findTarget(final CardStack source, final int cardCount, final CardStackMap cardStacks, final Iterator<? extends Object> keys) {
		return findTarget(source, cardCount, cardStacks.iterator(keys));
	}

	//

	private static <T extends Node> T getElementAt(final Iterator<T> nodes, final Point2D scenePoint, final T checkAfter) {
		boolean found = false;
		while (nodes.hasNext()) {
			final T node = nodes.next();
			final Point2D localPoint = getLocalPoint(node, scenePoint);
			//			System.out.println("Checking " + localPoint + " in " + (node instanceof CardStackView ? ((CardStackView) node).getCardStack() : node));
			if (node == checkAfter) {
				found = true;
			} else if (node.contains(localPoint)) {
				//				System.out.println("Found it!");
				return node;
			}
		}
		return (found && checkAfter.contains(scenePoint) ? checkAfter : null);
	}

	private static Point2D getLocalPoint(final Node node, final Point2D scenePoint) {
		try {
			return node.getLocalToSceneTransform().inverseTransform(scenePoint);
		} catch (final NonInvertibleTransformException e) {
		}
		return null;
	}

	private CardStackView getCardStack(final Point2D scenePoint, final Iterator<CardStackView> cardStacks, final CardStackView checkAfter) {
		return getElementAt(cardStacks, scenePoint, checkAfter);
	}

	private CardStackView dragging = null;

	@FXML
	void mousePressed(final MouseEvent e) {
		final Point2D scenePoint = getScenePoint(e);
		dragging = getCardStack(scenePoint, getSourceStacks(), null);
		if (dragging != null) {
			e.consume();
			dragging.startDragging(scenePoint, null, -1);
			updateDragStatus(dragging.getCardStack(), dragging.getDragPos(), null);
		}
	}

	private Point2D getScenePoint(final MouseEvent e) {
		return new Point2D(e.getSceneX(), e.getSceneY());
	}

	protected boolean canDrag(final CardStack cardStack) {
		return cardStack != getPatience().getDeck();
	}

	private boolean isDragging() {
		return dragging != null;
	}

	public void mouseDragged(final MouseEvent e) {
		if (isDragging()) {
			e.consume();
			if (canDrag(dragging.getCardStack())) {
				final Point2D scenePoint = getScenePoint(e);
				dragging.drag(scenePoint);
				final CardStackView dropping = getCardStack(scenePoint, getTargetStacks(), dragging);
				if (dropping != null) {
					updateDragStatus(dragging.getCardStack(), dragging.getDragPos(), dropping.getCardStack());
				}
			}
		}
	}

	public void mouseReleased(final MouseEvent e) {
		final Point2D scenePoint = getScenePoint(e);
		e.consume();
		final CardStackView dropping = getCardStack(scenePoint, getTargetStacks(), dragging);
		if (dragging != null && dropping == dragging && dropping.getCardStack() == getPatience().getDeck() && getPatience().canDeal()) {
			//			getPatience().deal();
		} else if (isDragging()) {
			final CardStack source = dragging.getCardStack();
			final int cardCount = source.getCardCount() - dragging.getDragPos();
			final Point2D localPoint = getLocalPoint(dragging, scenePoint);
			dragging.stopDragging(localPoint);
			dragging = null;
			CardStack target = null;
			if (e.getClickCount() > 1) {
				target = getDefaultTarget(source, cardCount);
			} else if (dropping != null) {
				target = dropping.getCardStack();
			}
			if (target != null && target != source) {
				moveTopCards(source, cardCount, target);
				final Boolean result = getPatience().isFinished();
				if (result != null) {
					doFinished(result);
				}
			}
		}
	}

	protected void updateDragStatus(final CardStack source, final int sourceCardPos, final CardStack target) {
	}

	public void mouseClicked(final MouseEvent e) {}
	public void mouseEntered(final MouseEvent e) {}
	public void mouseExited(final MouseEvent  e) {}

	//

	protected void doFinished(final boolean result) {
	}

	protected CardStack getDefaultTarget(final CardStack source, final int cardCount) {
		return findTarget(source, cardCount, stacks.iterator());
	}

	//

	protected static NodeAlignment at(final Node relativeTo) { return NodeAlignment.at(relativeTo);}
	protected static NodeAlignment centeredOn(final Node relativeTo) { return NodeAlignment.centeredOn(relativeTo);}

	protected static NodeAlignment above(final Node relativeTo, final double spacing) { return NodeAlignment.above(relativeTo, spacing);}
	protected static NodeAlignment leftOf(final Node relativeTo, final double spacing) { return NodeAlignment.leftOf(relativeTo, spacing);}

	protected static NodeAlignment below(final Node relativeTo, final double spacing) { return NodeAlignment.below(relativeTo, spacing);}
	protected static NodeAlignment rightOf(final Node relativeTo, final double spacing) { return NodeAlignment.rightOf(relativeTo, spacing);}
}
