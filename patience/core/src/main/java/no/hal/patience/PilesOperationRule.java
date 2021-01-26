package no.hal.patience;

public interface PilesOperationRule<P extends Enum<P>> {
    
    MoveCardsOperation accept(Patience<P> patience, Pile source, int cardCount);
    MoveCardsOperation accept(Patience<P> patience, Pile source, int cardCount, Pile target, int targetPos);
}
