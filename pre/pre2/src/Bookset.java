import java.math.BigDecimal;

public class Bookset {
    private String name;
    private double price;
    private long num;
    private BigDecimal sum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public Bookset(String name, double price, long num) {
        this.name = name;
        this.price = price;
        this.num = num;
        this.sum = BigDecimal.valueOf(price).multiply(BigDecimal.valueOf(num));
    }

    public void addNum(long num) {
        this.num = this.num + num;
    }
}
