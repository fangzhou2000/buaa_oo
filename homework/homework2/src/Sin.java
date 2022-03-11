import java.math.BigInteger;

public class Sin extends Factor {
    private BigInteger index;

    public Sin(BigInteger index) {
        this.index = index;
    }

    public Factor clone() {
        return new Sin(new BigInteger(index.toString()));
    }

    public String toString() {
        if (index.equals(BigInteger.ZERO)) {
            return BigInteger.ONE.toString();
        } else if (index.equals(BigInteger.ONE)) {
            return "sin(x)";
        } else {
            return "sin(x)**" + index;
        }
    }

    public String deri() {
        if (index.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO.toString();
        } else if (index.equals(BigInteger.ONE)) {
            return "cos(x)";
        } else if (index.equals(BigInteger.valueOf(2))) {
            return index + "*sin(x)*cos(x)";
        } else {
            return index + "*sin(x)**" + index.subtract(BigInteger.ONE) + "*cos(x)";
        }
    }
}
