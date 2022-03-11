import java.math.BigInteger;
import java.util.HashMap;

public class Bookstack {
    private HashMap<String, Bookset> booksets;
    private int[] kind;

    public Bookstack() {
        this.booksets = new HashMap<>();
        this.kind = new int[7];
    }

    public Bookset query(String name) throws Exception1, Exception2 {
        if (booksets.isEmpty()) {
            throw new Exception2();
        } else if (!booksets.containsKey(name)) {
            throw new Exception1();
        }
        return booksets.get(name);
    }

    public BigInteger totalkind() throws Exception2 {
        BigInteger totalkind = BigInteger.ZERO;
        if (booksets.isEmpty()) {
            throw new Exception2();
        } else {
            for (int i = 0; i < kind.length; i++) {
                if (kind[i] != 0) {
                    totalkind = totalkind.add(BigInteger.valueOf(1));
                }
            }
        }
        return totalkind;
    }

    public BigInteger totalnum() throws Exception2 {
        BigInteger totalnum = BigInteger.ZERO;
        if (booksets.isEmpty()) {
            throw new Exception2();
        } else {
            for (String name : booksets.keySet()) {
                totalnum = totalnum.add(BigInteger.valueOf(booksets.get(name).getNum()));
            }
        }
        return totalnum;
    }

    public void add(Bookset bookset) throws Exception3 {
        if (this.booksets.containsKey(bookset.getName())) {
            throw new Exception3();
        } else {
            if (bookset.getClass().getName().equals("Other")) {
                kind[0]++;
            } else if (bookset.getClass().getName().equals("OtherA")) {
                kind[1]++;
            } else if (bookset.getClass().getName().equals("Novel")) {
                kind[2]++;
            } else if (bookset.getClass().getName().equals("Poetry")) {
                kind[3]++;
            } else if (bookset.getClass().getName().equals("OtherS")) {
                kind[4]++;
            } else if (bookset.getClass().getName().equals("Math")) {
                kind[5]++;
            } else if (bookset.getClass().getName().equals("Computer")) {
                kind[6]++;
            } else {
                System.out.println("add wrong");
            }
            this.booksets.put(bookset.getName(), bookset);
        }
    }

    public BigInteger remove(String name) throws Exception4 {
        if (!booksets.containsKey(name)) {
            throw new Exception4();
        } else {
            Bookset bookset = booksets.get(name);
            if (bookset.getClass().getName().equals("Other")) {
                if (kind[0] > 0) {
                    kind[0]--;
                }
            } else if (bookset.getClass().getName().equals("OtherA")) {
                if (kind[1] > 0) {
                    kind[1]--;
                }
            } else if (bookset.getClass().getName().equals("Novel")) {
                if (kind[2] > 0) {
                    kind[2]--;
                }
            } else if (bookset.getClass().getName().equals("Poetry")) {
                if (kind[3] > 0) {
                    kind[3]--;
                }
            } else if (bookset.getClass().getName().equals("OtherS")) {
                if (kind[4] > 0) {
                    kind[4]--;
                }
            } else if (bookset.getClass().getName().equals("Math")) {
                if (kind[5] > 0) {
                    kind[5]--;
                }
            } else if (bookset.getClass().getName().equals("Computer")) {
                if (kind[6] > 0) {
                    kind[6]--;
                }
            } else {
                System.out.println("remove wrong");
            }
            booksets.remove(name);
        }
        BigInteger totalnum = BigInteger.ZERO;
        for (String key : booksets.keySet()) {
            totalnum = totalnum.add(BigInteger.valueOf(booksets.get(key).getNum()));
        }
        return totalnum;
    }

    public HashMap<String, Bookset> getBooksets() {
        return booksets;
    }

    public int[] getKind() {
        return kind;
    }

    public void setKind(int[] kind) {
        this.kind = kind.clone();
    }

    public void addKind(Bookset bookset) {
        if (bookset.getClass().getName().equals("Other")) {
            this.kind[0]++;
        } else if (bookset.getClass().getName().equals("OtherA")) {
            this.kind[1]++;
        } else if (bookset.getClass().getName().equals("Novel")) {
            this.kind[2]++;
        } else if (bookset.getClass().getName().equals("Poetry")) {
            this.kind[3]++;
        } else if (bookset.getClass().getName().equals("OtherS")) {
            this.kind[4]++;
        } else if (bookset.getClass().getName().equals("Math")) {
            this.kind[5]++;
        } else if (bookset.getClass().getName().equals("Computer")) {
            this.kind[6]++;
        } else {
            System.out.println("addKind wrong");
        }
    }
}
