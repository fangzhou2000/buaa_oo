public class Science extends Bookset {
    private long year;

    public Science(String name, double price, long num, long year) {
        super(name, price, num);
        this.year = year;
    }

    public long getYear() {
        return year;
    }
}
