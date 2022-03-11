public class Novel extends Art {
    private boolean finish;

    public Novel(String name, double price, long num, long age, boolean finish) {
        super(name, price, num, age);
        this.finish = finish;
    }

    public boolean getFinish() {
        return finish;
    }

    public String toString() {
        return super.getName() + " " + super.getPrice() + " "
                + super.getNum() + " " + super.getAge() + " " + this.getFinish();
    }
}
