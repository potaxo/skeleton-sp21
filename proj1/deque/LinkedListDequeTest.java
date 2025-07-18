package deque;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {

        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());

    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {

        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }

    }
    
    @Test
    public void addFirstTest() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        lld.addFirst(1);
        assertEquals(1, lld.size());
        lld.addFirst(2);
        lld.addFirst(3);
        assertEquals(3, lld.size());

    }

    @Test
    public void addLastTest() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        lld.addLast(1);
        assertEquals(1, lld.size());
        lld.addLast(2);
        lld.addLast(3);
        assertEquals(3, lld.size());
    }

    @Test
    public void printDequeTest() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        lld.addFirst(1);
        lld.addLast(2);
        lld.addLast(3);
        // This will print: 1 2 3
        lld.printDeque();
    }

    @Test
    public void removeFirstTest() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        lld.addFirst(1);
        lld.addLast(2);
        lld.addLast(3);
        assertEquals(Integer.valueOf(1), lld.removeFirst());
        assertEquals(2, lld.size());
    }

    @Test
    public void removeLastTest() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        lld.addFirst(1);
        lld.addLast(2);
        lld.addLast(3);
        assertEquals(Integer.valueOf(3), lld.removeLast());
        assertEquals(2, lld.size());
    }

    @Test
    public void getTest() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        lld.addFirst(1);
        lld.addLast(2);
        lld.addLast(3);
        assertEquals(Integer.valueOf(1), lld.get(0));
        assertEquals(Integer.valueOf(2), lld.get(1));
        assertEquals(Integer.valueOf(3), lld.get(2));
        assertNull(lld.get(3)); // Out of bounds
    }
    @Test
    /**
     * Tests the iterator on a basic deque to ensure it returns elements in the correct order.
     */
    public void iteratorBasicTest() {
        LinkedListDeque<String> lld = new LinkedListDeque<>();
        lld.addLast("A");
        lld.addLast("B");
        lld.addLast("C");

        // The for-each loop implicitly uses the iterator.
        // We can check if it returns the items in the expected order.
        StringBuilder result = new StringBuilder();
        for (String s : lld) {
            result.append(s);
        }
        assertEquals("Iterator should traverse from first to last", "ABC", result.toString());
    }

    @Test
    /**
     * Completes the original test for iterating over a large deque.
     */
    public void bigIteratorTest() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld.addLast(i);
        }

        // We check if each element returned by the iterator matches its expected value.
        int expectedValue = 0;
        for (int actualValue : lld) {
            assertEquals("The iterated value should match the expected value",
                    Integer.valueOf(expectedValue), Integer.valueOf(actualValue));
            expectedValue++;
        }

        // Final check to ensure we iterated through all one million elements.
        assertEquals("The iterator should have traversed all 1,000,000 elements", 1000000, expectedValue);
    }

    @Test
    /**
     * Tests that the iterator works correctly for an empty deque.
     */
    public void iteratorEmptyTest() {
        LinkedListDeque<String> lld = new LinkedListDeque<>();
        int count = 0;
        // This for-each loop should not execute its body at all.
        for (String s : lld) {
            count++;
        }
        assertEquals("Iterator on an empty deque should not run", 0, count);
    }
    @Test
    /* Test the equals method with various scenarios */
    public void equalsTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ArrayDeque<Integer> ad2 = new ArrayDeque<>();
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        boolean b = ad1.equals(ad2);
        // 1. Two empty deques should be equal
        assertTrue("Two empty deques should be equal", ad1.equals(ad2));

        ad1.addLast(1);
        ad1.addLast(2);
        ad1.addLast(3);

        ad2.addLast(1);
        ad2.addLast(2);
        ad2.addLast(3);

        lld1.addLast(1);
        lld1.addLast(2);
        lld1.addLast(3);

        // 2. Two deques with the same content should be equal
        assertTrue("Two deques with the same integer content should be equal", ad1.equals(ad2));

        // 3. An ArrayDeque and a LinkedListDeque with the same content should be equal
        assertTrue("An ArrayDeque and LinkedListDeque with the same content should be equal", ad1.equals(lld1));

        // 4. Comparing a deque to itself should be true
        assertTrue("Comparing a deque to itself should be true", ad1.equals(ad1));

        // 5. Comparing to null should be false
        assertFalse("Comparing to null should be false", ad1.equals(null));

        // 6. Comparing to a different type of object should be false
        assertFalse("Comparing to a non-deque object should be false", ad1.equals("a string"));

        // 7. Deques with different content should not be equal
        ad2.removeLast();
        ad2.addLast(4); // ad2 is now [1, 2, 4]
        assertFalse("Deques with different content should not be equal", ad1.equals(ad2));

        // 8. Deques with different sizes should not be equal
        ad2.removeLast(); // ad2 is now [1, 2]
        assertFalse("Deques with different sizes should not be equal", ad1.equals(ad2));
    }
}
