import java.util.Scanner;

public class Booksetfactory {
    public static Bookset getBookset(Scanner scanner, String type,
                                     String name, double price, long num) {
        if (type.equals("Other")) {
            return new Other(name, price, num);
        } else if (type.equals("OtherA")) {
            long age = scanner.nextLong();
            return new OtherA(name, price, num, age);
        } else if (type.equals("Novel")) {
            long age = scanner.nextLong();
            boolean finish = scanner.nextBoolean();
            return new Novel(name, price, num, age, finish);
        } else if (type.equals("Poetry")) {
            long age = scanner.nextLong();
            String author = scanner.next();
            return new Poetry(name, price, num, age, author);
        } else if (type.equals("OtherS")) {
            long year = scanner.nextLong();
            return new OtherS(name, price, num, year);
        } else if (type.equals("Math")) {
            long year = scanner.nextLong();
            long grade = scanner.nextLong();
            return new Math(name, price, num, year, grade);
        } else if (type.equals("Computer")) {
            long year = scanner.nextLong();
            String major = scanner.next();
            return new Computer(name, price, num, year, major);
        } else {
            System.out.println("factorywrong");
            return new Other(name, price, num);
        }
    }
}
