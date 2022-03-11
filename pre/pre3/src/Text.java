import java.util.Scanner;

public class Text {
    private String text;

    public Text(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getStrings() {
        String string1 = text.replace(", ", " ");
        String string2 = string1.replace(",", " ");
        return string2.split(" ");
    }

    public int getNum() {
        String string1 = text.replace(", ", " ");
        String string2 = string1.replace(",", " ");
        return string2.split(" ").length;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Map map = new Map();
        while (scanner.hasNext()) {
            Text text = new Text(scanner.nextLine());
            if (text.text.equals("END_OF_INFORMATION")) {
                break;
            } else {
                for (int i = 0; i < text.getStrings().length; i++) {
                    Mail mail = new Mail(text.getStrings()[i]);
                    map.Add(mail);
                }
            }
        }
        while (scanner.hasNext()) {
            String[] strings = scanner.nextLine().split(" ");
            String instr = strings[0];
            String mail = "";
            String place = "";
            if (strings.length == 3) {
                mail = strings[1];
                place = strings[2];
            } else if (strings.length == 4) {
                mail = strings[2];
                place = strings[3];
            } else {
                System.out.println("input wrong");
            }
            query(map, instr, mail, place);
        }
    }

    public static void query(Map map, String instr, String mail, String place) {
        if (instr.equals("qutype")) {
            map.MapQueryUtype(mail);
        } else if (instr.equals("qdtype")) {
            map.MapQueryDtype(mail);
        } else if (instr.equals("qyear")) {
            map.MapQueryYear(mail);
        } else if (instr.equals("qmonth")) {
            map.MapQueryMonth(mail);
        } else if (instr.equals("qday")) {
            map.MapQueryDay(mail);
        } else if (instr.equals("qhour")) {
            map.MapQueryHour(mail);
        } else if (instr.equals("qminute")) {
            map.MapQueryMinute(mail);
        } else if (instr.equals("qsec")) {
            map.MapQuerySecond(mail);
        } else if (instr.equals("del")) {
            map.Remove(mail, place);
        } else if (instr.equals("qutime")) {
            map.MapQueryUtime(mail, place);
        } else {
            System.out.println("type wrong");
        }
    }
}
