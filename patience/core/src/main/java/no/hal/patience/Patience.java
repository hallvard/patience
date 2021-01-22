package no.hal.patience;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Patience<P extends Enum<P>> implements Iterable<Pile> {
    
    private Map<Enum<P>, Collection<Pile>> piless = new HashMap<>();
    private Map<Enum<P>, Pile> piles = new HashMap<>();

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

    protected void putPiles(Enum<P> category, Collection<Pile> piles) {
        this.piless.put(category, piles);
    }

    protected void putPile(Enum<P> name, Pile pile) {
        this.piles.put(name, pile);
    }

    public abstract void initPiles();

    public Collection<Pile> getPiles(Enum<P> category) {
        if (! piless.containsKey(category)) {
            throw new IllegalArgumentException("The pile category must be one of " + piless.keySet());
        }
        return piless.get(category);
    }

    public Pile getPile(Enum<P> name) {
        if (! piles.containsKey(name)) {
            throw new IllegalArgumentException("The pile name must be one of " + piles.keySet());
        }
        return piles.get(name);
    }

    public Enum<P> getPileKind(Pile pile) {
        for (Enum<P> category : piless.keySet()) {
            if (piless.get(category).contains(pile)) {
                return category;
            }
        }
        for (Enum<P> name : piles.keySet()) {
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

    public Collection<Enum<P>> getPileCategories() {
        return Collections.unmodifiableCollection(piless.keySet());
    }

    public Collection<Enum<P>> getPileNames() {
        return Collections.unmodifiableCollection(piles.keySet());
    }

    public Pile getDeck() {
        return null;
    }

    //

    private Collection<PilesOperationRule> pilesOperationRules = null;

    protected void addPilesOperationRules(PilesOperationRule... rules) {
        pilesOperationRules.addAll(Arrays.asList(rules));
    }

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
