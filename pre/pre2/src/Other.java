public class Other extends Bookset {
    public Other(String name, double price, long num) {
        super(name, price, num);
    }

    public String toString() {
        return super.getName() + " " + super.getPrice() + " " + super.getNum();
    }
}
