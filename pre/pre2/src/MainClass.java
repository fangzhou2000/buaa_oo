import java.util.ArrayList;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Bookstack> bookstacks = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            bookstacks.add(new Bookstack());
        }
        operate(scanner, m, bookstacks);
    }

    public static void operate(Scanner scanner, int m, ArrayList<Bookstack> bookstacks) {
        for (int i = 1; i <= m; i++) {
            int t = scanner.nextInt();
            int a = scanner.nextInt();
            if (t == 1) {
                String name = scanner.next();
                try {
                    System.out.println(bookstacks.get(a - 1).query(name).toString());
                } catch (Exception2 exception2) {
                    System.out.println("Oh, no! This is empty.");
                } catch (Exception1 exception1) {
                    System.out.println("Oh, no! We don't have " + name + ".");
                } finally {
                    scanner.nextLine();
                }
            } else if (t == 2) {
                try {
                    System.out.println(bookstacks.get(a - 1).totalkind());
                } catch (Exception2 exception2) {
                    System.out.println("Oh, no! This is empty.");
                } finally {
                    scanner.nextLine();
                }
            } else if (t == 3) {
                try {
                    System.out.println(bookstacks.get(a - 1).totalnum());
                } catch (Exception2 exception2) {
                    System.out.println("Oh, no! This is empty.");
                } finally {
                    scanner.nextLine();
                }
            } else {
                suboperate(scanner, t, a, bookstacks);
            }
        }
    }

    public static void suboperate(Scanner scanner,
                                  int t, int a, ArrayList<Bookstack> bookstacks) {
        if (t == 4) {
            String type = scanner.next();
            String name = scanner.next();
            double price = scanner.nextDouble();
            long num = scanner.nextLong();
            try {
                bookstacks.get(a - 1).add(Booksetfactory.
                        getBookset(scanner, type, name, price, num));
            } catch (Exception3 exception3) {
                System.out.println("Oh, no! The " + name + " exist.");
            } finally {
                scanner.nextLine();
            }
        } else if (t == 5) {
            String name = scanner.next();
            try {
                System.out.println(bookstacks.get(a - 1).remove(name));
            } catch (Exception4 exception4) {
                System.out.println("mei you wo zhen mei you.");
            } finally {
                scanner.nextLine();
            }
        } else if (t == 6) {
            int j = scanner.nextInt();
            try {
                System.out.println(merge(bookstacks,
                        bookstacks.get(a - 1), bookstacks.get(j - 1)));
            } catch (Exception5 exception5) {
                System.out.println("Oh, no. We fail!");
            } finally {
                scanner.nextLine();
            }
        } else {
            System.out.println("subwrong");
        }
    }

    public static int merge(ArrayList<Bookstack> bookstacks,
                            Bookstack bookstack1, Bookstack bookstack2) throws Exception5 {
        Bookstack bookstack = new Bookstack();
        for (String key2 : bookstack2.getBooksets().keySet()) {
            bookstack.getBooksets().put(key2, cloneBookset(bookstack2.getBooksets().get(key2)));
        }
        bookstack.setKind(bookstack2.getKind());
        int flag;
        for (String key1 : bookstack1.getBooksets().keySet()) {
            flag = 0;
            for (String key2 : bookstack2.getBooksets().keySet()) {
                if (compare(bookstack1.getBooksets().get(key1),
                        bookstack2.getBooksets().get(key2))) {
                    bookstack.getBooksets().get(key2)
                            .addNum(bookstack1.getBooksets().get(key1).getNum());
                    flag = 1;
                    break;
                }
            }
            if (flag == 0) {
                bookstack.getBooksets().put(key1, cloneBookset(bookstack1.getBooksets().get(key1)));
                bookstack.addKind(bookstack1.getBooksets().get(key1));
            }
        }
        bookstacks.add(bookstack);
        return bookstacks.size();
    }

    public static Bookset cloneBookset(Bookset bookset1) {
        String[] string1 = bookset1.toString().split(" ");
        String name = string1[0];
        double price = Double.parseDouble(string1[1]);
        long num = Long.parseLong(string1[2]);
        if (bookset1.getClass().getName().equals("Other")) {
            return new Other(name, price, num);
        } else if (bookset1.getClass().getName().equals("OtherA")) {
            long age = Long.parseLong(string1[3]);
            return new OtherA(name, price, num, age);
        } else if (bookset1.getClass().getName().equals("Novel")) {
            long age = Long.parseLong(string1[3]);
            boolean finish = Boolean.parseBoolean(string1[4]);
            return new Novel(name, price, num, age, finish);
        } else if (bookset1.getClass().getName().equals("Poetry")) {
            long age = Long.parseLong(string1[3]);
            String author = string1[4];
            return new Poetry(name, price, num, age, author);
        } else if (bookset1.getClass().getName().equals("OtherS")) {
            long year = Long.parseLong(string1[3]);
            return new OtherS(name, price, num, year);
        } else if (bookset1.getClass().getName().equals("Math")) {
            long year = Long.parseLong(string1[3]);
            long grade = Long.parseLong(string1[4]);
            return new Math(name, price, num, year, grade);
        } else if (bookset1.getClass().getName().equals("Computer")) {
            long year = Long.parseLong(string1[3]);
            String major = string1[4];
            return new Computer(name, price, num, year, major);
        } else {
            System.out.println("clone wrong");
            return new Bookset(name, price, num);
        }
    }

    public static boolean compare(Bookset bookset1, Bookset bookset2) throws Exception5 {
        String[] string1 = bookset1.toString().split(" ");
        String[] string2 = bookset2.toString().split(" ");
        int length = java.lang.Math.max(string1.length, string2.length);
        if (string1[0].equals(string2[0])) {
            for (int i = 1; i < length; i++) {
                if (i == 2) {
                    continue;
                } else {
                    if (!string1[i].equals(string2[i])) {
                        throw new Exception5();
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}

