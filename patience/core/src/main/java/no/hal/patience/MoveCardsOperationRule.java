package no.hal.patience;

public class MoveCardsOperationRule<P extends Enum<P>> extends AbstractMoveCardsOperationRule<P> {

    public MoveCardsOperationRule(Enum<P> sourcePileKind, Enum<P> targetPileKind, int count) {
        super(sourcePileKind, targetPileKind, count);
    }
    public MoveCardsOperationRule(Enum<P> sourcePileKind, Enum<P> targetPileKind) {
        this(sourcePileKind, targetPileKind, -1);
    }

    public PilesOperation createPilesOperation(Pile source, int cardCount, Pile target, int targetPos) {
        return new MoveCardsOperation(source, cardCount, target, targetPos).options(getOptions());
    }
}
