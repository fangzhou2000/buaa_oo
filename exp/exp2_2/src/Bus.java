import java.util.ArrayList;
import java.util.List;

public class Bus {
    private List<Person> personList;

    public Bus() {
        personList = new ArrayList<>();
    }

    public void addPerson(Person person) {
        personList.add(person);
    }

    public void removePerson(Person person) {
        personList.remove(person);
    }

    public List<Person> getPersonList() {
        // TODO: return the current list of persons in the bus by implementing the deep clone
        List<Person> myPersonList = new ArrayList<>();
        for (Person per : personList) {
            Person myPer = per.clone();
            myPersonList.add(myPer);
        }
        return myPersonList;
        /*List<Person> trylist = new ArrayList<>();
        for (Person per : personList) {
            String[] strings = per.toString().split(" ");
            Person tryPer = new Person(Integer.parseInt(strings[0]), strings[1]);
            trylist.add(tryPer);
        }
        return trylist;*/
    }
}
