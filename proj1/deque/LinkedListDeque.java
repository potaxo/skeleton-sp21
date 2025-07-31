package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
    private class Node {
        public T item;
        public Node next;
        public Node prev;

        public Node(T i, Node p, Node n) {
            item = i;
            prev = p;
            next = n;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size++;
    }

    @Override
    public void addLast(T item) {
        Node newNode = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size++;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (Node p = sentinel.next; p != sentinel; p = p.next) {
            System.out.print(p.item + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node first = sentinel.next;
        sentinel.next = first.next;
        first.next.prev = sentinel;
        size--;
        return first.item;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node last = sentinel.prev;
        sentinel.prev = last.prev;
        last.prev.next = sentinel;
        size--;
        return last.item;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.item;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node current = sentinel.next;

            @Override
            public boolean hasNext() {
                return current != sentinel;
            }

            @Override
            public T next() {
                T item = current.item;
                current = current.next;
                return item;
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Deque<?>)) return false;
        Deque<?> other = (Deque<?>) o;
        if (other.size() != this.size) return false;

        for (int i = 0; i < size; i++) {
            T a = this.get(i);
            Object b = other.get(i);
            if (a == null ? b != null : !a.equals(b)) {
                return false;
            }
        }
        return true;
    }
}
