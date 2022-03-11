import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parse {
    private int indexOfstring;
    private String string;

    public Parse(String string) {
        this.indexOfstring = 0;
        this.string = string;
    }

    public Boolean matchExp() throws MyException {
        matchBlank();
        if (matchAddSub()) {
            matchBlank();
        }
        matchItem();
        matchBlank();
        while (matchAddSub()) {
            matchBlank();
            matchItem();
            matchBlank();
        }
        if (indexOfstring == string.length()) {
            return true;
        } else {
            throw new MyException();
        }
    }

    public Boolean matchItem() throws MyException {
        if (matchAddSub()) {
            matchBlank();
        }
        matchFactor();
        matchBlank();
        while (matchMul()) {
            matchBlank();
            matchFactor();
            matchBlank();
        }
        return true;
    }

    private static Pattern parsePower = Pattern.compile("^x");
    private static Pattern parseSin = Pattern.compile("^sin[\\t\\s]*\\([\\t\\s]*");
    private static Pattern parseCos = Pattern.compile("^cos[\\t\\s]*\\([\\t\\s]*");
    private static Pattern parseConst = Pattern.compile("^[+-]?[\\d]+");

    public Boolean matchFactor() throws MyException {
        String substring = string.substring(indexOfstring);
        Matcher power = parsePower.matcher(substring);
        Matcher con = parseConst.matcher(substring);
        Matcher sin = parseSin.matcher(substring);
        Matcher cos = parseCos.matcher(substring);
        if (indexOfstring < string.length() && string.charAt(indexOfstring) == '(') {
            String exp = "";
            int j;
            int flag = 0;
            for (j = indexOfstring + 1; j < string.length(); j++) {
                if (flag == 0 && string.charAt(j) == ')') {
                    exp = string.substring(indexOfstring + 1, j);
                    break;
                } else if (string.charAt(j) == ')') {
                    flag--;
                } else if (string.charAt(j) == '(') {
                    flag++;
                }
            }
            if (j >= string.length()) {
                throw new MyException();
            }
            Parse parse = new Parse(exp);
            parse.matchExp();
            indexOfstring = j + 1;
        } else {
            matchFactortoolong(sin, cos, power, con);
        }
        return true;
    }

    public Boolean matchFactortoolong(Matcher sin, Matcher cos,
                                      Matcher power, Matcher con) throws MyException {
        if (sin.find()) {
            String factor = "";
            indexOfstring = indexOfstring + sin.group(0).length();
            int j;
            int flag = 0;
            for (j = indexOfstring; j < string.length(); j++) {
                if (flag == 0 && string.charAt(j) == ')') {
                    factor = string.substring(indexOfstring, j + 1);
                    break;
                } else if (string.charAt(j) == ')') {
                    flag--;
                } else if (string.charAt(j) == '(') {
                    flag++;
                }
            }
            if (j >= string.length()) {
                throw new MyException();
            }
            Parse parse = new Parse(factor);
            parse.matchFactor();
            parse.matchBlank();
            parse.matchRightBracket();
            indexOfstring = j + 1;
            matchIndex();
        } else if (cos.find()) {
            String factor = "";
            indexOfstring = indexOfstring + cos.group(0).length();
            int j;
            int flag = 0;
            for (j = indexOfstring; j < string.length(); j++) {
                if (flag == 0 && string.charAt(j) == ')') {
                    factor = string.substring(indexOfstring, j + 1);
                    break;
                } else if (string.charAt(j) == ')') {
                    flag--;
                } else if (string.charAt(j) == '(') {
                    flag++;
                }
            }
            if (j >= string.length()) {
                throw new MyException();
            }
            Parse parse = new Parse(factor);
            parse.matchFactor();
            parse.matchBlank();
            parse.matchRightBracket();
            indexOfstring = j + 1;
            matchIndex();
        } else if (power.find()) {
            indexOfstring += power.group(0).length();
            matchBlank();
            matchIndex();
        } else if (con.find()) {
            indexOfstring += con.group(0).length();
        } else {
            throw new MyException();
        }
        return true;
    }

    private static Pattern parseIndex =
            Pattern.compile("^((?:[\\t\\s]*\\*\\*[\\t\\s]*([+-]?[\\d]+))?)");

    public Boolean matchIndex() throws MyException {
        String substring = string.substring(indexOfstring);
        Matcher index = parseIndex.matcher(substring);
        //不必判断i
        if (index.find()) {
            if (!index.group(1).equals("")) {
                BigInteger num = new BigInteger(index.group(2));
                if (num.abs().compareTo(BigInteger.valueOf(50)) > 0) {
                    throw new MyException();
                }
            }
            indexOfstring = indexOfstring + index.group(0).length();
            return true;
        } else {
            throw new MyException();
        }
    }

    public Boolean matchLeftBracket() throws MyException {
        if (indexOfstring < string.length() && string.charAt(indexOfstring) == '(') {
            indexOfstring = indexOfstring + 1;
            return true;
        } else {
            throw new MyException();
        }
    }

    public Boolean matchRightBracket() throws MyException {
        if (indexOfstring < string.length() && string.charAt(indexOfstring) == ')') {
            indexOfstring = indexOfstring + 1;
            return true;
        } else {
            throw new MyException();
        }
    }

    public Boolean matchMul() throws MyException {
        if (indexOfstring < string.length() && string.charAt(indexOfstring) == '*') {
            indexOfstring = indexOfstring + 1;
            return true;
        } else {
            return false;
        }
    }

    public Boolean matchAddSub() throws MyException {
        if (indexOfstring < string.length()
                && (string.charAt(indexOfstring) == '+' || string.charAt(indexOfstring) == '-')) {
            indexOfstring = indexOfstring + 1;
            return true;
        } else {
            return false;
        }
    }

    private static Pattern parseBlank = Pattern.compile("^[\\s\\t]*");

    public Boolean matchBlank() throws MyException {
        String substring = string.substring(indexOfstring);
        Matcher blank = parseBlank.matcher(substring);
        //不必判断i
        if (blank.find()) {
            indexOfstring = indexOfstring + blank.group(0).length();
            return true;
        } else {
            //其实这个分支执行不了
            throw new MyException();
        }
    }
}
