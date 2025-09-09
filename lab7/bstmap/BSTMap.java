package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private Node root; // We'll get to this next!
    private int size;

    // Private inner class for the nodes
    private class Node {
        private K key;
        private V value;
        private Node left;
        private Node right;

        // A constructor for the Node is a great idea!
        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }


    @Override
    public void clear() {
        root.key = null;
        root.value = null;
        root.left = null;
        root.right = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return containsKeyHelp(root, key);
    }

    private boolean containsKeyHelp(Node n, K key) {
        if (n == null) {
            return false;
        }

        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            return containsKeyHelp(n.left, key);
        } else if (cmp > 0) {
            return containsKeyHelp(n.right, key);
        } else {
            return true;
        }
    }


    public V get(K key) {
        if (containsKey(key)) {
            return getHelp(root, key);
        } else {
            return null;
        }
    }

    private V getHelp(Node n, K key) {
        if (n == null) {
            return null;
        }

        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            return getHelp(n.left, key);
        } else if (cmp > 0) {
            return getHelp(n.right, key);
        } else {
            return n.value;
        }
    }
    /* returns the number of key-value mappings in this map. */
    public int size() {
        return size;
    }

    /* associates the specified value with the specified key in this map. */
    public void put(K key, V value) {
        root = putHelp(root, key, value);
    }

    private Node putHelp(Node n, K key, V value) {
        // Base Case: If we've found an empty spot, create the new node.
        if (n == null) {
            size++;
            return new Node(key, value);
        }

        int cmp = key.compareTo(n.key);
        if (cmp < 0) {
            n.left = putHelp(n.left, key, value);
        } else if (cmp > 0) {
            n.right = putHelp(n.right, key, value);
        } else {
            // The key already exists, so we just update the value.
            n.value = value;
        }
        // Return the current node after potential modifications to its children.
        return n;
    }

    /* returns a set view of the keys contained in this map. not required for lab 7.
     * if you don't implement this, throw an unsupportedoperationexception. */
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }


    /* removes the mapping for the specified key from this map if present.
     * not required for lab 7. if you don't implement this, throw an
     * unsupportedoperationexception. */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /* removes the entry for the specified key only if it is currently mapped to
     * the specified value. not required for lab 7. if you don't implement this,
     * throw an unsupportedoperationexception.*/
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return new BSTMapIter();
    }

    private class BSTMapIter implements Iterator<K> {
        private Node cur;

        @Override
        public  boolean hasNext() {
            throw new UnsupportedOperationException();
        }

        public BSTMapIter() {
            throw new UnsupportedOperationException();
        }

        public K next() {
            throw new UnsupportedOperationException();
        }
    }
}
