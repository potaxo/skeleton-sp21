package flik;

public class HorribleSteve2 {
    public static void main(String [] args) {
        Integer r = 0;
        Integer i = 0;
        Integer k = 0;
        while(r < 500) {
            boolean same = Flik.isSameNumber(i, k);
            if (!same) {
                System.out.println(i);
            }
            r++;
            i++; k++;
        }

    }
}
