package no.hal.patience.solitaire.fx;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import no.hal.patience.fx.PatienceController;
import no.hal.patience.fx.PileView;
import no.hal.patience.fx.PilesView;
import no.hal.patience.solitaire.core.Solitaire;

public class SolitaireController extends PatienceController<Solitaire, Solitaire.PileKinds> {

    @Override
    protected Solitaire createPatience() {
        return new Solitaire();
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
