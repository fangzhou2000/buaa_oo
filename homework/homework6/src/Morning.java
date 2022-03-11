import java.util.ArrayList;

public class Morning extends Strategy {

    public Morning(WaitQueue waitQueue) {
        super(waitQueue);
    }

    @Override
    public Person getMainPerson() {
        ArrayList<Person> persons = getWaitQueue().getPersons();
        Person lowPerson = null;
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if (person.isAvailable()) {
                if (lowPerson == null) {
                    lowPerson = person;
                } else {
                    if (lowPerson.getPersonRequest().getFromFloor() >
                            person.getPersonRequest().getFromFloor()) {
                        lowPerson = person;
                    }
                }
            }
        }
        if (lowPerson != null) {
            lowPerson.setStatus(1);
        }
        return lowPerson;
    }
}
