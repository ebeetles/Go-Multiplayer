public class DLList<E> {

	private Node<E> head;
	private Node<E> tail;
	private int size;

	public DLList() {
		head = new Node<E>(null);
		tail = new Node<E>(null);
		size = 0;
		head.setNext(tail);
		tail.setPrev(head);
	}

	public Node<E> getTail() {
		return tail;
	}

	private Node<E> getNode(int n) {
		if (size == 0) {
			return new Node<E>(null);
		}
		if (n > size) {
			return new Node<E>(null);
		}
		Node<E> current = head.next();
		int i = 0;
		while (current != tail) {
			if (i == n) {
				return current;
			}
			current = current.next();
			i++;
		}
		return null;
	}

	public void add(E e) {
		Node<E> a = new Node<E>(e);
		tail.prev().setNext(a);
		a.setPrev(tail.prev());
		tail.setPrev(a);
		a.setNext(tail);
		size++;
	}

	public void add(int n, E e) {
		Node<E> prev = head;
		Node<E> current = head.next();
		Node<E> node = new Node<E>(e);
		int pos = 0;

		if (n == 0) {
			node.setPrev(head);
			node.setNext(head.next());
			head.next().setPrev(node);
			head.setNext(node);
		} else if (n == size) {
			node.setPrev(tail.prev());
			node.setNext(tail);
			tail.prev().setNext(node);
			tail.setPrev(node);
		} else {
			while (current != tail) {
				if (pos == n) {
					node.setNext(current);
					node.setPrev(prev);
					current.setPrev(node);
					prev.setNext(node);
					size++;
				}
				prev = prev.next();
				current = current.next();
				pos++;
			}
		}
		size++;
	}

	public E get(int n) {
		Node<E> temp = this.getNode(n);
		return temp.get();
	}

	public void remove(int n) {
		Node<E> r = getNode(n);
		r.prev().setNext(r.next());
		r.next().setPrev(r.prev());
		size--;
	}

	public void remove(E e) {
		Node<E> current = head;
		while (current.next() != null) {
			current = current.next();
			if (current.get().equals(e)) {
				current.prev().setNext(current.next());
				current.next().setPrev(current.prev());
				size--;
				break;
			}
		}
	}

	public void set(int n, E e) {
		Node<E> temp = this.getNode(n);
		temp.set(e);
	}

	public String toString() {
		String a = new String();
		Node<E> current = head;
		while (current.next().next() != null) {
			current = current.next();
			a += current.get();
			a += "   ";
		}
		return a;
	}

	public int size() {
		return size;
	}

}