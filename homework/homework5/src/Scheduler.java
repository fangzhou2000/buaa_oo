import com.oocourse.elevator1.PersonRequest;

import java.util.ArrayList;

public class Scheduler implements Runnable {
    private WaitQueue waitQueue;
    private Elevator elevator;

    public Scheduler(WaitQueue waitQueue, Elevator elevator) {
        this.waitQueue = waitQueue;
        this.elevator = elevator;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (waitQueue) {
                if (waitQueue.isEnd() && waitQueue.isEmpty()) {
                    synchronized (elevator.getWaitQueue()) {
                        elevator.getWaitQueue().close();
                        elevator.getWaitQueue().notifyAll();
                    }
                    waitQueue.notifyAll();
                    return;
                }
                if (waitQueue.isEmpty()) {
                    try {
                        waitQueue.wait();
                        //被唤醒后跳过本层if else进入下一层while循环
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    //注意，elevator和schedule的锁不是同一把锁
                    ArrayList<PersonRequest> temp = new ArrayList<>();
                    temp.addAll(waitQueue.getPersonRequests());
                    for (int i = 0; i < temp.size(); i++) {
                        PersonRequest p = temp.get(i);
                        synchronized (elevator.getWaitQueue()) {
                            elevator.getWaitQueue().addRequest(p);
                        }
                        temp.remove(p);
                        i--;
                    }
                    //唤醒processQueue
                    synchronized (elevator.getWaitQueue()) {
                        elevator.getWaitQueue().setArrivePattern(waitQueue.getArrivePattern());
                        elevator.getWaitQueue().notifyAll();
                    }
                    //将waitQueue清空，表明已经分配完毕，下一层循环将wait
                    waitQueue.clearQueue();
                }
            }
        }
    }
}

