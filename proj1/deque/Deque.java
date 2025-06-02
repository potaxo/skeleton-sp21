package deque;

public interface Deque<T> {
    /**
     * Adds an item to the front of the deque.
     *
     * @param item the item to add
     */
    public void addFirst(T item);

    /**
     * Adds an item to the back of the deque.
     *
     * @param item the item to add
     */
    public void addLast(T item);

    /**
     * Returns true if the deque is empty, false otherwise.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty();

    /**
     * Returns the number of items in the deque.
     *
     * @return the size of the deque
     */
    public int size();

    /**
     * prints the items in the deque from first to last, separated by a space.
     * If the deque is empty, prints nothing.
     */
    public void printDeque();

    /**
     * Removes and returns the first item from the deque.
     *
     * @return the first item
     * @return null if the deque is empty
    public T removeFirst();

    /**
     * Removes and returns the last item from the deque.
     *
     * @return the last item
     * @return null if the deque is empty
     */
    public T removeLast();

    /**
     * Gets the item at the given index, where 0 is the first item.
     * If the index is out of bounds, returns null.
     *
     * @param index the index of the item to get
     * @return the item at the specified index, or null if out of bounds
     */
    public T get(int index);
}