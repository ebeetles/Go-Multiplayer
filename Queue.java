public class Queue<E> {
	private MyArrayList<E> myList;

	public Queue() {
		myList = new MyArrayList<E>();
	}

	public void add(E item) {
		myList.add(item);
	}

	public void remove(E item) {
		int i = myList.indexOf(item);
		if (i == -1) return;
		
		myList.set(i, myList.get(myList.size() - 1));
		myList.remove(myList.size() - 1);
	}
	
	public MyArrayList<E> get() {
		return myList;
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		int index = 0;
		int level = 0;

		while (index < myList.size()) {
			int levelSize = (int) Math.pow(2, level);
			for (int i = 0; i < levelSize && index < myList.size(); i++) {
				result.append(myList.get(index)).append("\t");
				result.append("\n");

				index++;
			}
			level++;
		}
		return result.toString();
	}

	public E poll() { 
		E top = myList.get(0);
		myList.set(0, myList.get(myList.size() - 1));
		myList.remove(myList.size() - 1);

		return top;
	}

	public E peek() {
		return myList.get(0);
	}

	public boolean isEmpty() {
		return myList.size() == 0;
	}
}