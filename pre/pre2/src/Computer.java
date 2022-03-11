public class Computer extends Science {
    private String major;

    public Computer(String name, double price, long num, long year, String major) {
        super(name, price, num, year);
        this.major = major;
    }

    public String getMajor() {
        return major;
    }

    public String toString() {
        return super.getName() + " " + super.getPrice() + " "
                + super.getNum() + " " + super.getYear() + " " + this.getMajor();
    }
}
