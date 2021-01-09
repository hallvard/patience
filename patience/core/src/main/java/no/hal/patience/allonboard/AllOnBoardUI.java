package patience.allonboard;

import patience.fx.PatienceController;

public class AllOnBoardController extends PatienceController<AllOnBoard> {

	@Override
	protected AllOnBoard createPatience() {
		return new AllOnBoard();
	}

	@Override
	public void initUI(GCanvas gCanvas) {
		super.initUI(gCanvas);
		
		gCanvas.setSize(1250, 600);
		
		AllOnBoard allOnBoard = getPatience();

		Options defaults = new Options().setScaling(0.5).setSpacing(10.0);
		
		for (int row = 0; row < allOnBoard.getRowCount(); row++) {
			for (int column = 0; column < allOnBoard.getColumnCount(); column++) {
				AllOnBoardStack stack = allOnBoard.getAllOnBoardStack(row, column);
				stacks.putCardStack(row + "-" + column, stack);
				GCardStack gCardStack = new GCardStack(stack);
				gCardStack.setOptions(defaults);
				gCardStack.setLocation(getLocation(row, column, gCardStack, defaults));
				gCanvas.add(gCardStack);
			}
		}
		
		GCardStack deckCardStack = new GCardStack(allOnBoard.getDeck());
		deckCardStack.setOptions(defaults.copy().setHidingOffset(new GDimension(0.0, 2.0)));
		deckCardStack.setLocation(getLocation(4, 7, deckCardStack, defaults));

		gCanvas.add(deckCardStack);
	}
	
	private GPoint getLocation(int row, int column, GCardStack dimensions, Options options) {
		return new GPoint(40.0 + column * (dimensions.getWidth() + options.getSpacing()), 10.0 + row * (dimensions.getHeight() + options.getSpacing()));
	}
}
