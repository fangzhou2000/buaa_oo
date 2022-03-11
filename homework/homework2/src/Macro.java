import java.util.regex.Pattern;

public class Macro {
    private static String num = "([+-]?[\\d]+)";
    private static String power = "(x(\\^[+-]?[\\d]+)?)";
    private static String tri = "((sin(x)|cos(x))(\\^[+-]?[\\d]+)?)";
    private static String exp = "(\\(.+\\))";
    private static String item = "[+-]?[+-]?" +
            "(" + num + "|" + power + "|" + tri + "|" + exp + ")" +
            "\\*(" + num + "|" + power + "|" + tri + "|" + exp + ")*";
    private static Pattern patternItem = Pattern.compile("([+-])" +
            "(([+-]?[\\d]+)|(x(\\^[+-]?[\\d]+)?)|" +
            "((sin\\(x\\)|cos\\(x\\))(\\^[+-]?[\\d]+)?)|(\\$([^$%]*)%))" +
            "(\\*" +
            "(([+-]?[\\d]+)|(x(\\^[+-]?[\\d]+)?)|" +
            "((sin\\(x\\)|cos\\(x\\))(\\^[+-]?[\\d]+)?)|(\\$([^$%]*)%)))*");

    private static Pattern patternPower = Pattern.compile("^x((?:\\^([+-]?[\\d]+))?)");
    private static Pattern patternConst = Pattern.compile("^([+-]?[\\d]+)");
    private static Pattern patternSin = Pattern.compile("^sin\\(x\\)((?:\\^([+-]?[\\d]+))?)");
    private static Pattern patternCos = Pattern.compile("^cos\\(x\\)((?:\\^([+-]?[\\d]+))?)");
    private static Pattern patternExp = Pattern.compile("^\\$([^$%]*)%?");

    public static Pattern getPatternItem() {
        return patternItem;
    }

    public static Pattern getPatternConst() {
        return patternConst;
    }

    public static Pattern getPatternPower() {
        return patternPower;
    }

    public static Pattern getPatternCos() {
        return patternCos;
    }

    public static Pattern getPatternSin() {
        return patternSin;
    }

    public static Pattern getPatternExp() {
        return patternExp;
    }
}
