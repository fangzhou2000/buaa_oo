import main.java.com.oocourse.OutRequest;
import main.java.com.oocourse.StuRequest;

public class InputThread extends Thread {
    private final WaitQueue waitQueue;

    public InputThread(WaitQueue waitQueue) {
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        OutRequest outRequest = new OutRequest(System.in);
        while (true) {
            StuRequest stuRequest = outRequest.getNextRequest();
            // 请将ObjectA替换成合适的对象以加锁  (9)
            synchronized (waitQueue) {
                if (stuRequest == null) {
                    waitQueue.close();
                    waitQueue.notifyAll();
                    return;
                } else {
                    Request request = new Request(stuRequest.getLeaveTime(), 
                                                  stuRequest.getBackTime(), 
                                                  stuRequest.getDestination());
                    waitQueue.addRequest(request);
                    waitQueue.notifyAll();
                }
            }
        }
    }
}

