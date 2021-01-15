package no.hal.patience.idiot.fx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import no.hal.patience.Card;
import no.hal.patience.Patience;
import no.hal.patience.Pile;
import no.hal.patience.SuitKind;
import no.hal.patience.fx.PatienceController;
import no.hal.patience.fx.PileView;
import no.hal.patience.fx.PilesView;
import no.hal.patience.idiot.core.Idiot;
import no.hal.patience.util.SuitsPredicate;

public class IdiotController extends PatienceController<Idiot> {

    static class ExamplesPatience extends Patience {

        private List<Pile> sourcePiles;
        private List<Pile> targetPiles;

        @Override
        public void initPiles() {
            sourcePiles = new ArrayList<>();
            for (var suit : SuitKind.values()) {
                sourcePiles.add(Pile.empty(SuitsPredicate.sameAs(suit)));
            }
            System.out.println(sourcePiles);
            putPiles("sources", sourcePiles);
    
            Pile deck = Pile.deck();
            deck.shuffle();
            List<Card> cards = deck.getAllCards();
            int pileCount = 6;
            List<Collection<Card>> allCards = new ArrayList<Collection<Card>>();
            for (int i = 0; i < pileCount; i++) {
                allCards.add(new ArrayList<>());
            }
            for (int i = 0; i < cards.size(); i++) {
                allCards.get(i % pileCount).add(cards.get(i));
            }
            targetPiles = new ArrayList<>();
            for (int i = 0; i < pileCount; i++) {
                targetPiles.add(Pile.of(allCards.get(i)));
            }
            System.out.println(targetPiles);
            putPiles("targets", targetPiles);
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
    protected Idiot createPatience() {
        return new Idiot();
    }
    
    //
    
    @FXML
    private PilesView sources;
    
    @FXML
    private PilesView targets;

	protected void initialize() {
        super.initialize();
        sources.getPiles().addAll(getPatience().getPiles("sources").stream().map(pile -> new PileView(pile)).collect(Collectors.toList()));
        targets.getPiles().addAll(getPatience().getPiles("targets").stream().map(pile -> new PileView(pile)).collect(Collectors.toList()));
    }

    @Override
    public Iterator<PileView> getSourcePiles() {
        return sources.getPiles().iterator();
    }

    @Override
    public Iterator<PileView> getTargetPiles() {
        return targets.getPiles().iterator();
    }
}
