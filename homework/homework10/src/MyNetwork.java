import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;
import com.oocourse.spec2.main.Message;

import java.util.HashMap;
import java.util.List;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people;
    //<id, fatherId>
    private HashMap<Integer, Integer> father;
    private HashMap<Integer, Group> groups;
    private HashMap<Integer, Message> messages;

    public MyNetwork() {
        this.people = new HashMap<>();
        this.father = new HashMap<>();
        this.groups = new HashMap<>();
        this.messages = new HashMap<>();
    }

    //union-find sets
    private int find(int id) {
        if (father.get(id) == id) {
            return id;
        } else {
            int ans = find(father.get(id));
            father.put(id, ans);
            return ans;
        }
    }

    private void merge(int id1, int id2) {
        int fatherId1 = find(id1);
        int fatherId2 = find(id2);
        if (fatherId1 != fatherId2) {
            father.put(fatherId2, fatherId1);
        }
    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            return people.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (person == null) {
            return;
        } else if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.put(person.getId(), person);
            father.put(person.getId(), person.getId());
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (contains(id1) && contains(id2) &&
                !getPerson(id1).isLinked(getPerson(id2))) {
            Person person1 = getPerson(id1);
            Person person2 = getPerson(id2);
            ((MyPerson) person1).getAcquaintance().put(id2, person2);
            ((MyPerson) person1).getValue().put(id2, value);
            ((MyPerson) person2).getAcquaintance().put(id1, person1);
            ((MyPerson) person2).getValue().put(id1, value);
            merge(id1, id2);
            //维护valueSum，先加入Group再添加关系的情况
            for (Group group1 : ((MyPerson) person1).getGroups().values()) {
                for (Group group2 : ((MyPerson) person2).getGroups().values()) {
                    if (group1.getId() == group2.getId()) {
                        ((MyGroup) group1).addValueSum(2 * value);
                    }
                }
            }
        } else if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (contains(id1) && contains(id2) && getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
    }

    @Override
    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (contains(id1) && contains(id2) && !getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }

    @Override
    public int compareName(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return getPerson(id1).getName().compareTo(getPerson(id2).getName());
    }

    @Override
    public int queryPeopleSum() {
        return people.size();
    }

    @Override
    public int queryNameRank(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        //初始排名为1
        int rankCount = 1;
        Person person1 = getPerson(id);
        //foreach速度高于for，value仅使用值
        for (Person person2 : people.values()) {
            if (person1.getName().compareTo(person2.getName()) > 0) {
                rankCount++;
            }
        }
        return rankCount;
    }

    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (contains(id1) && !contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (id1 == id2) {
            return true;
        }
        //union-find sets
        int fatherId1 = find(id1);
        int fatherId2 = find(id2);
        return fatherId1 == fatherId2;
    }

    @Override
    public int queryBlockSum() {
        int blockCount = 0;
        //union-find sets
        for (Integer id : father.keySet()) {
            if (father.get(id).equals(id)) {
                blockCount++;
            }
        }
        return blockCount;
    }

    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (group == null) {
            return;
        } else if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        } else {
            groups.put(group.getId(), group);
        }
    }

    @Override
    public Group getGroup(int id) {
        if (groups.containsKey(id)) {
            return groups.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (groups.containsKey(id2) && people.containsKey(id1) &&
                !getGroup(id2).hasPerson(getPerson(id1)) && getGroup(id2).getSize() < 1111) {
            Group group = groups.get(id2);
            Person person = people.get(id1);
            group.addPerson(person);
            //在addPerson中对person添加group，封装！
            //((MyPerson)person).addGroup(group);
        } else if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (groups.containsKey(id2) && !people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (groups.containsKey(id2) && people.containsKey(id1) &&
                getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
    }

    @Override
    public int queryGroupSum() {
        return groups.size();
    }

    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getSize();
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getValueSum();
    }

    @Override
    public int queryGroupAgeMean(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getAgeMean();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getAgeVar();
    }

    @Override
    public void delFromGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (groups.containsKey(id2) && people.containsKey(id1) &&
                getGroup(id2).hasPerson(getPerson(id1))) {
            Group group = groups.get(id2);
            Person person = people.get(id1);
            group.delPerson(person);
        } else if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (groups.containsKey(id2) && !people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (groups.containsKey(id2) && people.containsKey(id1) &&
                !getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException,
            EqualPersonIdException {
        if (!containsMessage(message.getId())
                &&
                ((message.getType() == 0 &&
                        message.getPerson1().getId() != message.getPerson2().getId()) ||
                        (message.getType() != 0))) {
            messages.put(message.getId(), message);
        } else if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (!containsMessage(message.getId()) && message.getType() == 0 &&
                message.getPerson1().getId() == message.getPerson2().getId()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
    }

    @Override
    public Message getMessage(int id) {
        if (containsMessage(id)) {
            return messages.get(id);
        } else {
            return null;
        }
    }

    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (containsMessage(id) && getMessage(id).getType() == 0 &&
                getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()) &&
                getMessage(id).getPerson1() != getMessage(id).getPerson2()) {
            getMessage(id).getPerson1().addSocialValue(getMessage(id).getSocialValue());
            getMessage(id).getPerson2().addSocialValue(getMessage(id).getSocialValue());
            getMessage(id).getPerson2().getMessages().add(0, getMessage(id));
            messages.remove(id);
        } else if (containsMessage(id) && getMessage(id).getType() == 1 &&
                getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1())) {
            for (Person person : ((MyGroup) (getMessage(id).getGroup())).getPeople().values()) {
                person.addSocialValue(getMessage(id).getSocialValue());
            }
            messages.remove(id);
        } else if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        } else if (containsMessage(id) && getMessage(id).getType() == 0 &&
                !(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
            throw new MyRelationNotFoundException(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId());
        } else if (containsMessage(id) && getMessage(id).getType() == 1 &&
                !(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
            throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getSocialValue();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (contains(id)) {
            return getPerson(id).getReceivedMessages();
        } else {
            throw new MyPersonIdNotFoundException(id);
        }
    }
}
