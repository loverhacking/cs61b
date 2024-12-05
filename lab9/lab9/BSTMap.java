package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author zjy
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) return null;
        if (p.key.compareTo(key) == 0) {
            return p.value;
        } else if (p.key.compareTo(key) < 0) {
            return getHelper(key, p.right);
        } else {
            return getHelper(key, p.left);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            return new Node(key, value);
        }
        if (p.key.compareTo(key) == 0) {
            return p;
        }
        if (p.key.compareTo(key) < 0) {
            p.right = putHelper(key, value, p.right);
        } else if (p.key.compareTo(key) > 0) {
            p.left = putHelper(key, value, p.left);
        }
        return p;

    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */

    @Override
    public void put(K key, V value) {
        if (this.containsKey(key)) {
            putHelper(key, value, root).value = value;
            return;
        }
        root = putHelper(key, value, root);
        size++;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    private void traverseAdd(Node p, Set<K> keys) {
        if (p == null) return;
        keys.add(p.key);
        traverseAdd(p.left, keys);
        traverseAdd(p.right, keys);
    }
    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        traverseAdd(root, keys);
        return keys;
    }

    /**
     * find the node to replace the removed node
     * when the removed node has 2 children.
     */
    private Node findMin(Node p) {
        if (p.left == null) return p;
        return findMin(p.left);
    }

    /**
     * remove the found minNode
     * and return the removed node's right tree
     */
    private Node removeMin(Node p) {
        if (p.left == null) return p.right;
        p.left = removeMin(p.left);
        return p;
    }

    private Node removeHelper(Node p, K key) {
        if (p.key.compareTo(key) < 0) {
            p.right = removeHelper(p.right, key);
        } else if (p.key.compareTo(key) > 0) {
            p.left = removeHelper(p.left, key);
        } else {

            // the removed node has 0 or 1 child
            if (p.left == null) return p.right;
            if (p.right == null) return p.left;

            // the removed node has 2 children
            Node t = p;
            // find the node to replace the removed node
            p = findMin(t.right);
            p.right = removeMin(t.right);
            p.left = t.left;
        }
        return p;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
//        V value = get(key);
//        if (!this.containsKey(key)) {
//            return null;
//        }
//
//        removeHelper(root, key);
//        size--;
//        return value;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

//        if (!this.containsKey(key)) {
//            return null;
//        }
//        V returnValue = get(key);
//        if (!returnValue.equals(value)) {
//            return null;
//        }
//        removeHelper(root, key);
//        size--;
//        return returnValue;
//    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }




}
