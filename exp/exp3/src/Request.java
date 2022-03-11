public class Request {
    private int leaveTime;
    private int backTime;
    private String destination;

    public Request(int leaveTime, int backTime, String destination) {
        this.backTime = backTime;
        this.leaveTime = leaveTime;
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
    }

    public int getLeaveTime() {
        return leaveTime;
    }

    public int getBackTime() {
        return backTime;
    }

    public String toString() {
        // need to complete (8)
        return "<destination:" + destination + " FROM-" + leaveTime + "-TO-" + backTime + ">";
    }
}