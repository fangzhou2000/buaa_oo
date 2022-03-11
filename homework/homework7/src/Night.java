import java.util.ArrayList;

public class Night extends Strategy {

    public Night(WaitQueue waitQueue, String type) {
        super(waitQueue, type);
    }

    @Override
    public Person getMainPerson() {
        ArrayList<Person> persons = getWaitQueue().getPersons();
        Person highPerson = null;
        for (int i = 0; i < persons.size(); i++) {
            Person person = persons.get(i);
            if (person.isAvailableForElevator(getType())) {
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
            highPerson.setMainPersonFlag(1);
        }
        return highPerson;
    }
}
