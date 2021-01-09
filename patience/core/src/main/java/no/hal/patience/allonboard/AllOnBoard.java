package patience.allonboard;

import patience.Card;
import patience.Patience;
import patience.Suit;

class AllOnBoard extends Patience {

	private final AllOnBoardStack[][] stacks = new AllOnBoardStack[4][14];

	public AllOnBoard() {
		super();
		for (final AllOnBoardStack[] row: stacks) {
			AllOnBoardStack previousStack = null;
			for (int column = 0; column < row.length; column++) {
				previousStack = (row[column] = new AllOnBoardStack(previousStack));
			}
		}
		doDeal();
	}

	public int getRowCount() {
		return stacks.length;
	}
	public int getColumnCount() {
		return stacks[0].length;
	}
	public AllOnBoardStack getAllOnBoardStack(final int row, final int column) {
		return stacks[row][column];
	}

	private int dealCount = 3;

	@Override
	public boolean canDeal() {
		return countTargetableStacks() == 0 && dealCount > 0;
	}

	@Override
	protected void doDeal() {
		for (final AllOnBoardStack[] row: stacks) {
			Suit rowSuit = null;
			boolean collect = false;
			for (int column = 0; column < row.length; column++) {
				final AllOnBoardStack stack = row[column];
				if (stack.getCardCount() == 0) {
					collect = true;
				} else {
					final Card card = stack.getTopCard();
					if (rowSuit == null) {
						rowSuit = card.getSuit();
					}
					if (card.getSuit() != rowSuit || card.getFace() - 1 != column) {
						collect = true;
					}
					if (collect) {
						getDeck().collect(stack, 1);
					}
				}
			}
		}
		getDeck().shuffle(10);
		for (final AllOnBoardStack[] row: stacks) {
			AllOnBoardStack previousStack = null;
			boolean deal = false;
			for (int column = 0; column < row.length; column++) {
				final AllOnBoardStack stack = row[column];
				if (column > 0 && previousStack.getCardCount() == 0) {
					deal = true;
				}
				if (deal) {
					getDeck().deal(stack, 1);
				}
				previousStack = stack;
			}
		}
		dealCount--;
	}

	int countTargetableStacks() {
		int count = 0;
		for (final AllOnBoardStack[] row: stacks) {
			AllOnBoardStack previousStack = null;
			for (int column = 0; column < row.length; column++) {
				final AllOnBoardStack stack = row[column];
				if (stack.getCardCount() == 0) {
					if (previousStack == null || (previousStack.getCardCount() > 0 && previousStack.getTopCard().getFace() != 13)) {
						count++;
					}
				}
				previousStack = stack;
			}
		}
		return count;
	}

	//

	@Override
	public Boolean isFinished() {
		for (final AllOnBoardStack[] row: stacks) {
			if (row[row.length - 1].getCardCount() > 0) {
				return null;
			}
		}
		return true;
	}
}
