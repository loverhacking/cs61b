public class LinkedListDeque<T> {

    private static class Node<T> {
        Node<T> prev;
        T data;
        Node<T> next;
        public Node(Node<T> prev, T data, Node<T> next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }

    private int size;
    private Node<T> sentinel;

    /** create an empty deque */
    public LinkedListDeque() {
        sentinel = new Node<>(null, null, null);

        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }


    /** Adds an item of type T to the front of the deque */
    public void addFirst(T item) {

        Node<T> p = new Node<>(null, item, null);

        p.next = sentinel.next;
        p.prev = sentinel;

        sentinel.next.prev = p;
        sentinel.next = p;

        size += 1;
    }

    /** Adds an item of type T to the back of the deque */
    public void addLast(T item) {

        Node<T> p = new Node<>(null, item, null);

        sentinel.prev.next = p;
        p.prev = sentinel.prev;

        p.next = sentinel;
        sentinel.prev = p;

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
        Node<T> current = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(current.data + " ");
            current = current.next;
        }
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null */
    public T removeFirst() {

        if (isEmpty()) {
            return null;
        }

        T data = sentinel.next.data;

        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;

        size -= 1;
        return data;
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        T data = sentinel.prev.data;

        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;

        size -= 1;
        return data;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    public T get(int index) {
        Node<T> current = sentinel.next;
        for (int i = 0; i < size; i++) {
            if (i == index) {
                return current.data;
            }
            current = current.next;
        }
        return null;

    }

    private T getRecursive(Node<T> p, int index) {
        if (index == 0) {
            return p.data;
        } else if (index < 0) {
            return null;
        }
        return getRecursive(p.next, index - 1);

    }

    /** Same as get, but uses recursion */
    public T getRecursive(int index) {
        return getRecursive(sentinel.next, index);
    }
}
