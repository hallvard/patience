package no.hal.patience.fx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import no.hal.patience.Card;
import no.hal.patience.Pile;
import no.hal.patience.fx.util.NodeAlignment;
import no.hal.patience.util.CardsListener;

public class PileView extends Region implements CardsListener<Pile> {

	private Pile pile;

	public PileView() {
		this(null);
	}

	public PileView(final Pile pile) {
        faceDownOffset.addListener(layoutChangeListener);
		faceUpOffset.addListener(layoutChangeListener);
        cardScaling.addListener(layoutChangeListener);
        cardNames.addListener((ListChangeListener.Change<? extends Object> change) -> updateCardViews());
        cards.addListener((ListChangeListener.Change<? extends Object> change) -> updateChildren());
        setPile(pile);
	}

    @Override
    public String toString() {
        return super.toString() + " for " + pile;
    }

	public Pile getPile() {
		return pile;
	}

	public void setPile(final Pile pile) {
		if (this.pile != null) {
			pile.removeCardsListener(this);
		}
		this.pile = pile;
		if (this.pile != null) {
			pile.addCardsListener(this);
		}
        updateCardNames();
	}

	//

	private final ChangeListener<Object> layoutChangeListener = (observable, oldValue, newValue)  -> updateLayout();

	//

	private final ObservableList<String> cardNames = FXCollections.observableArrayList();
    
	public ObservableList<String> getCardNames() {
        return cardNames;
	}
    
    private final ObservableList<CardView> cards = FXCollections.observableArrayList();

    public ObservableList<CardView> getCards() {
        return cards;
    }

	//

	private SimpleDoubleProperty cardScaling = new SimpleDoubleProperty(1.0);

	public SimpleDoubleProperty cardScalingProperty() {
		return cardScaling;
	}
	public double getCardScaling() {
		return cardScaling.get();
	}
	public void setCardScaling(final double scaling) {
		cardScaling.set(scaling);
	}

    //

	private SimpleObjectProperty<Dimension2D> faceDownOffset = new SimpleObjectProperty<Dimension2D>(new Dimension2D(10.0,  10.0));

	public SimpleObjectProperty<Dimension2D> faceDownOffsetProperty() {
		return faceDownOffset;
	}
	public Dimension2D getFaceDownOffset() {
		return faceDownOffset.get();
	}
	public void setFaceDownOffset(final Dimension2D offset) {
		faceDownOffset.set(offset);
	}

    //
    
	private SimpleObjectProperty<Dimension2D> faceUpOffset = new SimpleObjectProperty<Dimension2D>(new Dimension2D(10.0,  10.0));

	public SimpleObjectProperty<Dimension2D> faceUpOffsetProperty() {
		return faceUpOffset;
	}
	public Dimension2D getFaceUpOffset() {
		return faceUpOffset.get();
	}
	public void setFaceUpOffset(final Dimension2D offset) {
		faceUpOffset.set(offset);
	}

	//

    private String getCardName(Card card) {
        return (card.isFaceDown() ? "~" : "") + card.getSuit().name().substring(0, 1).toUpperCase() + card.getFace();
    }

    @Override
    public void cardsChanged(Pile cards, List<Card> oldCards, List<Card> newCards) {
        updateCardNames();
    }

	protected void updateCardNames() {
        if (pile != null) {
            this.cardNames.setAll(pile.getAllCards().stream().map(this::getCardName).collect(Collectors.toList()));
        }
        if (this.cardNames.isEmpty()) {
            // placeholder
            this.cardNames.setAll("");
        }
	}

	protected void updateCardViews() {
        // System.out.println("Updating card views for " + getCardNames());
		final Collection<CardView> newCards = new ArrayList<CardView>();
		for (var cardName: cardNames) {
			var faceUp = true;
			if (cardName.startsWith("~")) {
				faceUp = false;
				cardName = cardName.substring(1);
			}
			newCards.add(new CardView(cardName, faceUp));
		}
		getCards().setAll(newCards);
	}

