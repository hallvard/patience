package no.hal.patience;

public interface PilesOperationRule {
    
    MoveCardsOperation accept(Pile source, int sourcePos);
    MoveCardsOperation accept(Pile source, int sourcePos, Pile target, int targetPos);
}
