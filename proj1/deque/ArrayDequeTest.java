package deque;

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
}
