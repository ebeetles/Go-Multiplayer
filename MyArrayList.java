import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class MyArrayList<E> {
    private Object[] list;
    private int size;
    private int capacity;

    public MyArrayList() {
        size = 0;
        capacity = 1;
        list = new Object[capacity];
    }

    public boolean add(E e) {
        size++;
        if (list.length <= size) {
            capacity *= 2;
            list = Arrays.copyOf(list, capacity);
        }

        try {
            list[size - 1] = e;
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void add(int num, E e) {
        size++;
        if (list.length <= size) {
            capacity *= 2;
            list = Arrays.copyOf(list, capacity);
        }
        for (int i = num; i < size; i++) {
            Object temp = list[i + 1];
            list[i + 1] = list[num];
            list[num] = temp;
        }
        list[num] = e;
    }

    @SuppressWarnings("unchecked")
    public E get(int i) {
        return (E) list[i];
    }

    public E remove(int i) {
        E e = get(i);
        if (i >= size - 1)
            list[i] = null;
        for (int j = i + 1; j < list.length; j++) {
            list[i] = list[j];
            i++;
        }
        list[size - 1] = null;
        size--;
        return e;
    }

    public E remove(E e) {
        for (int i = 0; i < size; i++) {
            if (list[i].equals(e)) {
                remove(i);
            }
        }
        return e;
    }

    public void set(int i, E e) {
        list[i] = e;
    }
    
    public Iterator<E> iterator() {
		return new ArrayListIterator();
	}

	private class ArrayListIterator implements Iterator<E> {
		private int currentIndex = 0;

		public boolean hasNext() {
			while (currentIndex < size && list[currentIndex] == null) {
				currentIndex++;
			}
			return currentIndex < size;
		}

		@SuppressWarnings("unchecked")
		public E next() {
			if (hasNext()) {
				return (E) list[currentIndex++];
			}
			return null;
		}
	}
	
	public int indexOf(E e) {
		if (e == null) return -1;
		for (int i = 0; i < size; i++) {
			if (e.equals(list[i])) return i;
		}
		return -1;
	}


    public String toString() {
        String endString = "[";
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                endString += ", ";
            }
            endString += list[i].toString();
        }
        endString += "]";
        return endString;
    }

    public int size() {
        return size;
    }
}
