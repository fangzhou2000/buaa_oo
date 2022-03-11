import java.math.BigInteger;
import java.util.regex.Matcher;

public class FactorFactory {
    //输入的是指数和括号都换了的
    public static Factor generateFactor(String string) {
        Matcher matcherExp = Macro.getPatternExp().matcher(string);
        Matcher matcherPower = Macro.getPatternPower().matcher(string);
        Matcher matcherSin = Macro.getPatternSin().matcher(string);
        Matcher matcherCos = Macro.getPatternCos().matcher(string);
        Matcher matcherConst = Macro.getPatternConst().matcher(string);
        if (matcherExp.find()) {
            //这里的group(1)去除了$和%
            return new Expression(matcherExp.group(1));
        } else if (matcherPower.find()) {
            BigInteger index;
            if (matcherPower.group(1).equals("")) {
                index = BigInteger.ONE;
            } else {
                index = new BigInteger(matcherPower.group(2));
            }
            return new Power(index);
        } else if (matcherSin.find()) {
            BigInteger index;
            String sinfactor = matcherSin.group(1);
            if (matcherSin.group(2).equals("")) {
                index = BigInteger.ONE;
            } else {
                index = new BigInteger(matcherSin.group(3));
            }
            return new Sin(sinfactor, index);
        } else if (matcherCos.find()) {
            BigInteger index;
            String cosfactor = matcherCos.group(1);
            if (matcherCos.group(2).equals("")) {
                index = BigInteger.ONE;
            } else {
                index = new BigInteger(matcherCos.group(3));
            }
            return new Cos(cosfactor, index);
        } else if (matcherConst.find()) {
            return new Const(new BigInteger(matcherConst.group(1)));
        } else {
            System.out.println("WRONG FORMAT!" + "The subfactor is " + string);
            System.exit(0);
            return null;
        }
    }
}
