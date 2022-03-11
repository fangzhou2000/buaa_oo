public class OtherA extends Art {

    public OtherA(String name, double price, long num, long age) {
        super(name, price, num, age);
    }

    public long getAge() {
        return super.getAge();
    }

    public String toString() {
        return super.getName() + " " + super.getPrice() + " "
                + super.getNum() + " " + this.getAge();
    }
}
