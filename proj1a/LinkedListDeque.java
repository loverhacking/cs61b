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
    public LinkedListDeque(){
        sentinel = new Node<>(null, null, null);
        size = 0;
    }


    /** Adds an item of type T to the front of the deque */
    public void addFirst(T item){


        if (isEmpty()) {
            sentinel.next = new Node<>(null, item, sentinel.next);
            sentinel.next.prev = sentinel.next;
            sentinel.next.next = sentinel.next;
        }
        else {
            sentinel.next = new Node<>(sentinel.next.prev, item, sentinel.next);
            sentinel.next.prev.next = sentinel.next;
        }

        size += 1;
    }

    /** Adds an item of type T to the back of the deque */
    public void addLast(T item){
        if (isEmpty()) {
            addFirst(item);
            return;
        }

        else {
            Node<T> p = new Node<>(null, item, null);

            sentinel.next.prev.next = p;
            p.prev = sentinel.next.prev;
            sentinel.next.prev = p;
            p.next = sentinel.next;
        }
        size += 1;

    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty(){
        return size == 0;
    }

    /** Returns the number of items in the deque. */
    public int size(){
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space. */
    public void printDeque(){
        Node<T> current = sentinel.next;
        for(int i = 0; i < size; i++){
            System.out.print(current.data + " ");
            current = current.next;
        }
    }

    /** Removes and returns the item at the front of the deque. If no such item exists, returns null */
    public T removeFirst(){

        if (isEmpty()){
            return null;
        }

        Node<T> first = sentinel.next;
        T data = first.data;

        first.next.prev = first.prev;
        first.prev.next = first.next;

        sentinel.next = sentinel.next.next;

        size -= 1;

        return data;
    }

    /** Removes and returns the item at the back of the deque. If no such item exists, returns null. */
    public T removeLast(){
        if (isEmpty()){
            return null;
        }


        Node<T> last = sentinel.next.prev;

        T data = last.data;

        sentinel.next.prev = last.prev;
        last.prev.next = sentinel.next;
        size -= 1;

        return data;
    }

    /** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    public T get(int index){
        Node<T> current = sentinel.next;
        for(int i = 0; i < size; i++){
            if(i == index){
                return current.data;
            }
            current = current.next;
        }
        return null;

    }

    public T getRecursive(Node<T> p, int index){
        if(index == 0){
            return p.data;
        }
        else if (index < 0) {
            return null;
        }
        return getRecursive(p.next,index-1);

    }

    /** :Same as get, but uses recursion */
    public T getRecursive(int index){
       return getRecursive(sentinel.next, index);
    }

    public static void main(String[] args) {
        LinkedListDeque<Integer> deque = new LinkedListDeque<>();
//        deque.addFirst(1);
//        deque.addFirst(2);
//        deque.addFirst(3);
        deque.addLast(4);
        deque.addLast(5);
        deque.addLast(6);

        deque.printDeque();
//        System.out.println(deque.removeFirst());
//        System.out.println(deque.removeLast());
//        deque.printDeque();
        //System.out.println(deque.getRecursive(1));
    }


}
