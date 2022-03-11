import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Integer, Person> acquaintance;
    private HashMap<Integer, Integer> value;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
    }

    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }

    public HashMap<Integer, Integer> getValue() {
        return value;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof Person)) {
            return false;
        }
        return (((Person) obj).getId() == id);
    }

    @Override
    public boolean isLinked(Person person) {
        return acquaintance.containsKey(person.getId()) || person.getId() == id;
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintance.containsKey(person.getId())) {
            return value.get(person.getId());
        } else {
            return 0;
        }
    }

    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }
}
