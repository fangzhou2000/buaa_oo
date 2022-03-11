import com.oocourse.spec2.exceptions.EqualRelationException;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private int id1;
    private int id2;
    private static int count = 0;
    private static HashMap<Integer, Integer> idCount = new HashMap<>();

    public MyEqualRelationException(int id1, int id2) {
        if (id1 < id2) {
            this.id1 = id1;
            this.id2 = id2;
        } else {
            this.id1 = id2;
            this.id2 = id1;
        }
    }

    @Override
    public void print() {
        count++;
        if (idCount.containsKey(this.id1)) {
            int old = idCount.get(this.id1);
            idCount.put(this.id1, old + 1);
        } else {
            idCount.put(this.id1, 1);
        }
        if (id1 == id2) {
            System.out.println("er-" + count + ", " + id1 + "-" + idCount.get(id1) +
                    ", " + id2 + "-" + idCount.get(id1));
        } else {
            if (idCount.containsKey(this.id2)) {
                int old = idCount.get(this.id2);
                idCount.put(this.id2, old + 1);
            } else {
                idCount.put(this.id2, 1);
            }
            System.out.println("er-" + count + ", " + id1 + "-" + idCount.get(id1) +
                    ", " + id2 + "-" + idCount.get(id2));
        }
    }
}
