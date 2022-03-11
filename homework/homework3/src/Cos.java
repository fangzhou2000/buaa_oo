import java.math.BigInteger;

public class Cos extends Factor {
    private String cosFactor;
    private String cosOrigin;
    private String cosChange;
    private BigInteger index;

    //输入的是指数换了但是括号没换的
    public Cos(String cosFactor, BigInteger index) {
        this.cosFactor = cosFactor;
        this.cosOrigin = cosFactor.replaceAll("\\^", "**");
        this.cosChange = ManageInput.manageFactor(cosFactor);
        this.index = index;
    }

    public Factor clone() {
        return new Cos(cosFactor, new BigInteger(index.toString()));
    }

    public String toString() {
        if (index.equals(BigInteger.ZERO)) {
            return BigInteger.ONE.toString();
        } else if (index.equals(BigInteger.ONE)) {
            return "cos(" + cosOrigin + ")";
        } else {
            return "cos(" + cosOrigin + ")**" + index;
        }
    }

    public String deri() {
        //待优化，去0，去1
        Node node = new Node(FactorFactory.generateFactor(cosChange));
        String result = Deri.deri(node);
        if (index.equals(BigInteger.ZERO)) {
            return "0";
        } else if (index.equals(BigInteger.ONE)) {
            if (result.equals("0")) {
                return "0";
            } else if (result.equals("1")) {
                return "-1*sin(" + cosOrigin + ")";
            } else {
                return "-1*sin(" + cosOrigin + ")*" + result;
            }
        } else if (index.equals(BigInteger.valueOf(2))) {
            if (result.equals("0")) {
                return "0";
            } else if (result.equals("1")) {
                return index.negate() +
                        "*cos(" + cosOrigin + ")*sin(" + cosOrigin + ")";
            } else {
                return index.negate() +
                        "*cos(" + cosOrigin + ")*sin(" + cosOrigin + ")*" + result;
            }
        } else {
            if (result.equals("0")) {
                return "0";
            } else if (result.equals("1")) {
                return index.negate() + "*cos(" + cosOrigin + ")**" +
                        index.subtract(BigInteger.ONE) + "*sin(" + cosOrigin + ")";
            } else {
                return index.negate() + "*cos(" + cosOrigin + ")**" +
                        index.subtract(BigInteger.ONE) + "*sin(" + cosOrigin + ")*" + result;
            }
        }
    }
}
