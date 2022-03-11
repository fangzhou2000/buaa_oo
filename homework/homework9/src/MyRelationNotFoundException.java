import com.oocourse.spec1.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private int id1;
    private int id2;
    private static int count = 0;
    private static HashMap<Integer, Integer> idCount = new HashMap<>();

    public MyRelationNotFoundException(int id1, int id2) {
        if (id1 < id2) {
            this.id1 = id1;
            this.id2 = id2;
        } else {
            this.id1 = id2;
            this.id2 = id1;
        }
    }

    public void print() {
        count++;
        if (idCount.containsKey(this.id1)) {
            int old = idCount.get(this.id1);
            idCount.put(this.id1, old + 1);
        } else {
            idCount.put(this.id1, 1);
        }
        if (idCount.containsKey(this.id2)) {
            int old = idCount.get(this.id2);
            idCount.put(this.id2, old + 1);
        } else {
            idCount.put(this.id2, 1);
        }
        System.out.println("rnf-" + count + ", " + id1 + "-" + idCount.get(id1) +
                ", " + id2 + "-" + idCount.get(id2));
    }
}
