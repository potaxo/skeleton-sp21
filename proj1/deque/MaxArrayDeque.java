package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private Comparator<T> comp;
    public MaxArrayDeque(Comparator<T> c) {
       super();
       comp = c;
    }

    public T max() {
        if (isEmpty())
            return null;
        int maxindex = 0;
        for (int i = 0; i < size(); i++) {
            int compResult = comp.compare(get(maxindex), get(i));
            if (compResult < 0) {
                maxindex = i;
            }
        }
        return get(maxindex);
    }

    public T max(Comparator<T> c) {
        comp = c;
        return max();
    }
}
