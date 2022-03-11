public abstract class Strategy {
    private WaitQueue waitQueue;
    private String type;

    public Strategy(WaitQueue waitQueue, String type) {
        this.waitQueue = waitQueue;
        this.type = type;
    }

    public WaitQueue getWaitQueue() {
        return waitQueue;
    }

    public String getType() {
        return type;
    }

    public Person getMainPerson() {
        return null;
    }
}
