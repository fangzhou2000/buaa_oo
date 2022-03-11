import java.util.ArrayList;

public class WaitQueue {
    private ArrayList<Request> requests;
    private boolean isEnd;

    public WaitQueue() {
        requests = new ArrayList<>();
        isEnd = false;
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void clearQueue() {
        requests.clear();
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void close() {
        isEnd = true;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public boolean noWaiting() {
        return requests.isEmpty();
    }
}

