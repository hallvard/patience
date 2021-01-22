package no.hal.patience;

public interface PilesOperationRule<P extends Enum<P>> {
    
    MoveCardsOperation accept(Patience<P> patience, Pile source, int sourcePos);
    MoveCardsOperation accept(Patience<P> patience, Pile source, int sourcePos, Pile target, int targetPos);
}
