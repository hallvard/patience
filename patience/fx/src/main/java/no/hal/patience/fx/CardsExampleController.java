package no.hal.patience.fx;

import java.util.Iterator;

import javafx.fxml.FXML;
import no.hal.patience.Patience;

public class CardsExampleController extends PatienceController<CardsExampleController.ExamplesPatience> {

    static class ExamplesPatience extends Patience {

        @Override
        public void initPiles() {
        }

        @Override
        public boolean canDeal() {
            return false;
        }

        @Override
        public boolean isFinished() {
            return false;
        }        
    }

    @Override
    protected ExamplesPatience createPatience() {
        return new ExamplesPatience();
    }
    
    //
    
    @FXML
    private PilesView sources;
    
    @FXML
    private PilesView targets;

    @Override
    public Iterator<PileView> getSourcePiles() {
        return sources.getPiles().iterator();
    }

    @Override
    public Iterator<PileView> getTargetPiles() {
        return targets.getPiles().iterator();
    }
}
