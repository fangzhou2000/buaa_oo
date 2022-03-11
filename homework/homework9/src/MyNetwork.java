import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people;

    public MyNetwork() {
        people = new HashMap<>();
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
        //bfs
        ArrayList<Integer> bfsQueue = new ArrayList<>();
        HashMap<Integer, Boolean> bfsVisited = new HashMap<>();
        for (Integer id : people.keySet()) {
            bfsVisited.put(id, false);
        }
        bfsQueue.add(id1);
        bfsVisited.put(id1, true);
        while (!bfsQueue.isEmpty()) {
            int queueId = bfsQueue.remove(0);
            for (Integer id : ((MyPerson) getPerson(queueId)).getAcquaintance().keySet()) {
                if (bfsVisited.get(id) == false) {
                    if (id == id2) {
                        return true;
                    } else {
                        bfsQueue.add(id);
                        bfsVisited.put(id, true);
                    }
                }
            }
        }
        return false;
    }

    @Override
    public int queryBlockSum() {
        int blockCount = 0;
        //bfs
        ArrayList<Integer> bfsQueue = new ArrayList<>();
        ArrayList<Integer> bfsNotVisited = new ArrayList<>();
        for (Integer id : people.keySet()) {
            bfsNotVisited.add(id);
        }
        while (!bfsNotVisited.isEmpty()) {
            int visitedId = bfsNotVisited.remove(0);
            bfsQueue.add(visitedId);
            blockCount++;
            while (!bfsQueue.isEmpty()) {
                int queueId = bfsQueue.remove(0);
                for (Integer id : ((MyPerson) getPerson(queueId)).getAcquaintance().keySet()) {
                    if (bfsNotVisited.contains(id)) {
                        bfsQueue.add(id);
                        bfsNotVisited.remove(id);
                    }
                }
            }
        }
        return blockCount;
    }
}
