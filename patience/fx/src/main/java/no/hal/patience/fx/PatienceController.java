package no.hal.patience.fx;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

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

    protected abstract Parent getPilesParent();

	protected void initialize() {
        patience = createPatience();
        patience.initPiles();
        patience.updatePilesOperations();

        createPileViews(getPilesParent());
        registerMouseListeners(getPilesParent());
    }

    private Map<Pile, PileView> pileViewMap = null;

    protected void createPileViews(Parent pilesParent) {
        pileViewMap = new HashMap<>();
        for (var pileCategory : getPatience().getPileCategories()) {
            Node view = pilesParent.lookup("#" + pileCategory.name() + "View");
            if (view instanceof PilesView pv) {
                var pileViews = createPileViews(pileCategory);
                pv.getPiles().addAll(pileViews);
                pileViews.forEach(pileView -> pileViewMap.put(pileView.getPile(), pileView));
            }
        }
        for (var pileName : getPatience().getPileNames()) {
            Node view = pilesParent.lookup("#" + pileName.name() + "View");
            if (view instanceof PileView pv) {
                var pile = getPatience().getPile(pileName);
                pv.setPile(pile);
                pileViewMap.put(pile, pv);
            }
        }
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

	public Iterator<PileView> getSourcePiles() {
        return getPatience().getMoveCardsOperationSourcePiles().stream().map(pileViewMap::get).collect(Collectors.toList()).iterator();
    }

	public Iterator<PileView> getTargetPiles() {
        return getPatience().getMoveCardsOperationTargetPiles().stream().map(pileViewMap::get).collect(Collectors.toList()).iterator();
    }

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
            updateViewOrder(dragging, -1.0);
			updateDragStatus(dragging.getPile(), dragging.getDragPos(), null);
		}
	}

	private void updateViewOrder(Node node, double viewOrder) {
        while (node != getPilesParent()) {
            node.setViewOrder(viewOrder);
            node = node.getParent();
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
            updateViewOrder(dragging, 0.0);
			dragging = null;
			if (e.getClickCount() > 1) {
                if (cardCount <= 1) {
                    cardCount = -1;
                }
                doMoveCards(source, cardCount, null);
			} else if (dropping != null) {
                doMoveCards(source, cardCount, dropping.getPile());
            }
		}
	}

    protected void doMoveCards(Pile source, int cardCount, Pile target) {
        System.out.println("Trying source -(" + cardCount + ")> target: " + source + " -> " + target);
        boolean performed = patience.moveCards(source, cardCount, target);
        if (performed) {
            final Boolean result = getPatience().updatePilesOperations();
            if (result != null) {
                doFinished(result);
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
