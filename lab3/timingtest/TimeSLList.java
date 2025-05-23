package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // create a whole array to store the data
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();
        // create a loop double the operation
        for (int N = 1000; N <= 128000; N *= 2) {
            Ns.addLast(N);
            // create a new SLList
            SLList<Integer> L = new SLList<>();
            // Initialize the list
            for (int i = 0; i < N; i++) {
                L.addLast(i);
            }
            // start the timer
            Stopwatch sw = new Stopwatch();
            // operate get last
            final int M = 10000;
            opCounts.addLast(M);
            for (int i = 0; i < M; i++) {
                L.getLast();
            }
            // end the timer
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
        }
        // print
        printTimingTable(Ns, times, opCounts);
    }

}
