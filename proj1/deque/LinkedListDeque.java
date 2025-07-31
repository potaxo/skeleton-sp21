package deque;
import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private class Node {
        public T item;
        public Node next;
        public Node front;


        public Node(T i, Node ft, Node nt) {
            item = i;
            next = nt;
            front = ft;
        }
    }

    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel= new Node(null, null, null);
        sentinel.front = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    public LinkedListDeque(T x) {
        sentinel= new Node(x, null, null);
        sentinel.next = sentinel;
        sentinel.front = sentinel.next;
        size = 1;
    }

    @Override
    public void addFirst(T item) {
        Node newNode =  new Node(item, sentinel, sentinel.next);
        sentinel.next.front = newNode;
        sentinel.next = newNode;
        size++;
    }

    @Override
    public void addLast(T item) {
        Node newNode = new Node(item, sentinel.front, sentinel);
        sentinel.front.next = newNode;
        sentinel.front = newNode;
        size++;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {
        for (Node p = sentinel.next; p.item != null; p = p.next) {
            System.out.print(p.item + " ");
        }
    }

    @Override
    public T removeFirst() {
        if (sentinel.front == sentinel) {
            return null; // Deque is empty
        }
        Node deleted = sentinel.next;
        sentinel.next = sentinel.next.next;
        sentinel.next.front = sentinel;
        size--;
        return deleted.item;
    }

    @Override
    public T removeLast() {
        if (sentinel.front == sentinel) {
            return null; // Deque is empty
        }
        Node deleted = sentinel.front;
        sentinel.front = sentinel.front.front;
        sentinel.front.next = sentinel;
        size--;
        return deleted.item;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null; // Index out of bounds
        }
        return get(index, sentinel.next);
    }

    public T get(int index, Node p) {
        if (index == 0) {
            return p.item;
        } else {
            return get(index - 1, p.next);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }


    private class LinkedListDequeIterator implements Iterator<T> {
        private Node iter;

        public LinkedListDequeIterator() {
            iter = sentinel;
        }

        public boolean hasNext() {
            return iter.next != sentinel;
        }

        public T next() {
            T returnItem = iter.next.item;
            iter = iter.next;
            return returnItem;
        }
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> other = (Deque<T>) o;
        if (other.size() != other.size()) {
            return false;
        }

        int max = other.size();
        for (int x = 0; x < size; x++) {
            if (!(this.get(x).equals(other.get(x)))) {
                return false;
            }

        }
        return true;
    }


}