package no.hal.patience.idiot.fx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import no.hal.patience.Patience;
import no.hal.patience.Pile;
import no.hal.patience.fx.PatienceController;
import no.hal.patience.fx.PileView;
import no.hal.patience.fx.PilesView;
import no.hal.patience.fx.util.FxUtil;
import no.hal.patience.idiot.core.Idiot;

public class IdiotController extends PatienceController<Idiot> {

    @Override
    protected Idiot createPatience() {
        return new Idiot();
    }
    
    //
    
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

	protected void initialize() {
        super.initialize();
        suits.getPiles().addAll(createPileViews("suits"));
        stacks.getPiles().addAll(createPileViews("stacks"));
        extras.setPile(getPatience().getPile("extras"));
        deck.setPile(getPatience().getPile(Patience.DECK_PILE_NAME));
        deck2.setPile(getPatience().getPile("deck2"));

        sourcePiles = new ArrayList<>();
        sourcePiles.add(deck);
        sourcePiles.add(deck2);
        sourcePiles.add(extras);
        sourcePiles.addAll(stacks.getPiles());

        targetPiles = new ArrayList<>();
        targetPiles.add(deck2);
        targetPiles.addAll(stacks.getPiles());
        targetPiles.addAll(suits.getPiles());
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
