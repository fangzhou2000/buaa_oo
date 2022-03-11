import java.math.BigInteger;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
    private final String addsub = "[+-]";
    private final String num = "[\\\\d]+";
    private final String opnum = "[+-]?[\\\\d]+";
    private final String index = "\\\\^" + opnum;
    private final String power = "[+-]?x(" + index + ")?";
    private final String confactor = opnum;
    private final String varfactor = power;
    private final String factor = "((" + confactor + ")|(" + varfactor + "))";
    private final String item = addsub + "?" + factor + "(\\\\*" + factor + ")*";
    private static Pattern pattern = Pattern.compile("([+-]?)([+-]?)(([+-]?[\\d]+)|" +
            "([+-]?x(\\^[+-]?[\\d]+)?))(\\*(([+-]?[\\d]+)|(x(\\^[+-]?[\\d]+)?)))*");
    private static Pattern patternPower = Pattern.compile("([+-]?)x\\^([+-]?[\\d]+)");
    private static Pattern patternX = Pattern.compile("([+-]?)x");
    private static Pattern patternNum = Pattern.compile("([+-]?[\\d]+)");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        //处理原表达式
        string = string.replaceAll(" |\\t", "");
        string = string.replaceAll("\\*\\*", "^");
        Matcher matcher = pattern.matcher(string);
        //定义多项式
        Poly poly = new Poly();
        //匹配项
        while (matcher.find()) {
            String item = matcher.group(0);
            //提取项的符号
            String op;
            if (matcher.group(1).equals("-")) {
                op = "-";
                item = item.substring(1);
            } else if (matcher.group(1).equals("+")) {
                op = "+";
                item = item.substring(1);
            } else {
                op = "+";
            }
            //提取并计算系数和指数
            BigInteger index = BigInteger.ZERO;
            BigInteger coef = BigInteger.ONE;
            if (matcher.group(2).equals("-")) {
                coef = BigInteger.valueOf(-1);
                item = item.substring(1);
            } else if (matcher.group(2).equals("+")) {
                item = item.substring(1);
            }
            String[] powers = item.split("\\*");
            for (int i = 0; i < powers.length; i++) {
                Matcher matcherPower = patternPower.matcher(powers[i]);
                Matcher matcherX = patternX.matcher(powers[i]);
                Matcher matcherNum = patternNum.matcher(powers[i]);
                if (matcherPower.matches()) {
                    index = index.add(new BigInteger(matcherPower.group(2)));
                    //-x系数要乘-1
                    if (matcherPower.group(1).equals("-")) {
                        coef = coef.multiply(new BigInteger("-1"));
                    }
                } else if (matcherX.matches()) {
                    index = index.add(BigInteger.ONE);
                    //-x系数要乘-1
                    if (matcherX.group(1).equals("-")) {
                        coef = coef.multiply(new BigInteger("-1"));

                    }
                } else if (matcherNum.matches()) {
                    coef = coef.multiply(new BigInteger(matcherNum.group(1)));
                }
            }
            //建立标准的项，符号还未标准
            Power power = new Power(op, coef, index);
            //将标准的项加入到多项式中
            poly.getPoly().add(power);
        }
        //对每一个幂函数求导
        poly.derivative();
        //按指数大小排序方便合并同类项
        poly.sort();
        //先把项的符号和系数的符号合并了，这样合并同类项的时候只用加就行
        poly.mergeOp();
        //合并同类项
        poly.merge();
        //选一个正项作为开头
        poly.sortFrist();
        //输出
        poly.print();
    }
}
