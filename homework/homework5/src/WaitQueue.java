import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class WaitQueue {
    private ArrayList<PersonRequest> personRequests;
    private String arrivePattern;
    private boolean isEnd;

    public WaitQueue() {
        this.personRequests = new ArrayList<>();
        this.arrivePattern = null;
        this.isEnd = false;
    }

    public ArrayList<PersonRequest> getPersonRequests() {
        return personRequests;
    }

    public String getArrivePattern() {
        return arrivePattern;
    }

    public boolean isEmpty() {
        return personRequests.isEmpty();
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void clearQueue() {
        personRequests.clear();
    }

    public void close() {
        this.isEnd = true;
    }

    public void addRequest(PersonRequest request) {
        personRequests.add(request);
    }

    public void removeRequest(PersonRequest request) {
        personRequests.remove(request);
    }

    public void setArrivePattern(String arrivePattern) {
        this.arrivePattern = arrivePattern;
    }
}
