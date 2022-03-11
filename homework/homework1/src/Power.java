import java.math.BigInteger;

public class Power {
    private String op;
    private BigInteger index;
    private BigInteger coef;

    public Power(String op, BigInteger coef, BigInteger index) {
        this.op = op;
        this.coef = coef;
        this.index = index;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public BigInteger getCoef() {
        return coef;
    }

    public void setCoef(BigInteger coef) {
        this.coef = coef;
    }

    public BigInteger getIndex() {
        return index;
    }

    public void merge(Power power) {
        coef = coef.add(power.getCoef());
    }

    public void derivative() {
        if (index.equals(BigInteger.ZERO)) {
            coef = BigInteger.ZERO;
        } else {
            coef = coef.multiply(index);
            index = index.subtract(new BigInteger("1"));
        }
    }

    public String getCoefop() {
        if (coef.compareTo(BigInteger.ZERO) < 0) {
            return "-";
        } else {
            return "+";
        }
    }
}
