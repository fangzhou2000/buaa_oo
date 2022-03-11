import java.math.BigInteger;

public class Const extends Factor {
    //String op;
    private BigInteger coef;

    public Const(BigInteger coef) {
        //this.op = op;
        this.coef = coef;
    }

    public Factor clone() {
        return new Const(new BigInteger(coef.toString()));
    }

    public String toString() {
        return this.coef.toString();
    }

    public String deri() {
        return BigInteger.ZERO.toString();
    }
}
