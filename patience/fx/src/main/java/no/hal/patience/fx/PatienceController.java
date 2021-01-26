package no.hal.patience.fx;

import java.util.Collection;
import java.util.Iterator;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.NonInvertibleTransformException;

import no.hal.patience.Pile;
import no.hal.patience.fx.util.FxUtil;
import no.hal.patience.fx.util.NodeAlignment;
import no.hal.patience.Patience;

public abstract class PatienceController<T extends Patience<P>, P extends Enum<P>> {

	protected T patience;

	protected abstract T createPatience();

	public T getPatience() {
		return patience;
	}

	protected void initialize(Parent pilesParent) {
        patience = createPatience();
        patience.initPiles();
        patience.updatePilesOperations();

        if (pilesParent != null) {
            registerMouseListeners(pilesParent);
        }
    }

    protected void initialize() {
        initialize(null);
    }

    protected void registerMouseListeners(Parent pilesParent) {
        pilesParent.setOnMousePressed(this::mousePressed);
        pilesParent.setOnMouseDragged(this::mouseDragged);
        pilesParent.setOnMouseReleased(this::mouseReleased);
    }

    protected Collection<PileView> createPileViews(Collection<Pile> piles) {
        return FxUtil.createPileViews(piles);
    }

    protected Collection<PileView>  createPileViews(Enum<P> category) {
        return createPileViews(getPatience().getPiles(category));
    }

	public abstract Iterator<PileView> getSourcePiles();
	public abstract Iterator<PileView> getTargetPiles();

    //

	private static <T extends Node> T getElementAt(final Iterator<T> nodes, final Point2D scenePoint, final T checkAfter) {
		boolean found = false;
		while (nodes.hasNext()) {
			final T node = nodes.next();
			final Point2D localPoint = getLocalPoint(node, scenePoint);
			//			System.out.println("Checking " + localPoint + " in " + (node instanceof PileView ? ((PileView) node).getPile() : node));
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

	private PileView getPile(final Point2D scenePoint, final Iterator<PileView> Piles, final PileView checkAfter) {
		return getElementAt(Piles, scenePoint, checkAfter);
	}

	private PileView dragging = null;

	@FXML
	public void mousePressed(final MouseEvent e) {
		final Point2D scenePoint = getScenePoint(e);
        dragging = getPile(scenePoint, getSourcePiles(), null);
        // System.out.println("mousePressed.dragging=" + dragging);
		if (dragging != null) {
			e.consume();
			dragging.startDragging(scenePoint, null, -1);
			updateDragStatus(dragging.getPile(), dragging.getDragPos(), null);
		}
	}

	private Point2D getScenePoint(final MouseEvent e) {
		return new Point2D(e.getSceneX(), e.getSceneY());
	}

	protected boolean canDrag(final Pile pile) {
		return true; // getPatience().canMoveCards(pile, pile.getCardCount() - 1, null);
	}

	private boolean isDragging() {
		return dragging != null;
	}

	@FXML
	public void mouseDragged(final MouseEvent e) {
		if (isDragging()) {
			e.consume();
			if (canDrag(dragging.getPile())) {
				final Point2D scenePoint = getScenePoint(e);
				dragging.drag(scenePoint);
				final PileView dropping = getPile(scenePoint, getTargetPiles(), dragging);
                // System.out.println("mouseDragged.dropping=" + dropping);
				if (dropping != null) {
					updateDragStatus(dragging.getPile(), dragging.getDragPos(), dropping.getPile());
				}
			}
		}
	}

	@FXML
	public void mouseReleased(final MouseEvent e) {
		final Point2D scenePoint = getScenePoint(e);
		e.consume();
		final PileView dropping = getPile(scenePoint, getTargetPiles(), dragging);
		if (isDragging()) {
			final Pile source = dragging.getPile();
			int cardCount = source.getCardCount() - dragging.getDragPos();
			final Point2D localPoint = getLocalPoint(dragging, scenePoint);
			dragging.stopDragging(localPoint);
			dragging = null;
			Pile target = null;
			if (e.getClickCount() > 1) {
                if (cardCount <= 1) {
                    cardCount = -1;
                }
                target = patience.getDefaultTarget(source, cardCount);
			} else if (dropping != null) {
				target = dropping.getPile();
			}
            System.out.println("Source -(" + cardCount + ")> target: " + source + " -> " + target);
			if (target != null && target != source) {
				patience.moveCards(source, cardCount, target);
				final Boolean result = getPatience().updatePilesOperations();
				if (result != null) {
					doFinished(result);
				}
			}
		}
	}

	protected void updateDragStatus(final Pile source, final int sourceCardPos, final Pile target) {
    }

	//

	protected void doFinished(final boolean result) {
	}

	//

	protected static NodeAlignment at(final Node relativeTo) { return NodeAlignment.at(relativeTo);}
	protected static NodeAlignment centeredOn(final Node relativeTo) { return NodeAlignment.centeredOn(relativeTo);}

	protected static NodeAlignment above(final Node relativeTo, final double spacing) { return NodeAlignment.above(relativeTo, spacing);}
	protected static NodeAlignment leftOf(final Node relativeTo, final double spacing) { return NodeAlignment.leftOf(relativeTo, spacing);}

	protected static NodeAlignment below(final Node relativeTo, final double spacing) { return NodeAlignment.below(relativeTo, spacing);}
	protected static NodeAlignment rightOf(final Node relativeTo, final double spacing) { return NodeAlignment.rightOf(relativeTo, spacing);}
}
