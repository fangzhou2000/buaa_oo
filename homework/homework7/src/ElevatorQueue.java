import java.util.ArrayList;

public class ElevatorQueue {
    private ArrayList<Person> persons;
    private int maxNum;

    public ElevatorQueue(int maxNum) {
        this.persons = new ArrayList<>();
        this.maxNum = maxNum;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public synchronized void addPerson(Person person) {
        this.persons.add(person);
    }

    public synchronized void removePerson(Person person) {
        this.persons.remove(person);
    }

    public synchronized boolean isFull() {
        if (this.persons.size() == maxNum) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean isEmpty() {
        if (this.persons.size() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
