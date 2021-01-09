package patience.fx;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;

public class CardStacksView extends Region {

	public CardStacksView() {
		cardStacks.addListener(new ListChangeListener<Object>() {
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Object> o) {
				updateChildren();
			}
		});
		cardStackSpacing.addListener(layoutChangeListener);
	}
	
	private ChangeListener<Object> layoutChangeListener = new ChangeListener<Object>() {
		@Override
		public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
			updateChildren();		
		}
	};

	//

	private ObservableList<CardStackView> cardStacks = FXCollections.observableArrayList();

	public ObservableList<CardStackView> getCardStacks() {
		return cardStacks;
	}

	public SimpleDoubleProperty cardStackSpacing = new SimpleDoubleProperty(1.0);
	
	public ObservableValue<Number> cardStackSpacingProperty() {
		return cardStackSpacing;
	}
	public double getCardStackSpacing() {
		return cardStackSpacing.get();
	}
	public void setCardStackSpacing(double Spacing) {
		cardStackSpacing.set(Spacing);
	}

	//

	protected void updateChildren() {
		getChildren().clear();
		for (CardStackView cardStack: cardStacks) {
			getChildren().add(cardStack);
		}
		updateLayout();
	}

	protected void updateLayout() {
		double x = 0.0, y = 0.0;
		for (CardStackView cardStack: cardStacks) {
			cardStack.setLayoutX(x);
			cardStack.setLayoutY(y);
			x += cardStack.getBoundsInLocal().getWidth() + getCardStackSpacing();
		}
	}
	
	//
	
	public void locate(NodeAlignment xAlignment, NodeAlignment yAlignment) {
		NodeAlignment.locate(this, xAlignment, yAlignment);
	}

	public void xLocate(NodeAlignment xAlignment) {
		NodeAlignment.xLocate(this, xAlignment);
	}
	public void yLocate(NodeAlignment yAlignment) {
		NodeAlignment.yLocate(this, yAlignment);
	}
}
