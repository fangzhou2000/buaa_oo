import java.util.ArrayList;

public class Night extends Strategy {

    public Night(WaitQueue waitQueue) {
        super(waitQueue);
    }

    @Override
    public Person getMainPerson() {
        ArrayList<Person> persons = getWaitQueue().getPersons();
        Person highPerson = null;
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if (person.isAvailable()) {
                if (highPerson == null) {
                    highPerson = person;
                } else {
                    if (highPerson.getPersonRequest().getFromFloor() <
                            person.getPersonRequest().getFromFloor()) {
                        highPerson = person;
                    }
                }
            }
        }
        if (highPerson != null) {
            highPerson.setStatus(1);
        }
        return highPerson;
    }
}
