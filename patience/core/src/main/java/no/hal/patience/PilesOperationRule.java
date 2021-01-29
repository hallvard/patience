package no.hal.patience;

public interface PilesOperationRule<P extends Enum<P>> {
    PilesOperation accept(Patience<P> patience, Pile source, int cardCount);
    PilesOperation accept(Patience<P> patience, Pile source, int cardCount, Pile target, int targetPos);
}
