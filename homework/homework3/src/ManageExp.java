import java.util.regex.Matcher;

public class ManageExp {
    //输入进来的是一个符合题目要求的表达式
    public static Boolean deleteBracket(String string0) {
        String string = string0;
        //标记是否在子表达式内
        Boolean result = true;
        //首项特殊对待
        int frist = 1;
        String op = string.substring(0, 1);
        if ((op.equals("+") || op.equals("-")) && !Character.isDigit(string.charAt(1))) {
            return false;
        }
        string = ManageInput.manageInput(string);
        while (!string.equals("")) {
            op = string.substring(0, 1);
            string = string.substring(1);
            Matcher matcherPower = Macro.getPatternPower().matcher(string);
            Matcher matcherConst = Macro.getPatternConst().matcher(string);
            Matcher matcherSin = Macro.getPatternSin().matcher(string);
            Matcher matcherCos = Macro.getPatternCos().matcher(string);
            Matcher matcherExp = Macro.getPatternExp().matcher(string);
            if (frist == 0 && ((op.equals("+") || op.equals("-")))) {
                result = false;
                break;
            } else if (matcherExp.find()) {
                string = string.substring(matcherExp.group(0).length());
            } else if (matcherPower.find()) {
                string = string.substring(matcherPower.group(0).length());
            } else if (matcherSin.find()) {
                string = string.substring(matcherSin.group(0).length());
            } else if (matcherCos.find()) {
                string = string.substring(matcherCos.group(0).length());
            } else if (matcherConst.find()) {
                string = string.substring(matcherConst.group(0).length());
            }
            frist = 0;
        }
        return result;
    }

    public static String manageExp(String string) {
        if (deleteBracket(string)) {
            return string;
        } else {
            return "(" + string + ")";
        }
    }
}
