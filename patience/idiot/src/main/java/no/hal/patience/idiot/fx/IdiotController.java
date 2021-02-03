package no.hal.patience.idiot.fx;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import no.hal.patience.fx.PatienceController;
import no.hal.patience.fx.PileView;
import no.hal.patience.fx.PilesView;
import no.hal.patience.idiot.core.Idiot;

public class IdiotController extends PatienceController<Idiot, Idiot.PileKinds> {

    @Override
    protected Idiot createPatience() {
        return new Idiot();
    }

    //
    
    @FXML
    private Parent pilesParent;

    @Override
    public Parent getPilesParent() {
        return pilesParent;
    }

    @FXML private PilesView suitsView;
    @FXML private PilesView stacksView;
    @FXML private PileView extrasView;
    @FXML private PileView deckView;
    @FXML private PileView deck2View;

    @FXML
	protected void initialize() {
        super.initialize();
    }
}
