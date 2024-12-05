package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;



/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author zjy
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    public MyHashMap(int initialSize) {
        buckets = new ArrayMap[initialSize];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /**
     * Computes the hash function of the given key
     * and given length
     * use when resize is needed
     */
    private int hash(K key, int length) {
        if (key == null) {
            return 0;
        }
        return Math.floorMod(key.hashCode(), length);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {

        return buckets[hash(key)].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {

        ArrayMap<K, V> bucket = buckets[hash(key)];
        if (bucket.containsKey(key)) {
            bucket.remove(key, bucket.get(key));
        } else {
            size++;
        }
        bucket.put(key, value);
        if (loadFactor() > MAX_LF) {
            resize();
        }


    }

    private void resize() {
        ArrayMap<K, V>[] temp = new ArrayMap[buckets.length * 2];
        for (int i = 0; i < temp.length; i += 1) {
            temp[i] = new ArrayMap<>();
        }

        for (ArrayMap<K, V> bucket : buckets) {
            for (K key : bucket) {
                temp[hash(key, buckets.length * 2)].put(key, bucket.get(key));
            }
        }
        buckets = temp;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (ArrayMap<K, V> bucket : buckets) {
            keys.addAll(bucket.keySet());
        }
        return keys;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        ArrayMap<K, V> bucket = buckets[hash(key)];
        if (!bucket.containsKey(key)) {
            return null;
        }
        V value = bucket.remove(key);
        size--;
        return value;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        ArrayMap<K, V> bucket = buckets[hash(key)];
        if (!bucket.containsKey(key)) {
            return null;
        }
        V returnValue = bucket.remove(key, value);
        size--;
        return returnValue;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }


}
