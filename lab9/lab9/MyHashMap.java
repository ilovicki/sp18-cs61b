package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;
    private Set<K> keys;

    private double loadFactor() {
        return size / (double) buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
        keys = new HashSet<>();
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

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
//        throw new UnsupportedOperationException();
        return buckets[hash(key)].get(key);

    }

    /* Associates the specified value with the specified key in this map. */
    private int hashHelper(K key, int cap) {
        if (key == null) {
            return 0;
        }
        return Math.floorMod(key.hashCode(), cap);
    }
    private void resize(int cap) {
        ArrayMap<K, V>[] temp = new ArrayMap[cap];
        for (int i = 0; i < cap; i += 1) {
            temp[i] = new ArrayMap<>();
        }
        for (K k: keySet()) {
            temp[hashHelper(k, cap)].put(k, get(k));
        }
        buckets = temp;
    }
    @Override
    public void put(K key, V value) {
//        throw new UnsupportedOperationException();
        if (loadFactor() >= MAX_LF) {
            resize(buckets.length * 2);
        }
        if (get(key) == null) {
            size += 1;
            keys.add(key);
        }
        buckets[hash(key)].put(key, value);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
//        throw new UnsupportedOperationException();
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
//        throw new UnsupportedOperationException();
        return keys;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
//        throw new UnsupportedOperationException();
        return buckets[hash(key)].remove(key);
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
//        throw new UnsupportedOperationException();
        return buckets[hash(key)].remove(key, value);
    }

    @Override
    public Iterator<K> iterator() {
//        throw new UnsupportedOperationException();
        return keys.iterator();
    }
}
