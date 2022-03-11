public class Art extends Bookset {
    private long age;

    public Art(String name, double price, long num, long age) {
        super(name, price, num);
        this.age = age;
    }

    public long getAge() {
        return age;
    }
}
