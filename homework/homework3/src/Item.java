import java.math.BigInteger;

public class Item {
    //常数
    private BigInteger coef;
    //幂函数
    private BigInteger power;
    //其他
    private String other;

    public Item() {
        this.coef = BigInteger.ONE;
        this.power = BigInteger.ZERO;
        this.other = "";
    }

    public String toString() {
        String otherString = other;
        String powerString = "";
        if (power.equals(BigInteger.ZERO)) {
            powerString = "";
        } else if (power.equals(BigInteger.ONE)) {
            powerString = "x";
        } else if (power.equals(BigInteger.valueOf(2))) {
            powerString = "x*x";
        } else {
            powerString = "x**" + power.toString();
        }
        String result = "";
        String op = "";
        if (this.coef.compareTo(BigInteger.ZERO) > 0) {
            op = "+";
        } else if (this.coef.compareTo(BigInteger.ZERO) < 0) {
            op = "-";
        } else if (this.coef.equals(BigInteger.ZERO)) {
            return "";
        }
        if (this.coef.abs().equals(BigInteger.ONE)) {
            if (this.power.equals(BigInteger.ZERO) && this.other.equals("")) {
                result = op + "1";
            } else if (this.power.equals(BigInteger.ZERO) && !this.other.equals("")) {
                result = op + otherString;
            } else if (!this.power.equals(BigInteger.ZERO) && this.other.equals("")) {
                result = op + powerString;
            } else {
                result = op + powerString + "*" + otherString;
            }
        } else {
            if (this.power.equals(BigInteger.ZERO) && this.other.equals("")) {
                result = op + this.coef.abs().toString();
            } else if (this.power.equals(BigInteger.ZERO) && !this.other.equals("")) {
                result = op + this.coef.abs().toString() + "*" + otherString;
            } else if (!this.power.equals(BigInteger.ZERO) && this.other.equals("")) {
                result = op + this.coef.abs().toString() + "*" + powerString;
            } else {
                result = op + this.coef.abs().toString() + "*" + powerString + "*" + otherString;
            }
        }
        return result;
    }

    public BigInteger getCoef() {
        return coef;
    }

    public BigInteger getPower() {
        return power;
    }

    public String getOther() {
        return other;
    }

    public void addCoef(BigInteger coef) {
        this.coef = this.coef.add(coef);
    }

    public void mulCoef(BigInteger coef) {
        this.coef = this.coef.multiply(coef);
    }

    public void mulPower(BigInteger power) {
        this.power = this.power.add(power);
    }

    public void mulOther(String string) {
        if (this.other.equals("")) {
            this.other = this.other + string;
        } else {
            this.other = this.other + "*" + string;
        }

    }

    public int hashCode() {
        return power.hashCode() + other.hashCode();
    }

    public boolean equals(Object obj) {
        if (obj instanceof Item) {
            Item item = (Item) obj;
            if (this.power.equals(item.power) && this.other.equals(item.other)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
