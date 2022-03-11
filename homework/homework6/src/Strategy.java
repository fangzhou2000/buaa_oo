public abstract class Strategy {
    private WaitQueue waitQueue;

    public Strategy(WaitQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    public WaitQueue getWaitQueue() {
        return waitQueue;
    }

    public Person getMainPerson() {
        return null;
    }
}
