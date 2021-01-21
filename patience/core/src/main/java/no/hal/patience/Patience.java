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

    public String getPileCategoryOrName(Pile pile) {
        for (String category : piless.keySet()) {
            if (piless.get(category).contains(pile)) {
                return category;
            }
        }
        for (String name : piles.keySet()) {
            if (piles.get(name) == pile) {
                return name;
            }
        }
        return null;
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

    //

    private Collection<PilesOperationRule> pilesOperationRules = null;

    protected void initPilesOperationRules() {
        pilesOperationRules = new ArrayList<>();
    }

    public boolean updatePilesOperations() {
        if (pilesOperationRules == null) {
            initPilesOperationRules();
        }
        return true;
    }

    //

	public boolean canMoveCards(Pile source, int cardCount, Pile target) {
        // TODO
		return true;
	}

	public void moveCards(Pile source, int cardCount, Pile target) {
        // TODO
	}
}
