import java.util.ArrayList;

public class WaitQueue {
    private ArrayList<Person> persons;
    private String arrivePattern;
    private boolean isEnd;

    public WaitQueue() {
        this.persons = new ArrayList<>();
        this.arrivePattern = null;
        this.isEnd = false;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public String getArrivePattern() {
        return arrivePattern;
    }

    public boolean isEmpty() {
        for (int i = 0; i < persons.size(); i++) {
            if (persons.get(i).isAvailable()) {
                return false;
            }
        }
        return true;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void close() {
        this.isEnd = true;
    }

    public void addPerson(Person p) {
        persons.add(p);
    }

    public void removePerson(Person p) {
        persons.remove(p);
    }

    public void setArrivePattern(String arrivePattern) {
        this.arrivePattern = arrivePattern;
    }
}
