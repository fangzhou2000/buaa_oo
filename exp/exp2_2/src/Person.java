public class Person implements Cloneable {
    private final int personId;
    private String boardTime;

    public Person(int personId, String boardTime) {
        this.personId = personId;
        this.boardTime = boardTime;
    }

    public Person(int personId) {
        this.personId = personId;
    }

    public void setBoardTime(String newBoardTime) {
        this.boardTime = newBoardTime;
    }

    public int getPersonId() {
        return personId;
    }

    @Override
    public String toString() {
        return personId + " " + boardTime + " ";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        return personId == person.personId;
    }

    @Override
    public Person clone() {
        Person clone = null;
        try {
            clone = (Person) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
