import com.oocourse.elevator1.PersonRequest;

public abstract class Strategy {
    private WaitQueue waitQueue;

    public Strategy(WaitQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    public WaitQueue getWaitQueue() {
        return waitQueue;
    }

    public PersonRequest getMainRequest() {
        return null;
    }
}
