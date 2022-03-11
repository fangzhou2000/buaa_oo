import java.util.HashMap;

public class Poly {
    private HashMap<Item, Item> poly;

    public Poly() {
        this.poly = new HashMap<>();
    }

    public HashMap<Item, Item> getPoly() {
        return poly;
    }

    public String toString() {
        String result = "";
        for (Item item : poly.keySet()) {
            result = result + item.toString();
        }
        if (result.equals("")) {
            return "0";
        } else if (result.charAt(0) == '+') {
            return result.substring(1);
        } else {
            return result;
        }
    }
}
