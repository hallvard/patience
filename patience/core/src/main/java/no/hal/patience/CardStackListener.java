package patience;

public interface CardStackListener {
	public void cardsTurned(CardStack cardStack, int start, int end);
	public void cardsAdded  (CardStack cardStack, int start, int end);
	public void cardsRemoved(CardStack cardStack, int start, int end);
}
