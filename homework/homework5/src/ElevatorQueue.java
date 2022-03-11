import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class ElevatorQueue {
    private ArrayList<PersonRequest> personRequests;
    private int maxNum;

    public ElevatorQueue(int maxNum) {
        this.personRequests = new ArrayList<>();
        this.maxNum = maxNum;
    }

    public ArrayList<PersonRequest> getPersonRequests() {
        return personRequests;
    }

    public synchronized void addRequest(PersonRequest personRequest) {
        this.personRequests.add(personRequest);
    }

    public synchronized void removeRequest(PersonRequest personRequest) {
        this.personRequests.remove(personRequest);
    }

    public synchronized boolean isFull() {
        if (this.personRequests.size() == maxNum) {
            return true;
        } else {
            return false;
        }
    }

    public synchronized boolean isEmpty() {
        if (this.personRequests.size() == 0) {
            return true;
        } else {
            return false;
        }
    }
}
