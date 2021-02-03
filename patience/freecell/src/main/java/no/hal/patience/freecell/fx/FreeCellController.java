package no.hal.patience.freecell.fx;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import no.hal.patience.fx.PatienceController;
import no.hal.patience.fx.PilesView;
import no.hal.patience.freecell.core.FreeCell;

public class FreeCellController extends PatienceController<FreeCell, FreeCell.PileKinds> {

    @Override
    protected FreeCell createPatience() {
        return new FreeCell();
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

    @FXML
	protected void initialize() {
        super.initialize();
    }
}
