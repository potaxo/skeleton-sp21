package deque;

import java.util.Iterator;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

   @Test
   public void addFirstTest() {
       ArrayDeque<Integer> ad = new ArrayDeque<>();
       for (int i = 0; i < 9999; i++) {
           ad.addFirst(i);
       }
   } 

    @Test
    public void randomAddFirstTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < 10000; i++) {
            if (Math.random() < 0.5) {
                ad.addFirst(i);
            } else {
                ad.addLast(i);
            }
        }
    }

    @Test
    public void printDequeTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < 100; i++) {
            if (Math.random() < 0.5) {
                ad.addFirst(i);
            } else {
                ad.addLast(i);
            }
        }
        ad.printDeque();
    }

    @Test
    public void printEmptyDequeTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.printDeque(); // Should print nothing
    }

    @Test
    public void printTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addFirst(1);
        ad.addFirst(2);
        ad.addLast(3);
        ad.addLast(4);
        ad.addFirst(5);
        ad.addLast(6);
        ad.addFirst(7);
        ad.addLast(8);
        ad.printDeque(); // Should print: 7 5 2 1 3 4 6 8
    }

    @Test
    public void removeFirstTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < 100; i++) {
            ad.addFirst(i);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(99 - i, (int) ad.removeFirst());
        }
        assertTrue(ad.isEmpty());
    }

    @Test
    public void removeLastTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < 100; i++) {
            ad.addLast(i);
        }
        for (int i = 99; i >= 0; i--) {
            assertEquals(i, (int) ad.removeLast());
        }
        assertTrue(ad.isEmpty());
    }

    @Test
    public void getTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        for (int i = 0; i < 100; i++) {
            ad.addLast(i);
        }
        for (int i = 0; i < 100; i++) {
            assertEquals(i, (int) ad.get(i));
        }
        assertNull(ad.get(100)); // Out of bounds
        assertNull(ad.get(-1)); // Negative index
    }

    @Test
    /**
     * Tests the iterator on a basic deque to ensure it returns elements in the correct order.
     */
    public void iteratorBasicTest() {
        ArrayDeque<String> ad = new ArrayDeque<>();
        ad.addLast("A");
        ad.addLast("B");
        ad.addLast("C");

        // The for-each loop implicitly uses the iterator.
        // We can check if it returns the items in the expected order.
        StringBuilder result = new StringBuilder();
        for (String s : ad) {
            result.append(s);
        }
        assertEquals("Iterator should traverse from first to last", "ABC", result.toString());
    }

    @Test
    /**
     * Tests that the iterator works correctly for an empty deque.
     */
    public void iteratorEmptyTest() {
        ArrayDeque<String> ad = new ArrayDeque<>();
        int count = 0;
        // This for-each loop should not execute its body at all.
        for (String s : ad) {
            count++;
        }
        assertEquals("Iterator on an empty deque should not run", 0, count);
    }

    @Test
    /**
     * Tests that the iterator works correctly even when the array has wrapped around.
     * This is a crucial test for a circular array deque.
     */
    public void iteratorWrapAroundTest() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        // Fill up the initial array
        for (int i = 0; i < 8; i++) {
            ad.addLast(i);
        } // Deque: 0 1 2 3 4 5 6 7

        // Remove from front, add to back to cause wrap-around
        ad.removeFirst(); // 1 2 3 4 5 6 7
        ad.removeFirst(); // 2 3 4 5 6 7
        ad.addLast(8);    // 2 3 4 5 6 7 8
        ad.addLast(9);    // 2 3 4 5 6 7 8 9 (now it has wrapped around)

        // Expected order: 2, 3, 4, 5, 6, 7, 8, 9
        Integer expected = 2;
        for (Integer actual : ad) {
            assertEquals("Iterator should handle wrap-around correctly", expected, actual);
            expected++;
        }
    }

}
