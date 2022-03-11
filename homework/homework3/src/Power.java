import java.math.BigInteger;

public class Power extends Factor {
    private BigInteger index;

    public Power(BigInteger index) {
        this.index = index;
    }

    public Factor clone() {
        return new Power(new BigInteger(index.toString()));
    }

    public String toString() {
        if (index.equals(BigInteger.ZERO)) {
            return BigInteger.ONE.toString();
        } else if (index.equals(BigInteger.ONE)) {
            return "x";
        } else if (index.equals(BigInteger.valueOf(2))) {
            return "x*x";
        } else {
            return "x**" + index;
        }
    }

    public String deri() {
        if (index.equals(BigInteger.ZERO)) {
            return BigInteger.ZERO.toString();
        } else if (index.equals(BigInteger.ONE)) {
            return BigInteger.ONE.toString();
        } else if (index.equals(BigInteger.valueOf(2))) {
            return "2*x";
        } else if (index.equals(BigInteger.valueOf(3))) {
            return "3*x*x";
        } else {
            return index + "*x**" + index.subtract(BigInteger.ONE);
        }
    }

    public BigInteger getIndex() {
        return index;
    }
}
