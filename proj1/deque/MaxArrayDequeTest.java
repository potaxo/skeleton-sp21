package deque;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;

/**
 * Tests for the MaxArrayDeque class.
 *
 * @author Your Name
 */
public class MaxArrayDequeTest {

    /**
     * A simple comparator for integers that compares them in their natural order.
     */
    private static class IntegerComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer a, Integer b) {
            return a - b;
        }
    }

    /**
     * A comparator for strings that compares them by their length.
     */
    private static class StringLengthComparator implements Comparator<String> {
        @Override
        public int compare(String a, String b) {
            return a.length() - b.length();
        }
    }

    @Test
    public void testMaxWithDefaultComparator() {
        // Create a MaxArrayDeque with a standard integer comparator.
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new IntegerComparator());

        // Add some integers to the deque.
        mad.addLast(5);
        mad.addLast(10);
        mad.addFirst(1);
        mad.addLast(20); // Deque state: [1, 5, 10, 20]
        int i = mad.max();
        // The maximum value should be 20.
        assertEquals("Should return the largest integer.", 20, (long) mad.max());
    }

    @Test
    public void testMaxOnEmptyDeque() {
        // Create a MaxArrayDeque with a standard integer comparator.
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new IntegerComparator());

        // Calling max() on an empty deque should return null.
        assertNull("Max on an empty deque should be null.", mad.max());
    }

    @Test
    public void testMaxWithCustomComparator() {
        // Create a MaxArrayDeque with a standard string comparator (natural order).
        MaxArrayDeque<String> mad = new MaxArrayDeque<>(String::compareTo);

        mad.addLast("apple");
        mad.addLast("banana");
        mad.addLast("kiwi"); // Deque state: ["apple", "banana", "kiwi"]

        // Create a new comparator that compares strings by their length.
        StringLengthComparator lengthComparator = new StringLengthComparator();

        // The longest string is "banana".
        assertEquals("Should return the longest string.", "banana", mad.max(lengthComparator));
    }

    @Test
    public void testMaxAfterComparatorChange() {
        // Create a MaxArrayDeque with a standard integer comparator.
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new IntegerComparator());
        mad.addLast(100);
        mad.addLast(50);
        mad.addLast(1);

        // Initially, the max should be 100.
        assertEquals("Initial max should be 100.", 100, (long) mad.max());

        // Create a "reverse" comparator that finds the minimum value.
        Comparator<Integer> reverseComparator = (a, b) -> b - a;

        // Find the "max" using the reverse comparator, which should be the minimum value.
        assertEquals("Max with reverse comparator should be the minimum value (1).", 1, (long) mad.max(reverseComparator));

        // According to your implementation, calling max(c) also updates the default comparator.
        // Let's verify that the default comparator has been changed.
        assertEquals("Default comparator should now be the reverse comparator, so max is 1.", 1, (long) mad.max());
    }

    @Test
    public void testMaxWithAllEqualElements() {
        MaxArrayDeque<Integer> mad = new MaxArrayDeque<>(new IntegerComparator());
        mad.addLast(7);
        mad.addLast(7);
        mad.addLast(7);

        // The max should be 7.
        assertEquals("Max of equal elements should be the element itself.", 7, (long) mad.max());
    }
}
