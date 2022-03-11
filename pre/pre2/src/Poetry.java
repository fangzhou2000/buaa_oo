public class Poetry extends Art {
    private String author;

    public Poetry(String name, double price, long num, long age, String author) {
        super(name, price, num, age);
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public String toString() {
        return super.getName() + " " + super.getPrice() + " "
                + super.getNum() + " " + super.getAge() + " " + this.getAuthor();
    }
}
