import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;

import java.util.HashMap;

public class MyEmojiIdNotFoundException extends EmojiIdNotFoundException {
    private int id;
    private static int count = 0;
    private static HashMap<Integer, Integer> idCount = new HashMap<>();

    public MyEmojiIdNotFoundException(int id) {
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
        System.out.println("einf-" + count + ", " + id + "-" + idCount.get(id));
    }
}
