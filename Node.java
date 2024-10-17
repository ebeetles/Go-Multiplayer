public class Node<E> {
	private E data;
	private Node<E> next;
	private Node<E> prev;

	public Node(E e) {
		data = e;
		next = null;
		prev = null;
	}

	public E get() {
		return data;
	}

	public void set(E e) {
		data = e;
	}

	public Node<E> next() {
		return next;
	}

	public Node<E> prev() {
		return prev;
	}

	public void setNext(Node<E> node) {
		next = node;
	}

	public void setPrev(Node<E> node) {
		prev = node;
	}
}