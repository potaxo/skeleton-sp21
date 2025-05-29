package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
    // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        // initialize these two lists
        AListNoResizing<Integer> controlAlist = new AListNoResizing<>();
        BuggyAList<Integer> experimentalAlist = new BuggyAList<>();

        // add 4.5.6 to both
        controlAlist.addLast(4);
        controlAlist.addLast(5);
        controlAlist.addLast(6);

        experimentalAlist.addLast(4);
        experimentalAlist.addLast(5);
        experimentalAlist.addLast(6);
        // remove each one and print state
        assertEquals(controlAlist.size(), experimentalAlist.size());
        assertEquals(controlAlist.removeLast(), experimentalAlist.removeLast());
        assertEquals(controlAlist.removeLast(), experimentalAlist.removeLast());
        assertEquals(controlAlist.removeLast(), experimentalAlist.removeLast());
    }
    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> BL = new BuggyAList<>();
        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                BL.addLast(randVal);

            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int bsize = BL.size();
                assertEquals(size, bsize);
            } else if (operationNumber == 2) {
                // getLast
                if (L.size() == 0)
                    continue;
                assertEquals(L.getLast(), BL.getLast());
            } else {
                // removeLast
                if (L.size() == 0)
                    continue;
                int x = L.removeLast();
                int y = BL.removeLast();
                assertEquals(x, y);
            }
        }
    }
}
