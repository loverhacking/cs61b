public class ArrayDeque<T> {

    private int size;
    private T[] array;
    private int length = 8;
    private int start;
    private int end;
    private double ratio;
    private boolean iscircular;

    public ArrayDeque() {
        array = (T[]) new Object[length];
        start = 4;
        end = 5;
        size = 0;
        ratio = (double) size / length;
        iscircular = false;
    }

    /** Adds an item of type T to the front of the deque */
    public void addFirst(T item) {

        if (start < 0 && size != length) {
            start = length - 1;
            iscircular = true;
        }

        if (size == length) {
            extend(item);
        }


        array[start] = item;


        size += 1;
        start -= 1;
    }

    /** Adds an item of type T to the back of the deque */
    public void addLast(T item) {


        if (end == length && size != length) {
            end = 0;
            iscircular = true;
        }

        if (size == length) {
            extend(item);
        }


        array[end] = item;

        size += 1;
        end += 1;
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
        if (start < end && !iscircular) {
            for (int i = start + 1; i < end; i++) {
                System.out.print(array[i] + " ");
            }
        } else {
            /** if start > end, first print start -> length - 1 */
            for (int i = start + 1; i < length; i++) {
                System.out.print(array[i] + " ");
            }
            /** then print 0 -> end */
            for (int i = 0; i < end; i++) {
                System.out.print(array[i] + " ");
            }

        }
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null */
    public T removeFirst() {

        if (size == 0) {
            return null;
        }

        T first;
        if (start == length - 1) {
            start = 0;
            first = array[start];
            iscircular = false;
        } else {
            start += 1;
            first = array[start];
            array[start] = null;
        }

        size -= 1;

        narrow();
        return first;
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }

        T last;

        if (end == 0) {
            end = length - 1;
            last = array[end];
            iscircular = false;
        } else {
            end -= 1;
            last = array[end];
            array[end] = null;
        }

        size -= 1;

        narrow();

        return last;

    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    public T get(int index) {
        if (index < 0 || index > array.length - 1) {
            return null;
        } else if (!iscircular) {
            return array[index + start + 1];
        } else {
            return array[(start + index + 1) % length];
        }
    }

    private void extend(T item) {
        T[] temp = (T[]) new Object[size * 2];

        if (start == 0 && end == length - 1) {
            System.arraycopy(array, start, temp, size / 2, size);

            start = size / 2 - 1;
            end = start + size;

        } else {

            /** 0 -> end */
            System.arraycopy(array, 0, temp, 0, end);

            /** start -> length - 1 */

            System.arraycopy(array, start + 1, temp,
                    temp.length - (length - start) + 1, length - start - 1);

            start = temp.length - (length - start);

        }
        array = temp;
        length = array.length;

    }

    private void narrow() {
        ratio = (double) size / length;

        if (ratio < 0.25 && ratio > 0) {

            T[] temp = (T[]) new Object[length / 2];

            if (!iscircular) {
                System.arraycopy(array, start + 1, temp, start / 2, size);
                start = start / 2 - 1;
                end = start + size + 1;
            } else {
                /** 0 -> end */
                System.arraycopy(array, 0, temp, 0, end);

                /** start -> length - 1 */

                System.arraycopy(array, start + 1, temp,
                        temp.length - (length - start) + 1, length - start - 1);

                start = temp.length - (length - start);

            }

            array = temp;
            length = array.length;
        }
    }
    
}
