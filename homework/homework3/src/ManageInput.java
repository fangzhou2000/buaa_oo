public class ManageInput {
    public static String manageOrigin(String origin0) {
        String origin = origin0;
        String result = "";
        origin = origin.replaceAll(" |\\t", "");
        origin = origin.replaceAll("\\*\\*", "^");
        origin = origin.replaceAll("\\+\\+", "+");
        origin = origin.replaceAll("--", "+");
        origin = origin.replaceAll("\\+-", "-");
        origin = origin.replaceAll("-\\+", "-");
        origin = origin.replaceAll("\\+\\+", "+");
        origin = origin.replaceAll("--", "+");
        origin = origin.replaceAll("\\+-", "-");
        origin = origin.replaceAll("-\\+", "-");
        if (origin.charAt(0) != '+' && origin.charAt(0) != '-') {
            result = '+' + origin;
        } else if (origin.charAt(0) == '-') {
            result = "+0" + origin;
        } else {
            result = origin;
        }
        return result;
    }

    public static String manageBracket(String string) {
        String result;
        StringBuilder stringBuilder = new StringBuilder(string);
        int con = 0;
        int flag = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == '(') {
                if (con == 0) {
                    stringBuilder.setCharAt(i, '$');
                    flag = 1;
                }
                con++;
            } else if (string.charAt(i) == ')') {
                if (con == 1 && flag == 1) {
                    stringBuilder.setCharAt(i, '%');
                    flag = 0;
                }
                con--;
            }
        }
        result = stringBuilder.toString();
        return result;
    }

    public static String manageInput(String string0) {
        String string = string0;
        string = ManageInput.manageOrigin(string);
        string = ManageInput.manageBracket(string);
        return string;
    }

    public static String manageFactor(String string0) {
        String string = string0;
        string = ManageInput.manageBracket(string);
        return string;
    }
}

