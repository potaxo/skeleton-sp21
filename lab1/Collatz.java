/** Class that prints the Collatz sequence starting from a given number.
 *  @author potaxo
 */
public class Collatz {

    /** Buggy implementation of nextNumber! 
     * If n is even, the next number is n/2. If n is odd, 
     * the next number is 3n + 1. If n is 1, the sequence is over.
    */
    public static int nextNumber(int n) {
        if(n%2 == 0){
            n = n/2;
            return n;
        }
        else{
            n = 3*n+1;
            return n;
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.print(n + " ");
        while (n != 1) {
            n = nextNumber(n);
            System.out.print(n + " ");
        }
        System.out.println();
    }
}

