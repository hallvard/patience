package no.hal.patience.fx;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Region;
import no.hal.patience.fx.util.NodeAlignment;

public class PilesView extends Region {

	public PilesView() {
		piles.addListener((ListChangeListener.Change<? extends Object> change) -> updateChildren());
		pileSpacing.addListener(layoutChangeListener);
	}
	
	private ChangeListener<Object> layoutChangeListener = new ChangeListener<Object>() {
		@Override
		public void changed(ObservableValue<? extends Object> observable, Object oldValue, Object newValue) {
			updateChildren();		
		}
	};

	//

	private ObservableList<PileView> piles = FXCollections.observableArrayList();

	public ObservableList<PileView> getPiles() {
		return piles;
	}

	public SimpleDoubleProperty pileSpacing = new SimpleDoubleProperty(1.0);
	
	public ObservableValue<Number> pileSpacingProperty() {
		return pileSpacing;
	}
	public double getPileSpacing() {
		return pileSpacing.get();
	}
	public void setPileSpacing(double spacing) {
		pileSpacing.set(spacing);
	}

	//

	protected void updateChildren() {
		getChildren().clear();
		for (var pile: piles) {
			getChildren().add(pile);
        }
        System.out.println("Updating layout for " + piles.size() + " piles");
		updateLayout();
	}

	protected void updateLayout() {
		double x = 0.0, y = 0.0;
		for (var pile: piles) {
			pile.setLayoutX(x);
			pile.setLayoutY(y);
			x += pile.getBoundsInLocal().getWidth() + getPileSpacing();
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