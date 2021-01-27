package no.hal.patience.idiot.fx;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import no.hal.patience.fx.PatienceController;
import no.hal.patience.fx.PileView;
import no.hal.patience.fx.PilesView;
import no.hal.patience.idiot.core.Idiot;

public class FourXFourController extends PatienceController<FourXFour, FourXFour.PileKinds> {

    @Override
    protected FourXFour createPatience() {
        return new FourXFour();
    }

    //
    
    @FXML
    private Parent pilesParent;

    @FXML private PilesView suitsView;
    @FXML private PilesView stacksView;

    @FXML
	protected void initialize() {
        super.initialize(pilesParent);
    }
}
