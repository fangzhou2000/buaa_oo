import java.util.ArrayList;

public class Process extends Thread {
    private final ProcessingQueue processingQueue;
    private final WaitQueue waitQueue;
    private int type;

    public Process(ProcessingQueue processingQueue, int type, WaitQueue waitQueue) {
        this.processingQueue = processingQueue;
        this.type = type;
        this.waitQueue = waitQueue;
    }

    @Override
    public void run() {
        while (true) {
            // 请将ObjectB替换成合适的对象以加锁  (10)
            synchronized (processingQueue) {
                synchronized (waitQueue) {
                    if (processingQueue.isEmpty() && waitQueue.noWaiting() && waitQueue.isEnd()) {
                        System.out.println("P " + type + " over");
                        return;
                    }
                }
                try {
                    processingQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 请将下面三个dealRequest替换成本类中已经实现的方法以完成审批的正确处理(11)
            switch (type) {
                case 0 : {
                    dealBeijingRequest();
                    break;
                }
                case 1 : {
                    dealDomesticRequest();
                    break;
                }
                default: {
                    dealForeign();
                    break;
                }
            }
        }
    }

    private void dealBeijingRequest() {
        ArrayList<Request> temp = processingQueue.getRequests();
        for (int i = 0; i < temp.size(); i++) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (processingQueue) {
                Request request = temp.get(i);
                if ((request.getBackTime() - request.getLeaveTime()) < 24) {
                    System.out.println("ALLOW: " + request.toString());
                } else {
                    System.out.println("REJECT: " + request.toString());
                }
                temp.remove(i);
                i--;
            }
        }
    }

    private void dealDomesticRequest() {
        ArrayList<Request> temp = processingQueue.getRequests();
        for (int i = 0; i < temp.size(); i++) {
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (processingQueue) {
                Request request = temp.get(i);
                if ((request.getBackTime() - request.getLeaveTime()) < (7 * 24)) {
                    System.out.println("ALLOW: " + request.toString());
                } else {
                    System.out.println("REJECT: " + request.toString());
                }
                temp.remove(i);
                i--;
            }
        }
    }

    private void dealForeign() {
        ArrayList<Request> temp = processingQueue.getRequests();
        for (int i = 0; i < temp.size(); i++) {
            try {
                sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (processingQueue) {
                Request request = temp.get(i);
                if ((request.getBackTime() - request.getLeaveTime()) < (30 * 24)) {
                    System.out.println("ALLOW: " + request.toString());
                } else {
                    System.out.println("REJECT: " + request.toString());
                }
                temp.remove(i);
                i--;
            }
        }
    }
}