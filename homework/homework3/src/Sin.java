import java.math.BigInteger;

public class Sin extends Factor {
    private String sinFactor;
    private String sinOrigin;
    private String sinChange;
    private BigInteger index;

    public Sin(String sinFactor, BigInteger index) {
        this.sinFactor = sinFactor;
        this.sinOrigin = sinFactor.replaceAll("\\^", "**");
        this.sinChange = ManageInput.manageFactor(sinFactor);
        this.index = index;
    }

    public Factor clone() {
        return new Sin(sinFactor, new BigInteger(index.toString()));
    }

    public String toString() {
        if (index.equals(BigInteger.ZERO)) {
            return BigInteger.ONE.toString();
        } else if (index.equals(BigInteger.ONE)) {
            return "sin(" + sinOrigin + ")";
        } else {
            return "sin(" + sinOrigin + ")**" + index;
        }
    }

    public String deri() {
        Node node = new Node(FactorFactory.generateFactor(sinChange));
        String result = Deri.deri(node);
        if (index.equals(BigInteger.ZERO)) {
            return "0";
        } else if (index.equals(BigInteger.ONE)) {
            if (result.equals("0")) {
                return "0";
            } else if (result.equals("1")) {
                return "cos(" + sinOrigin + ")";
            } else {
                return "cos(" + sinOrigin + ")*" + result;
            }
        } else if (index.equals(BigInteger.valueOf(2))) {
            if (result.equals("0")) {
                return "0";
            } else if (result.equals("1")) {
                return index + "*sin(" + sinOrigin + ")*cos(" + sinOrigin + ")";
            } else {
                return index +
                        "*sin(" + sinOrigin + ")*cos(" + sinOrigin + ")*" + result;
            }
        } else {
            if (result.equals("0")) {
                return "0";
            } else if (result.equals("1")) {
                return index + "*sin(" + sinOrigin + ")**" +
                        index.subtract(BigInteger.ONE) + "*cos(" + sinOrigin + ")";
            } else {
                return index + "*sin(" + sinOrigin + ")**" +
                        index.subtract(BigInteger.ONE) + "*cos(" + sinOrigin + ")*" + result;
            }
        }
    }
}
