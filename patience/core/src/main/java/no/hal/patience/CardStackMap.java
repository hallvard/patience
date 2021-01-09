package patience;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CardStackMap implements Iterable<CardStack> {
	
	Map<Object, CardStack> stacks = new HashMap<Object, CardStack>();
	
	public CardStack getCardStack(Object key) {
		CardStack stack = stacks.get(key);
		return stack;
	}
	
	public void putCardStack(Object key, CardStack stack) {
		stacks.put(key, stack);
	}

	public void putCardStacks(Iterator<?> keys, Iterator<? extends CardStack> stacks) {
		while (keys.hasNext() && stacks.hasNext()) {
			putCardStack(keys.next(), stacks.next());
		}
	}
	
	//

	public Iterator<CardStack> iterator(final Iterator<?> keys) {
		return new Iterator<CardStack>() {
			public boolean hasNext() {
				return keys.hasNext();
			}
			public CardStack next() {
				return stacks.get(keys.next());
			}
			public void remove() {
				throw new UnsupportedOperationException(this.getClass() + " does not support Iterator.remove()");
			}
		};
	}
	
	public static Iterator<Object> keys(int start, int end) {
		return keys(null, start, end);
	}
	public static Iterator<Object> keys(String prefix, int start, int end) {
		return keys(prefix, start, end, null);
	}
	public static Iterator<Object> keys(final String prefix, final int start, final int end, final String suffix) {
		return new Iterator<Object>() {
			private int count = start;
			public boolean hasNext() {
				return count <= end;
			}
			public Object next() {
				String s = String.valueOf(count++);
				if (prefix != null) {
					s = prefix + s;
				}
				if (suffix != null) {
					s = s + suffix;
				}
				return s;
			}
			public void remove() {
				throw new UnsupportedOperationException(this.getClass() + " does not support Iterator.remove");
			}
		};
	}
	
	public Iterator<CardStack> iterator() {
		return iterator(stacks.keySet().iterator());
	}
	public Iterator<CardStack> iterator(int start, int end) {
		return iterator(keys(null, start, end));
	}
	public Iterator<CardStack> iterator(String prefix, int start, int end) {
		return iterator(keys(prefix, start, end));
	}
	public Iterator<CardStack> iterator(final String prefix, final int start, final int end, final String suffix) {
		return iterator(keys(prefix, start, end, suffix));
	}
}
