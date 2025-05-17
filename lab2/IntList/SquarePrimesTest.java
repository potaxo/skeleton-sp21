package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesSimpleone() {
        IntList lst = IntList.of(2, 4, 3, 6, 6, 6, 7, 7, 7, 0, 64, 1);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 4 -> 9 -> 6 -> 6 -> 6 -> 49 -> 49 -> 49 -> 0 -> 64 -> 1", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesSimpletwo() {
        IntList lst = IntList.of(4, 6, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("4 -> 6 -> 18", lst.toString());
        assertFalse(changed);
    }

    @Test
    public void testSquarePrimesSimplethree() {
        IntList lst = IntList.of(1);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("1", lst.toString());
        assertFalse(changed);
    }
}
