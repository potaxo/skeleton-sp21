package deque;

public class ArrayDeque<T> implements Deque<T> {
    private T[] items;
    private int nf; // next first
    private int nl; // next last
    private int size;
    private int arraySize;

    public ArrayDeque() {
        items = (T[]) new Object[8];
        arraySize = 8;
        size = 0;
        nf = arraySize - 1;
        nl = 0;
    }


    /* public void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        if (nl == 0 && nf ==  arraySize - 1) {
            System.arraycopy(items, 0, a, 0, size);
        } else {
            System.arraycopy(items, nf + 1, a, 0, size - (nf + 1));
            System.arraycopy(items, 0, a, size - (nf + 1), nl);
        }
        items = a;
        arraySize = capacity;
        nf = arraySize - 1;
        nl = size;
    } */

    public void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int f = updateBoundary(nf + 1);
        int l = updateBoundary(nl - 1);
        if (f < l) {
            System.arraycopy(items, f, a, 0, l - f + 1);
        } else {
            System.arraycopy(items, f, a, 0, size - f);
            System.arraycopy(items, 0, a, size - f, updateBoundary(l + 1));
        }
        items = a;
        arraySize = capacity;
        nf = arraySize - 1;
        nl = size;
    }

    public void isOutOfBoundary() {
        if (items[nf] != null) {
            resize(size * 2);
        }
    }

    public int updateBoundary(int n) {
        if (n < 0) {
            return arraySize - 1;
        } else if (n > arraySize - 1) {
            return 0;
        } else {
            return n;
        }

    }

    @Override
    public void addFirst(T item) {
        isOutOfBoundary();
        items[nf] = item;
        nf -= 1;
        nf = updateBoundary(nf);
        size++;
    }

    @Override
    public void addLast(T item) {
        isOutOfBoundary();
        items[nl] = item;
        nl += 1;
        nl = updateBoundary(nl);
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
        int f = updateBoundary(nf + 1);
        int l = updateBoundary(nl - 1);
        for (int i = f; i < size; i++) {
            System.out.print(items[i] + " ");
        }
        for (int i = 0; i < l + 1 && items[i] != null; i++) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }


    @Override
    public T removeFirst() {
        int f = updateBoundary(nf + 1);
        int l = updateBoundary(nl - 1);
        if (size == 0) {
            return null;
        }
        T tem = items[f];
        items[f] = null;
        nf = updateBoundary(nf + 1);
        size--;
        checkResize();
        return tem;
    }

    public void checkResize() {
        if (size < arraySize / 4 && arraySize > 8) {
            resize(arraySize / 2);
        }
    }

    @Override
    public T removeLast() {
        int f = updateBoundary(nf + 1);
        int l = updateBoundary(nl - 1);
        if (size == 0) {
            return null;
        }
        T tem = items[l];
        items[l] = null;
        nl = updateBoundary(nl - 1);
        size--;
        checkResize();
        return tem;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int f = updateBoundary(nf + 1);
        return items[updateBoundary(f + index)];
    }

}