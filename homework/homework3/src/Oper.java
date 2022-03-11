public class Oper extends Factor {
    private String op;

    public Oper(String op) {
        this.op = op;
    }

    public Factor clone() {
        return new Oper(op);
    }

    public String toString() {
        return op;
    }
}
