package no.hal.patience.fx;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Dimension2D;
import javafx.scene.layout.Pane;
import no.hal.patience.fx.util.FxUtil;
import no.hal.patience.fx.util.NodeAlignment;

public class PilesView extends Pane {

    private List<PilesView> pileParents = null;

    public PilesView() {
        piles.addListener((ListChangeListener.Change<? extends Object> change) -> updateChildren());
        pileSpacing.addListener(layoutChangeListener);
        registerPileViewPropertyListeners();
    }

    private ChangeListener<Object> layoutChangeListener = (observable, oldValue, newValue) -> updateLayout();

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

    private void initPileParents() {
        pileParents = new ArrayList<>();
        for (var child : lookupAll("PilesView")) {
            if (child != this && child instanceof PilesView pilesView) {
                pileParents.add(pilesView);
            }
        }
        if (pileParents.isEmpty()) {
            pileParents.add(this);
        }
    }

    protected void updateChildren() {
        if (pileParents == null) {
            initPileParents();
        }
        for (PilesView pileParent : pileParents) {
            pileParent.getChildren().clear();
        }
        int pileNum = 0;
        for (PileView pile : piles) {
            var pileParent = pileParents.get(pileNum % pileParents.size());
            pileParent.getChildren().add(pile);
            pileNum++;
        }
        updateLayout();
	}

	protected void updateLayout() {
        if (pileParents == null) {
            initPileParents();
        }
        int pileNum = 0;
        for (PileView pile : piles) {
            var pileParent = pileParents.get(pileNum % pileParents.size());
            pile.setCardScaling(pileParent.getPilesCardScaling());
            pile.setFaceDownOffset(pileParent.getPilesFaceDownOffset());
            pile.setFaceUpOffset(pileParent.getPilesFaceUpOffset());
            pileNum++;
        }
        for (PilesView pileParent : pileParents) {
            if (pileParent != this) {
                pileParent.updateLayout();
            }
        }
        double x = 0.0, y = 0.0;
        for (var child : getChildren()) {
            if (child instanceof PileView pile) {
                pile.setLayoutX(x);
                pile.setLayoutY(y);
                x += pile.getWidth() + getPileSpacing();
            }
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
    
    // pile view properties, overrides those on children if set

    protected void registerPileViewPropertyListeners() {
        pilesCardScaling.addListener((prop, oldValue, newValue) -> FxUtil.setPileViewProperties(PileView::cardScalingProperty, (Double) newValue, getPiles()));
        pilesFaceDownOffset.addListener((prop, oldValue, newValue) -> FxUtil.setPileViewProperties(PileView::faceDownOffsetProperty, newValue, getPiles()));
        pilesFaceUpOffset.addListener((prop, oldValue, newValue) -> FxUtil.setPileViewProperties(PileView::faceUpOffsetProperty, newValue, getPiles()));
    }

    //

	private SimpleDoubleProperty pilesCardScaling = new SimpleDoubleProperty(1.0);

	public ObservableValue<Number> pilesCardScalingProperty() {
		return pilesCardScaling;
	}
	public double getPilesCardScaling() {
		return pilesCardScaling.get();
	}
	public void setPilesCardScaling(final double scaling) {
		pilesCardScaling.set(scaling);
    }
    
    //

	private SimpleObjectProperty<Dimension2D> pilesFaceDownOffset = new SimpleObjectProperty<Dimension2D>(new Dimension2D(10.0,  10.0));

	public ObservableValue<Dimension2D> pilesFaceDownOffsetProperty() {
		return pilesFaceDownOffset;
	}
	public Dimension2D getPilesFaceDownOffset() {
		return pilesFaceDownOffset.get();
	}
	public void setPilesFaceDownOffset(final Dimension2D offset) {
		pilesFaceDownOffset.set(offset);
    }
    
    //

	private SimpleObjectProperty<Dimension2D> pilesFaceUpOffset = new SimpleObjectProperty<Dimension2D>(new Dimension2D(10.0,  10.0));

	public ObservableValue<Dimension2D> pilesFaceUpOffsetProperty() {
		return pilesFaceUpOffset;
	}
	public Dimension2D getPilesFaceUpOffset() {
		return pilesFaceUpOffset.get();
	}
	public void setPilesFaceUpOffset(final Dimension2D offset) {
		pilesFaceUpOffset.set(offset);
	}

}
