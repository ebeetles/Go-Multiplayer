import java.util.Iterator;

public class MyHashSet<E> implements Iterable<E> {
	Object[] arr;
	int size;
	int c = 0;

	public MyHashSet() {
		size = 99999;
		arr = new Object[size];
	}

	public boolean add(E obj) {
		if (arr[obj.hashCode()] == null) {
			arr[obj.hashCode()] = obj;
			return true;
		}
		return false;
	}

	public void clear() {
		arr = new Object[size];
	}

	public boolean contains(Object obj) {
		if (arr[obj.hashCode()] == null) {
			return false;
		}
		c++;
		return true;
	}

	public boolean remove(Object obj) {
		if (arr[obj.hashCode()] == null) {
			return false;
		}
		arr[obj.hashCode()] = null;
		c--;
		return true;
	}

	public int size() {
		return c;
	}

	public Iterator<E> iterator() {
		return new HashSetIterator();
	}

	private class HashSetIterator implements Iterator<E> {
		private int currentIndex = 0;

		public boolean hasNext() {
			while (currentIndex < size && arr[currentIndex] == null) {
				currentIndex++;
			}
			return currentIndex < size;
		}

		@SuppressWarnings("unchecked")
		public E next() {
			if (hasNext()) {
				return (E) arr[currentIndex++];
			}
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public DLList<E> toDLList() {
		DLList<E> list = new DLList<E>();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] != null) {
				list.add((E) arr[i]);
			}
		}
		return list;
	}
}