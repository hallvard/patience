package no.hal.patience.twodownoneup.fx;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import no.hal.patience.fx.PatienceController;
import no.hal.patience.fx.PilesView;
import no.hal.patience.twodownoneup.core.TwoDownOneUp;

public class TwoDownOneUpController extends PatienceController<TwoDownOneUp, TwoDownOneUp.PileKinds> {

    @Override
    protected TwoDownOneUp createPatience() {
        return new TwoDownOneUp();
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