	protected void updateChildren() {
        getChildren().clear();
        getChildren().addAll(cards);
        // System.out.println("Updating children layout (" + getChildren().size()  + ") for " + getCardNames());
		updateLayout();
	}

	protected void updateLayout() {
        double x = 0.0, y = 0.0;
        double tx = 0.0, ty = 0.0;
        double width = 0.0, height = 0.0;
        int cardNum = 0, cardCount = 0;
        for (final CardView cardNode : cards) {
			if (cardNode == dragging) {
				tx += draggingOffset.getWidth();
                ty += draggingOffset.getHeight();
				cardCount++;
			}
			if (dragCardCount >= 0 && cardCount > dragCardCount) {
				cardCount = 0;
				tx -= draggingOffset.getWidth();
				ty -= draggingOffset.getHeight();
			} else if (cardCount > 0) {
				cardCount++;
			}
			cardNode.setLayoutX(x);
			cardNode.setLayoutY(y);
            cardNode.setTranslateX(tx);
            cardNode.setTranslateY(ty);
			final Dimension2D offset = getCardOffset(cardNode, cardNum, cards.size());
			cardNode.setScaleX(getCardScaling());
			cardNode.setScaleY(getCardScaling());
			width = Math.max(width, x + cardNode.prefWidth(-1) * getCardScaling());
			height = Math.max(height, y + cardNode.prefHeight(-1) * getCardScaling());
			x += offset.getWidth();
            y += offset.getHeight();
			cardNum++;
		}
		setWidth(width);
		setHeight(height);
	}

	protected Dimension2D getCardOffset(final CardView cardNode, final int cardPos, final int cardCount) {
		return cardNode.isFaceUp() ? getFaceUpOffset() : getFaceDownOffset();
	}

	//

	private Node dragging = null;
	private int dragCardCount = 1;
	private Point2D draggingStart = null;
	private Dimension2D draggingOffset = null;

	protected int getElementIndexAt(final Point2D scenePoint) {
		final List<CardView> children = cards;
		for (int i = children.size() - 1; i >= 0; i--) {
			final CardView child = children.get(i);
			if (child.contains(child.sceneToLocal(scenePoint))) {
				return i;
			}
		}
		return -1;
	}

	public void startDragging(final Point2D point, final CardView node, final int dragCardCount) {
		final int elementPos = getElementIndexAt(point);
		if (elementPos >= 0) {
			dragging = cards.get(elementPos);
			draggingStart = new Point2D(point.getX(), point.getY());
			this.dragCardCount = (dragCardCount >= 0 ? dragCardCount : cards.size() - elementPos);
		}
	}

	public void drag(final Point2D point) {
		if (dragging != null && draggingStart != null) {
			draggingOffset = new Dimension2D(point.getX() - draggingStart.getX(), point.getY() - draggingStart.getY());
			updateLayout();
		}
    }
    
    public boolean isDragging() {
        return draggingOffset != null;
    }

    public boolean hasDragged(double distance) {
        if (draggingOffset == null) {
            return false;
        }
        double dx = draggingOffset.getWidth(), dy = draggingOffset.getHeight();
        return Math.sqrt(dx * dx + dy * dy) > distance;
    }

	public int getDragPos() {
		return cards.size() - dragCardCount;
	}

	public void stopDragging(final Point2D point) {
		dragging = null;
		draggingStart = null;
		draggingOffset = null;
		dragCardCount = 0;
		updateLayout();
	}

	//

	public void locate(final NodeAlignment xAlignment, final NodeAlignment yAlignment) {
		NodeAlignment.locate(this, xAlignment, yAlignment);
	}

	public void xLocate(final NodeAlignment xAlignment) {
		NodeAlignment.xLocate(this, xAlignment);
	}
	public void yLocate(final NodeAlignment yAlignment) {
		NodeAlignment.yLocate(this, yAlignment);
	}
}
