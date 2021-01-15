package no.hal.patience;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Patience implements Iterable<Pile> {
    
    private Map<String, Collection<Pile>> piless = new HashMap<>();
    private Map<String, Pile> piles = new HashMap<>();

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

    public abstract boolean canDeal();

    public abstract boolean isFinished();
}