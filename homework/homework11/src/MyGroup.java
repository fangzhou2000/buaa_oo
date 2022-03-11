import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.Objects;

public class MyGroup implements Group {
    private int id;
    private HashMap<Integer, Person> people;
    //缓存变量
    private int valueSum;
    private int ageSum;
    private long ageSum2;

    public MyGroup(int id) {
        this.id = id;
        this.people = new HashMap<>();
        this.valueSum = 0;
        this.ageSum = 0;
        this.ageSum2 = 0;
    }

    public HashMap<Integer, Person> getPeople() {
        return people;
    }

    public void addValueSum(int value) {
        valueSum += value;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyGroup myGroup = (MyGroup) o;
        return id == myGroup.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public void addPerson(Person person) {
        if (person == null) {
            return;
        }
        people.put(person.getId(), person);
        ((MyPerson) person).addGroup(this);
        //维护valueSum
        for (Person p : people.values()) {
            if (p.isLinked(person)) {
                if (p.getId() == person.getId()) {
                    valueSum += person.queryValue(p);
                } else {
                    valueSum += 2 * person.queryValue(p);
                }
            }
        }
        //维护ageSum
        ageSum += person.getAge();
        //维护ageSum2
        ageSum2 += ((long) person.getAge() * (long) person.getAge());
    }

    @Override
    public boolean hasPerson(Person person) {
        if (person == null) {
            return false;
        }
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        return valueSum;
    }

    //(\sum int i; 0 <= i && i < people.length; people[i].getAge()) / people.length
    //先求和再除
    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        } else {
            return ageSum / people.size();
        }
    }

    //采用维护的方法可能爆int
    //先求和再除
    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        } else {
            return (int) ((ageSum2 -
                    2 * (long) ageSum * (long) getAgeMean() +
                    (long) people.size() * (long) getAgeMean() * (long) getAgeMean())
                    /
                    (long) people.size());
        }
    }

    @Override
    public void delPerson(Person person) {
        if (person == null) {
            return;
        }
        //维护valueSum
        for (Person p : people.values()) {
            if (p.isLinked(person)) {
                if (p.getId() == person.getId()) {
                    valueSum -= person.queryValue(p);
                } else {
                    valueSum -= 2 * person.queryValue(p);
                }
            }
        }
        //维护ageSum
        ageSum -= person.getAge();
        //维护ageSum2
        ageSum2 -= ((long) person.getAge() * (long) person.getAge());
        people.remove(person.getId());
        ((MyPerson) person).delGroup(this);
    }

    @Override
    public int getSize() {
        return people.size();
    }
}
