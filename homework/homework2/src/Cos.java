import java.math.BigInteger;

public class Cos extends Factor {
    //private String op;
    private BigInteger index;

    public Cos(BigInteger index) {
        //this.op = op;
        this.index = index;
    }

    public Factor clone() {
        return new Cos(new BigInteger(index.toString()));
    }

    public String toString() {
        if (index.equals(BigInteger.ZERO)) {
            return BigInteger.ONE.toString();
        } else if (index.equals(BigInteger.ONE)) {
            return "cos(x)";
        } else {
            return "cos(x)**" + index;
        }
    }

    public String deri() {
        if (index.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO.toString();
        } else if (index.equals(BigInteger.ONE)) {
            return "-1*sin(x)";
        } else if (index.equals(BigInteger.valueOf(2))) {
            return index.negate() + "*cos(x)*sin(x)";
        } else {
            return index.negate() + "*cos(x)**" + index.subtract(BigInteger.ONE) + "*sin(x)";
        }
    }
}
