import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            while (in.hasNextLine()) {
                String strA = in.nextLine();
                String strB = in.nextLine();
                Expression expA = new Expression(strA);
                Expression expB = new Expression(strB);
                if (expA.equals(expB)) {
                    System.out.println("Y, " + expA.toString());
                } else {
                    System.out.println("N, " + expA.toString() + ", " + expB.toString());
                }
            }
        }
    }
}

