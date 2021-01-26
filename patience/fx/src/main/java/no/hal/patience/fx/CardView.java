package no.hal.patience.fx;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

public class CardView extends ImageView {

	private static CardImageProvider cardImageProvider = new CardImageProvider();

	private final BooleanProperty faceUp = new SimpleBooleanProperty(true);
	private final StringProperty card = new SimpleStringProperty("");

	public CardView() {
		this("", true);
	}

	public CardView(final String card, final boolean faceUp) {
		setFaceUp(faceUp);
		setCard(card);
		updateImage();
		cardProperty().addListener((observable, oldValue, newValue) -> updateImage());
		faceUpProperty().addListener((observable, oldValue, newValue) -> updateImage());
	}

    @Override
    public String toString() {
        return super.toString() + " for " + getCard();
    }

	protected void updateImage() {
		setImage(isFaceUp() ? cardImageProvider.getImage(getCard()) : cardImageProvider.getFaceDownImage());
	}

	public StringProperty cardProperty() {
		return card;
	}
	public String getCard() {
		return card.get();
	}
	public void setCard(final String card) {
		cardProperty().set(card);
	}

	public BooleanProperty faceUpProperty() {
		return faceUp;
	}
	public boolean getFaceUp() {
		return faceUp.get();
	}
	public boolean isFaceUp() {
		return faceUp.get();
	}
	public void setFaceUp(final boolean faceUp) {
		faceUpProperty().set(faceUp);
	}

	public void turn() {
		setFaceUp(! getFaceUp());
	}
}
