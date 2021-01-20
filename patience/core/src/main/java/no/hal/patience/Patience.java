package no.hal.patience;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Patience implements Iterable<Pile> {
    
    private Map<String, Collection<Pile>> piless = new HashMap<>();
    private Map<String, Pile> piles = new HashMap<>();

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[" + getClass().getSimpleName() + "\n");
        for (var piles : piless.keySet()) {
            int num = 0;
            for (var pile : piless.get(piles)) {
                builder.append("   " + piles + "." + num + "=" + pile + "\n");
                num++;
            }
        }
        for (var pile : piles.keySet()) {
            builder.append("   " + pile + "=" + piles.get(pile) + "\n");
        }
        builder.append("]");
        return builder.toString();
    }

    protected void putPiles(String category, Collection<Pile> piles) {
        this.piless.put(category, piles);
    }

    protected void putPile(String name, Pile pile) {
        this.piles.put(name, pile);
    }

    public abstract void initPiles();

    public Collection<Pile> getPiles(String category) {
        if (! piless.containsKey(category)) {
            throw new IllegalArgumentException("The pile category must be one of " + piless.keySet());
        }
        return piless.get(category);
    }

    public Pile getPile(String name) {
        if (! piles.containsKey(name)) {
            throw new IllegalArgumentException("The pile name must be one of " + piles.keySet());
        }
        return piles.get(name);
    }

    @Override
    public Iterator<Pile> iterator() {
        var allPiles = new ArrayList<Pile>();
        for (var piles : this.piless.values()) {
            allPiles.addAll(piles);
        }
        allPiles.addAll(piles.values());
        return allPiles.iterator();
    }

    public Collection<String> getPileCategories() {
        return Collections.unmodifiableCollection(piless.keySet());
    }

    public Collection<String> getPileNames() {
        return Collections.unmodifiableCollection(piles.keySet());
    }

    public final static String DECK_PILE_NAME = "deck";

    public Pile getDeck() {
        return getPile(DECK_PILE_NAME);
    }

    private Collection<PilesOperation> pilesOperations = null;

    protected void initPilesOperations() {
        pilesOperations = new ArrayList<>();
    }

    public boolean updatePilesOperations() {
        if (pilesOperations == null) {
            initPilesOperations();
        }
        return true;
    }

    //

    public PilesOperation matchPilesOperation(PilesOperation po, Pile source, int cardCount, Pile target) {
        if (po instanceof MoveCardsOperation mco && (mco.getCount() < 0 || mco.getCount() == cardCount)) {
            MoveCardsOperation mco2 = mco.withCount(cardCount);
            if (mco2.canApply(source, target)) {
                return mco2;
            }
        }
        return null;
    }

    public PilesOperation findMoveCardsOperation(Pile source, int cardCount, Pile target) {
        for (var po : pilesOperations) {
            PilesOperation po2 = matchPilesOperation(po, source, cardCount, target);
            if (po2 != null) {
                return po;
            }
        }
        return null;
    }

	public boolean canMoveCards(Pile source, int cardCount, Pile target) {
		return findMoveCardsOperation(source, cardCount, target) != null;
	}

	public void moveCards(Pile source, int cardCount, Pile target) {
        PilesOperation po = findMoveCardsOperation(source, cardCount, target);
        if (po != null) {
            po.apply(source, target);
        }
	}
}
