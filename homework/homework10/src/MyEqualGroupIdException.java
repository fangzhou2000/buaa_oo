import com.oocourse.spec2.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private int id;
    private static int count = 0;
    private static HashMap<Integer, Integer> idCount = new HashMap<>();

    public MyEqualGroupIdException(int id) {
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
        System.out.println("egi-" + count + ", " + id + "-" + idCount.get(id));
    }
}
