import java.util.ArrayList;

public class ProcessingQueue {
    private ArrayList<Request> requests;
    private int id;

    public ProcessingQueue(int id) {
        requests = new ArrayList<>();
        this.id = id;
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public synchronized void addRequest(Request request) {
        requests.add(request);
    }

    public boolean isEmpty() {
        return requests.isEmpty();
    }

    public int getId() {
        return id;
    }
}

