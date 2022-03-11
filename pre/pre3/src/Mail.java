import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mail {
    private String originAddress;
    private String name;
    private String time;
    private String domain;
    private String year;
    private String month;
    private String day;
    private String hour;
    private String minute;
    private String second;
    private String date;
    private String place;

    public Mail(String mail) {
        Pattern pattern = Pattern.
                compile("^([\\d:]*)-*" +
                        "(\\d{2})-(\\d{2})-(\\d{4})-" +
                        "([a-zA-Z-]+)@([a-zA-Z\\d.]+)-([a-zA-Z]*)$");
        Matcher matcher = pattern.matcher(mail);
        while (matcher.find()) {
            name = matcher.group(5).toLowerCase(Locale.ROOT);
            domain = matcher.group(6);
            place = matcher.group(7);
            originAddress = matcher.group(5) + "@" + matcher.group(6);
            year = matcher.group(4);
            month = matcher.group(3);
            day = matcher.group(2);
            time = year + "-" + month + "-" + day;
            hour = "null";
            minute = "null";
            second = "null";
            if (!matcher.group(1).equals("")) {
                String[] strings = matcher.group(1).split(":");
                if (strings.length == 1) {
                    hour = strings[0];
                    time = time + "-" + hour;
                } else if (strings.length == 2) {
                    hour = strings[1];
                    minute = strings[0];
                    time = time + "-" + hour + ":" + minute;
                } else if (strings.length == 3) {
                    hour = strings[2];
                    minute = strings[1];
                    second = strings[0];
                    time = time + "-" + hour + ":" + minute + ":" + second;
                }
            }
            date = year + "-" + month + "-" + day;
        }
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getDomain() {
        return domain;
    }

    public String getTime() {
        return time;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getSecond() {
        return second;
    }

    public String getDtype() {
        return domain.split("\\.")[0];
    }

    public String getPlace() {
        return place;
    }

    private Pattern patternNameA = Pattern.compile("a{2,3}b{2,4}a{2,4}c{2,3}");
    private Pattern patternNameB = Pattern.compile("a{2,3}(ba){0,100}?(bc){2,4}");
    private Pattern patternNameC =
            Pattern.compile("[aA]{2,3}([bB][aA]){0,100}?([bB][cC]){2,4}");
    private Pattern patternNameD =
            Pattern.compile("^a{0,3}b{1,100}?c{2,3}.*[bB]{1,2}[aA]{1,2}[cC]{0,3}$");
    private Pattern patternNameE = Pattern.compile(".*?a(.*b){2}?.*?c.*?b(.*c){2}?");

    public String getUtype() {
        int num = 0;
        String kind = "";
        String originName = originAddress.split("@")[0];
        Matcher matcherNameA = patternNameA.matcher(originName);
        if (matcherNameA.find()) {
            num++;
            kind = kind + "A";
        }
        Matcher matcherNameB = patternNameB.matcher(originName);
        if (matcherNameB.find()) {
            num++;
            kind = kind + "B";
        }
        Matcher matcherNameC = patternNameC.matcher(originName);
        if (matcherNameC.find()) {
            num++;
            kind = kind + "C";
        }
        Matcher matcherNameD = patternNameD.matcher(originName);
        if (matcherNameD.find()) {
            num++;
            kind = kind + "D";
        }
        Matcher matcherNameE = patternNameE.matcher(originName);
        if (matcherNameE.find()) {
            num++;
            kind = kind + "E";
        }
        return num + " " + kind;
    }
}
