import com.oocourse.spec1.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private int id;
    private static int count = 0;
    private static HashMap<Integer, Integer> idCount = new HashMap<>();

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
    }

    public void print() {
        count++;
        if (idCount.containsKey(this.id)) {
            int old = idCount.get(this.id);
            idCount.put(this.id, old + 1);
        } else {
            idCount.put(this.id, 1);
        }
        System.out.println("pinf-" + count + ", " + id + "-" + idCount.get(id));
    }
}
