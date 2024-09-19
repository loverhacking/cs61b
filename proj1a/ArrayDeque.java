public class ArrayDeque<T> {

    private int size;
    private T[] array;
    private int length = 8;
    private double ratio;

    public ArrayDeque() {
        array = (T[]) new Object[length];
        size = 0;
        ratio = (double) size / length;
    }

    /** Adds an item of type T to the front of the deque */
    public void addFirst(T item) {
        if (size == 0) {
            array[0] = item;
        } else if (size == length) {
            extend(item);
        } else {
            T[] temp = (T[]) new Object[length];
            System.arraycopy(array, 0, temp, 1, size);
            temp[0] = item;
            array = temp;
        }
        size += 1;

        narrow();
    }

    /** Adds an item of type T to the back of the deque */
    public void addLast(T item) {

        if (size == length) {
            extend(item);
        }
        array[size] = item;
        size += 1;
        narrow();
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space. */
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(array[i] + " ");
        }
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        T item = array[0];
        T[] temp = (T[]) new Object[size + 1];
        System.arraycopy(array, 1, temp, 0, size + 1);
        array = temp;
        length = array.length;
        size -= 1;

        narrow();
        return item;
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        T item = array[size - 1];
        array[size - 1] = null;
        size -= 1;

        narrow();
        return item;

    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    public T get(int index) {
        if (index < 0 || index > array.length - 1) {
            return null;
        }
        return array[index];
    }

    private void extend(T item) {
        T[] temp = (T[]) new Object[size * 2];
        System.arraycopy(array, 0, temp, 0, size);
        temp[size] = item;
        array = temp;
        length = array.length;

    }

    private void narrow() {
        ratio = (double) size / length;

        if (ratio > 1) {
            T[] temp = (T[]) new Object[length / 2];
            System.arraycopy(array, 0, temp, 0, size);

            array = temp;
            length = array.length;
        }
    }


    
}
