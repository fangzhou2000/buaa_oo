import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Map {
    private HashMap<String, Mail> nameMap;
    private HashMap<String, HashMap<String, HashMap<String, Mail>>> placeMap;

    public Map() {
        nameMap = new HashMap<>();
        placeMap = new HashMap<>();
    }

    public HashMap<String, Mail> getNameMap() {
        return nameMap;
    }

    public HashMap<String, HashMap<String, HashMap<String, Mail>>> getPlaceMap() {
        return placeMap;
    }

    public void Add(Mail mail) {
        nameMap.put(mail.getName(), mail);
        if (placeMap.containsKey(mail.getPlace())) {
            if (placeMap.get(mail.getPlace()).containsKey(mail.getDate())) {
                //包括地点日期
                placeMap.get(mail.getPlace()).get(mail.getDate()).put(mail.getName(), mail);
            } else {
                //包括地点但不包括日期
                HashMap<String, Mail> h = new HashMap<>();
                h.put(mail.getName(), mail);
                placeMap.get(mail.getPlace()).put(mail.getDate(), h);
            }
        } else {
            //不包括地点日期
            HashMap<String, HashMap<String, Mail>> h1 = new HashMap<>();
            HashMap<String, Mail> h2 = new HashMap<>();
            h2.put(mail.getName(), mail);
            h1.put(mail.getDate(), h2);
            placeMap.put(mail.getPlace(), h1);
        }
    }

    private Pattern patternName = Pattern.compile("^[a-zA-Z-]+");
    private Pattern patternDate = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

    public void Remove(String mail, String place) {
        Matcher matcherName = patternName.matcher(mail);
        Matcher matcherDate = patternDate.matcher(mail);
        if (matcherName.find()) {
            if (placeMap.containsKey(place) && nameMap.containsKey(mail)) {
                if (nameMap.get(mail).getPlace().equals(place)) {
                    String date = nameMap.get(mail).getDate();
                    nameMap.remove(mail);
                    placeMap.get(place).get(date).remove(mail);
                    if (placeMap.get(place).get(date).isEmpty()) {
                        //date空了就删除
                        placeMap.get(place).remove(date);
                        if (placeMap.get(place).isEmpty()) {
                            //place空了就删除
                            placeMap.remove(place);
                        }
                    }
                }
            }
        } else if (matcherDate.find()) {
            if (placeMap.containsKey(place) && placeMap.get(place).containsKey(mail)) {
                for (String name : placeMap.get(place).get(mail).keySet()) {
                    if (nameMap.get(name).getDate().equals(mail)) {
                        nameMap.remove(name);
                    }
                }
                placeMap.get(place).remove(mail);
                if (placeMap.get(place).isEmpty()) {
                    //place空了就删除
                    placeMap.remove(place);
                }
            }
        }
    }

    public void MapQueryUtime(String mail, String place) {
        Matcher matcherName = patternName.matcher(mail);
        Matcher matcherDate = patternDate.matcher(mail);
        if (!placeMap.containsKey(place)) {
            System.out.println("no place exists");
        } else if (matcherName.matches()) {
            if (nameMap.containsKey(mail)) {
                if (nameMap.get(mail).getPlace().equals(place)) {
                    //这里需要额外判断地点和日期是否匹配
                    System.out.println(nameMap.get(mail).getDate());
                } else {
                    System.out.println("no username exists");
                }
            } else {
                System.out.println("no username exists");
            }
        } else if (matcherDate.matches()) {
            if (placeMap.get(place).containsKey(mail)) {
                //这里不需要额外判断地点和日期是否匹配
                sort(placeMap.get(place).get(mail));
            } else {
                System.out.println("no email exists");
            }
        }
    }

    public void sort(HashMap<String, Mail> mails) {
        Set set = mails.keySet();
        Object[] arr = set.toArray();
        Arrays.sort(arr);
        for (Object key : arr) {
            System.out.println(mails.get(key).getName() +
                    "@" + mails.get(key).getDomain() + " " + mails.get(key).getTime());
        }
    }

    public void MapQueryUtype(String name) {
        if (nameMap.containsKey(name)) {
            System.out.println(nameMap.get(name).getUtype());
        }
    }

    public void MapQueryDtype(String name) {
        if (nameMap.containsKey(name)) {
            System.out.println(nameMap.get(name).getDtype());
        } else {
            System.out.println("no username exists");
        }
    }

    public void MapQueryYear(String name) {
        if (nameMap.containsKey(name)) {
            System.out.println(nameMap.get(name).getYear());
        } else {
            System.out.println("no username exists");
        }
    }

    public void MapQueryMonth(String name) {
        if (nameMap.containsKey(name)) {
            System.out.println(nameMap.get(name).getMonth());
        } else {
            System.out.println("no username exists");
        }
    }

    public void MapQueryDay(String name) {
        if (nameMap.containsKey(name)) {
            System.out.println(nameMap.get(name).getDay());
        } else {
            System.out.println("no username exists");
        }
    }

    public void MapQueryHour(String name) {
        if (nameMap.containsKey(name)) {
            System.out.println(nameMap.get(name).getHour());
        } else {
            System.out.println("no username exists");
        }
    }

    public void MapQueryMinute(String name) {
        if (nameMap.containsKey(name)) {
            System.out.println(nameMap.get(name).getMinute());
        } else {
            System.out.println("no username exists");
        }
    }

    public void MapQuerySecond(String name) {
        if (nameMap.containsKey(name)) {
            System.out.println(nameMap.get(name).getSecond());
        } else {
            System.out.println("no username exists");
        }
    }
}
