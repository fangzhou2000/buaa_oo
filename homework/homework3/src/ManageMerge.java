import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManageMerge {
    private static Pattern outputItem = Pattern.compile("^([+-])" +
            "(([+-]?[\\d]+)|(x(\\^[+-]?[\\d]+)?)|" +
            "((sin\\$[^$%]+%|cos\\$[^$%]+%)(\\^[+-]?[\\d]+)?)|(\\$([^$%]+)%))" +
            "(\\*" +
            "(([+-]?[\\d]+)|(x(\\^[+-]?[\\d]+)?)|" +
            "((sin\\$[^$%]+%|cos\\$[^$%]+%)(\\^[+-]?[\\d]+)?)|(\\$([^$%]+)%)))*");

    private static Pattern outputPower = Pattern.compile("^x((?:\\^([+-]?[\\d]+))?)");
    private static Pattern outputConst = Pattern.compile("^[+-]?[\\d]+");
    private static Pattern outputTri = Pattern.compile(
            "^((sin)|(cos))\\$([^$%]+)%((?:\\^([+-]?[\\d]+))?)");
    private static Pattern outputExp = Pattern.compile("^\\$([^$%]+)%");

    public static String manageItem(String string0) {
        String string = ManageInput.manageInput(string0);
        Poly poly = new Poly();
        while (!string.equals("")) {
            Item item = new Item();
            Matcher matcherItem = outputItem.matcher(string);
            if (matcherItem.find()) {
                String itemString = matcherItem.group(0);
                if (matcherItem.group(1).equals("-")) {
                    item.mulCoef(BigInteger.valueOf(-1));
                    itemString = itemString.substring(1);
                } else if (matcherItem.group(1).equals("+")) {
                    itemString = itemString.substring(1);
                }
                while (!itemString.equals("")) {
                    if (itemString.charAt(0) == '*') {
                        itemString = itemString.substring(1);
                    }
                    Matcher matcherPower = outputPower.matcher(itemString);
                    Matcher matcherConst = outputConst.matcher(itemString);
                    Matcher matcherTri = outputTri.matcher(itemString);
                    Matcher matcherExp = outputExp.matcher(itemString);
                    if (matcherExp.find()) {
                        String exp = matcherExp.group(0).replaceAll("\\^", "**");
                        exp = exp.replaceAll("\\$", "(");
                        exp = exp.replaceAll("%", ")");
                        item.mulOther(exp);
                        itemString = itemString.substring(matcherExp.group(0).length());
                    } else if (matcherTri.find()) {
                        String tri = matcherTri.group(0).replaceAll("\\^", "**");
                        tri = tri.replaceAll("\\$", "(");
                        tri = tri.replaceAll("%", ")");
                        item.mulOther(tri);
                        itemString = itemString.substring(matcherTri.group(0).length());
                    } else if (matcherPower.find()) {
                        if (matcherPower.group(1).equals("")) {
                            item.mulPower(BigInteger.ONE);
                        } else {
                            item.mulPower(new BigInteger(matcherPower.group(2)));
                        }
                        itemString = itemString.substring(matcherPower.group(0).length());
                    } else if (matcherConst.find()) {
                        BigInteger coef = new BigInteger(matcherConst.group(0));
                        item.mulCoef(coef);
                        itemString = itemString.substring(matcherConst.group(0).length());
                    } else {
                        return string0;
                    }
                }
                merge(poly, item);
                string = string.substring(matcherItem.group(0).length());
            }
        }
        return poly.toString();
    }

    public static void merge(Poly poly, Item item) {
        int flag = 0;
        for (Item i : poly.getPoly().keySet()) {
            if (i.equals(item)) {
                i.addCoef(item.getCoef());
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            poly.getPoly().put(item, item);
        }
    }
}

