package no.hal.patience.idiot.fx;

public class Idiot {
    
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