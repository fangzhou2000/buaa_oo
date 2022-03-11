import com.oocourse.spec3.exceptions.EqualMessageIdException;

import java.util.HashMap;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private int id;
    private static int count = 0;
    private static HashMap<Integer, Integer> idCount = new HashMap<>();

    public MyEqualMessageIdException(int id) {
        this.id = id;
    }

    @Override
    public void print() {
        count++;
        if (idCount.containsKey(this.id)) {
            int old = idCount.get(this.id);
            idCount.put(this.id, old + 1);
        } else {
            idCount.put(this.id, 1);
        }
        System.out.println("emi-" + count + ", " + id + "-" + idCount.get(id));
    }
}
