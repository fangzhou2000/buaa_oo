public class Math extends Science {
    private long grade;

    public Math(String name, double price, long num, long year, long grade) {
        super(name, price, num, year);
        this.grade = grade;
    }

    public long getGrade() {
        return grade;
    }

    public String toString() {
        return super.getName() + " " + super.getPrice() + " "
                + super.getNum() + " " + super.getYear() + " " + this.getGrade();
    }
}
