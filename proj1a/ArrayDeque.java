public class ArrayDeque<T> {

    private int size;
    private T[] array;
    private int length = 8;
    private int start;
    private double ratio;

    public ArrayDeque() {
        array = (T[]) new Object[length];
        start = 5;
        size = 0;
        ratio = (double) size / length;
    }

    /** Adds an item of type T to the front of the deque */
    public void addFirst(T item) {

        if (start == 0)  {
            T[] t = (T[]) new Object[size * 4];
            length = t.length;
            System.arraycopy(array, 0, t, length / 2, size);
            array = t;
            start = length / 2;
        }

        if (isEmpty()) {
            length = 8;
            array = (T[]) new Object[length];
            start = 5;
        }

        array[start - 1] = item;
        start -= 1;

        size += 1;

    }

    /** Adds an item of type T to the back of the deque */
    public void addLast(T item) {

        if (start + size == length) {
            extend(item);
        }
        array[start + size] = item;
        size += 1;

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
        for (int i = start; i < start + size; i++) {
            System.out.print(array[i] + " ");
        }
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        T item = array[start];
        array[start] = null;

        start += 1;
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
        T item = array[start + size - 1];
        array[start + size - 1] = null;


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
        return array[index + start];
    }

    private void extend(T item) {
        T[] temp = (T[]) new Object[size * 4];
        System.arraycopy(array, start, temp, start * 2, size);
        start = start * 2;
        temp[start + size] = item;
        array = temp;
        length = array.length;

    }

    private void narrow() {
        ratio = (double) size / length;

        if (ratio < 0.25) {
            T[] temp = (T[]) new Object[length / 2];
            System.arraycopy(array, start, temp, start / 2, size);

            start = start / 2;
            array = temp;
            length = array.length;
        }
    }

    public static void main(String[] args) {
        ArrayDeque<Integer> l = new ArrayDeque<>();


        l.addFirst(0);
        l.addFirst(1);
        System.out.println(l.removeLast());
        System.out.println(l.removeLast());
        l.isEmpty();
        l.isEmpty();
        l.isEmpty();
        l.addFirst(7);


    }


    
}
