package no.hal.patience;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Patience implements Iterable<Pile> {
    
    private Map<String, Collection<Pile>> piles = new HashMap<>();

    protected void putPiles(String category, Collection<Pile> piles) {
        this.piles.put(category, piles);
    }

    public abstract void initPiles();

    public Collection<Pile> getPiles(String category) {
        if (! piles.containsKey(category)) {
            throw new IllegalArgumentException("The pile category must be one of " + piles.keySet());
        }
        return piles.get(category);
    }

    @Override
    public Iterator<Pile> iterator() {
        var allPiles = new ArrayList<Pile>();
        for (var piles : this.piles.values()) {
            allPiles.addAll(piles);
        }
        return allPiles.iterator();
    }

    public Collection<String> getPileCategories() {
        return Collections.unmodifiableCollection(piles.keySet());
    }

    public Pile getDeck() {
        return getPiles("deck").iterator().next();
    }

    public abstract boolean canDeal();

    public abstract boolean isFinished();
}