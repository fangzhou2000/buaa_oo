import static java.lang.Math.abs;

public class Random extends Strategy {
    public Random(WaitQueue waitQueue) {
        super(waitQueue);
    }

    public Person getMainPerson(int thisFloor, int direction) {
        Person targetPerson = null;
        for (int i = 0; i < getWaitQueue().getPersons().size(); i++) {
            Person person = getWaitQueue().getPersons().get(i);
            if (person.isAvailable()) {
                if (direction == 1) {
                    if (thisFloor <= person.getPersonRequest().getFromFloor() &&
                            person.getPersonRequest().getFromFloor() <
                                    person.getPersonRequest().getToFloor()) {
                        if (targetPerson == null) {
                            targetPerson = person;
                        } else if (targetPerson.getPersonRequest().getFromFloor() - thisFloor >
                                person.getPersonRequest().getFromFloor() - thisFloor) {
                            targetPerson = person;
                        }
                    }
                } else if (direction == -1) {
                    if (thisFloor >= person.getPersonRequest().getFromFloor() &&
                            person.getPersonRequest().getFromFloor() >
                                    person.getPersonRequest().getToFloor()) {
                        if (targetPerson == null) {
                            targetPerson = person;
                        } else if (thisFloor - targetPerson.getPersonRequest().getFromFloor() >
                                thisFloor - person.getPersonRequest().getFromFloor()) {
                            targetPerson = person;
                        }
                    }
                }
            }
        }
        if (targetPerson == null) {
            for (int i = 0; i < getWaitQueue().getPersons().size(); i++) {
                Person person = getWaitQueue().getPersons().get(i);
                if (person.isAvailable()) {
                    if (targetPerson == null) {
                        targetPerson = person;
                    } else if (abs(targetPerson.getPersonRequest().getFromFloor() - thisFloor) >
                            abs(person.getPersonRequest().getFromFloor() - thisFloor)) {
                        targetPerson = person;
                    }
                }
            }
        }
        //一定不为空
        if (targetPerson != null) {
            targetPerson.setStatus(1);
        }
        return targetPerson;
    }

    public Person getMainPersonOfThisFloor(int thisFloor, int direction) {
        Person targetPerson = null;
        for (int i = 0; i < getWaitQueue().getPersons().size(); i++) {
            Person person = getWaitQueue().getPersons().get(i);
            if (person.isAvailable()) {
                if (person.getPersonRequest().getFromFloor() == thisFloor) {
                    if (direction == 1) {
                        if (person.getPersonRequest().getToFloor() > thisFloor) {
                            targetPerson = person;
                            break;
                        }
                    } else if (direction == -1) {
                        if (person.getPersonRequest().getToFloor() < thisFloor) {
                            targetPerson = person;
                            break;
                        }
                    }
                }
            }
        }
        if (targetPerson == null) {
            for (int i = 0; i < getWaitQueue().getPersons().size(); i++) {
                Person person = getWaitQueue().getPersons().get(i);
                if (person.isAvailable()) {
                    if (person.getPersonRequest().getFromFloor() == thisFloor) {
                        if (targetPerson == null) {
                            targetPerson = person;
                        } else if (abs(targetPerson.getPersonRequest().getToFloor() - thisFloor)
                                >
                                abs(person.getPersonRequest().getToFloor() - thisFloor)) {
                            targetPerson = person;
                        }
                    }
                }
            }
        }
        //可能为空
        if (targetPerson != null) {
            targetPerson.setStatus(1);
        }
        return targetPerson;
    }
}
