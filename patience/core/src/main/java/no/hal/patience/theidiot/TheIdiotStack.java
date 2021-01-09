package patience.theidiot;

import patience.CardStack;
import patience.util.OppositeColorStack;

public class TheIdiotStack extends OppositeColorStack {

	private final CardStack emptySource;

	public TheIdiotStack(final int endFace, final CardStack emptySource) {
		super(-1, endFace);
		this.emptySource = emptySource;
	}

	@Override
	public boolean canMoveTo(final int toPos, final CardStack source, final int fromPos, final int count) {
		if (getCardCount() == 0 && emptySource != null && emptySource.getCardCount() > 0 && source != emptySource) {
			return false;
		}
		return super.canMoveTo(toPos, source, fromPos, count);
	}
}
