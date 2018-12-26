package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v, Node l, Node r) {
            key = k;
            value = v;
            left = l;
            right = r;
        }
        private void copy(Node p) {
            if (p == null) {
                key = null;
                value = null;
                left = null;
                right = null;
            } else {
                key = p.key;
                value = p.value;
                left = p.left;
                right = p.right;
            }
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */
    private Set<K> keys;

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
        keys = new HashSet<>();
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
//        throw new UnsupportedOperationException();
        if (key == null || p == null) {
            return null;
        }
        if (key.compareTo(p.key) == 0) {
            return p.value;
        } else if (key.compareTo(p.key) < 0) {
            return getHelper(key, p.left);
        } else {
            return getHelper(key, p.right);
        }
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
//        throw new UnsupportedOperationException();
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private void putHelper(K key, V value, Node p) {
//        throw new UnsupportedOperationException();
        if (key.compareTo(p.key) == 0) {
            p.value = value;
        } else if (key.compareTo(p.key) < 0) {
            if (p.left == null) {
                p.left = new Node(key, value, null, null);
                size += 1;
                keys.add(key);
            } else {
                putHelper(key, value, p.left);
            }

        } else {
            if (p.right == null) {
                p.right = new Node(key, value, null, null);
                size += 1;
                keys.add(key);
            } else {
                putHelper(key, value, p.right);
            }
        }
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
//        throw new UnsupportedOperationException();
        if (key == null || value == null) {
            return;
        }
        if (root == null) {
            root = new Node(key, value, null, null);
            size += 1;
            keys.add(key);
            return;
        }
        putHelper(key, value, root);
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

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    private V removeHelper(K key, Node p) {
        if (p == null) {
            return null;
        }
        if (key.compareTo(p.key) == 0) {
            V v = p.value;
            Node leftMax = p.left;
            if (leftMax == null) {
                p.copy(p.right);
                size -= 1;
                keys.remove(key);
                return v;
            }
            if (leftMax.right == null) {
                leftMax.right = p.right;
                p.copy(leftMax);
                size -= 1;
                keys.remove(key);
                return v;
            }
            while (leftMax != null && leftMax.right != null
                    && leftMax.right.right != null) {
                leftMax = leftMax.right;
            }
            p.key = leftMax.right.key;
            p.value = leftMax.right.value;
            leftMax.right = leftMax.right.left;
            size -= 1;
            keys.remove(key);
            return v;
        } else if (key.compareTo(p.key) < 0) {
            return removeHelper(key, p.left);
        } else {
            return removeHelper(key, p.right);
        }
    }
    @Override
    public V remove(K key) {
//        throw new UnsupportedOperationException();
        return removeHelper(key, root);
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
//        throw new UnsupportedOperationException();
        if (get(key) != null && get(key).equals(value)) {
            return remove(key);
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
//        throw new UnsupportedOperationException();
        return keySet().iterator();
    }
}
