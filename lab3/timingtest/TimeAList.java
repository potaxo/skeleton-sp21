package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
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
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        //create a loop gives a array the N of the container
        AList<Integer> Ns = new AList<>();
        //create a loop gives a array the time of the container
        AList<Double> times = new AList<>();
        //create a loop gives a array the opCounts of the container
        AList<Integer> opCounts = new AList<>();

        for (int N = 1000; N <= 128000; N *= 2) {
            Ns.addLast(N);
            Stopwatch sw = new Stopwatch();
            AList<Integer> L = new AList<>();
            for (int i = 0; i < N; i++) {
                L.addLast(i);
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            opCounts.addLast(N);
        }
        printTimingTable(Ns, times, opCounts);
    }
}
