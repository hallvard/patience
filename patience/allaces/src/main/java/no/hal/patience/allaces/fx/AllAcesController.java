package no.hal.patience.allaces.fx;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import no.hal.patience.fx.PatienceController;
import no.hal.patience.fx.PilesView;
import no.hal.patience.fx.PileView;
import no.hal.patience.allaces.core.AllAces;

public class AllAcesController extends PatienceController<AllAces, AllAces.PileKinds> {

    @Override
    protected AllAces createPatience() {
        return new AllAces();
    }

    //
    
    @FXML
    private Parent pilesParent;

    @Override
    protected Parent getPilesParent() {
        return pilesParent;
    }

    @FXML private PilesView stacksView;
    @FXML private PileView deckView;
    @FXML private PileView deck2View;

    @FXML
	protected void initialize() {
        super.initialize();
    }
}
