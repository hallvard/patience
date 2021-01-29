package no.hal.patience;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

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

    public boolean hasPiles(Enum<P> category) {
        return piless.containsKey(category);
    }

    public Collection<Pile> getPiles(Enum<P> category) {
        if (! piless.containsKey(category)) {
            throw new IllegalArgumentException("The pile category must be one of " + piless.keySet() + ", but was " + category);
        }
        return piless.get(category);
    }

    public boolean hasPile(Enum<P> name) {
        return piles.containsKey(name);
    }

    public Pile getPile(Enum<P> name) {
        if (! piles.containsKey(name)) {
            throw new IllegalArgumentException("The pile name must be one of " + piles.keySet() + ", but was " + name);
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

    private Collection<PilesOperationRule<P>> pilesOperationRules = null;

    protected void addPilesOperationRules(Collection<PilesOperationRule<P>> rules) {
        pilesOperationRules.addAll(rules);
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

    protected Collection<Pile> getMoveCardsOperationPiles(Function<MoveCardsOperationRule<P>, Enum<P>> kind) {
        Collection<Pile> piles = new ArrayList<>();
        for (var rule : pilesOperationRules) {
            if (rule instanceof MoveCardsOperationRule<P> mcor) {
                var pileKind = kind.apply(mcor);
                if (hasPile(pileKind)) {
                    piles.add(getPile(pileKind));
                } else if (hasPiles(pileKind)) {
                    piles.addAll(getPiles(pileKind));
                }
            }
        }
        return piles;
    }

    public Collection<Pile> getMoveCardsOperationSourcePiles() {
        return getMoveCardsOperationPiles(MoveCardsOperationRule::getSourcePileKind);
    }

    public Collection<Pile> getMoveCardsOperationTargetPiles() {
        return getMoveCardsOperationPiles(MoveCardsOperationRule::getTargetPileKind);
    }

    //

    protected PilesOperation findMoveCardsOperation(Function<PilesOperationRule<P>, PilesOperation> fun) {
        for (var rule : pilesOperationRules) {
            PilesOperation op = fun.apply(rule);
            if (op != null) {
                return op;
            }
        }
        return null;
    }

    protected PilesOperation findMoveCardsOperation(Pile source, int cardCount) {
        return findMoveCardsOperation(rule -> rule.accept(this, source, cardCount));
    }

    protected PilesOperation findMoveCardsOperation(Pile source, int cardCount, Pile target) {
        return findMoveCardsOperation(rule -> rule.accept(this, source, cardCount, target, target.getCardCount()));
    }
    
    public Pile getDefaultTarget(Pile source, int cardCount) {
        PilesOperation op = findMoveCardsOperation(source, cardCount);
		return (op instanceof MoveCardsOperation mco ? mco.getTarget() : null);
	}

    public boolean canMoveCards(Pile source, int cardCount, Pile target) {
		return findMoveCardsOperation(source, cardCount, target) != null;
	}

	public void moveCards(Pile source, int cardCount, Pile target) {
        PilesOperation op = findMoveCardsOperation(source, cardCount, target);
        if (op != null && op.canApply()) {
            op.apply();
        }
    }
}
