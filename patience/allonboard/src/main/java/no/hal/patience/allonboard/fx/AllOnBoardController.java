package no.hal.patience.allonboard.fx;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import no.hal.patience.fx.PatienceController;
import no.hal.patience.fx.PilesView;
import no.hal.patience.fx.PileView;
import no.hal.patience.allonboard.core.AllOnBoard;

public class AllOnBoardController extends PatienceController<AllOnBoard, AllOnBoard.PileKinds> {

    @Override
    protected AllOnBoard createPatience() {
        return new AllOnBoard();
    }

    //
    
    @FXML
    private Parent pilesParent;

    @FXML private PilesView stacksView;
    @FXML private PileView deckView;
    @FXML private PileView deck2View;

    @FXML
	protected void initialize() {
        super.initialize(pilesParent);
    }
}
