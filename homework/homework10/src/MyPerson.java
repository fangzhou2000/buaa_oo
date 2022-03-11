import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MyPerson implements Person {
    private int id;
    private String name;
    private int age;
    private HashMap<Integer, Person> acquaintance;
    private HashMap<Integer, Integer> value;
    private int socialValue;
    private List<Message> messages;
    private HashMap<Integer, Group> groups;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.acquaintance = new HashMap<>();
        this.value = new HashMap<>();
        this.socialValue = 0;
        this.messages = new ArrayList<>();
        this.groups = new HashMap<>();
    }

    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }

    public HashMap<Integer, Integer> getValue() {
        return value;
    }

    public HashMap<Integer, Group> getGroups() {
        return groups;
    }

    public void addGroup(Group group) {
        groups.put(group.getId(), group);
    }

    public void delGroup(Group group) {
        groups.remove(group.getId());
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
    public int hashCode() {
        return Objects.hash(id);
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

    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public int getSocialValue() {
        return socialValue;
    }

    //这个方法好迷啊，干嘛的？
    @Override
    public List<Message> getReceivedMessages() {
        List<Message> receivedMessages = new ArrayList<>();
        for (int i = 0; i < messages.size() && i <= 3; i++) {
            receivedMessages.add(messages.get(i));
        }
        return receivedMessages;
    }
}
