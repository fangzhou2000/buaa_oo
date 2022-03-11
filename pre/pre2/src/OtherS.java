public class OtherS extends Science {
    private long year;

    public OtherS(String name, double price, long num, long year) {
        super(name, price, num, year);
    }

    public String toString() {
        return super.getName() + " " + super.getPrice() + " "
                + super.getNum() + " " + this.getYear();
    }
}
