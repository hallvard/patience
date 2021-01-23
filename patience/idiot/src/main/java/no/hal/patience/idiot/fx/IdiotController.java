package no.hal.patience.idiot.fx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    @FXML
    private PilesView suits;
    
    @FXML
    private PilesView stacks;

    @FXML
    private PileView extras;

    @FXML
    private PileView deck;

    @FXML
    private PileView deck2;

    @FXML
	protected void initialize() {
        super.initialize();

        System.out.println(patience);

        suits.getPiles().addAll(createPileViews(Idiot.PileKinds.suits));
        stacks.getPiles().addAll(createPileViews(Idiot.PileKinds.stacks));
        extras.setPile(getPatience().getPile(Idiot.PileKinds.extras));
        deck.setPile(getPatience().getPile(Idiot.PileKinds.deck));
        deck2.setPile(getPatience().getPile(Idiot.PileKinds.deck2));

        sourcePiles = new ArrayList<>();
        sourcePiles.add(deck);
        sourcePiles.add(deck2);
        sourcePiles.add(extras);
        sourcePiles.addAll(stacks.getPiles());

        targetPiles = new ArrayList<>();
        targetPiles.add(deck2);
        targetPiles.addAll(stacks.getPiles());
        targetPiles.addAll(suits.getPiles());

        registerMouseListeners(pilesParent);
    }

    private List<PileView> sourcePiles;
    private List<PileView> targetPiles;

    @Override
    public Iterator<PileView> getSourcePiles() {
        return sourcePiles.iterator();
    }

    @Override
    public Iterator<PileView> getTargetPiles() {
        return targetPiles.iterator();
    }
}
