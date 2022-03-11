import com.oocourse.TimableOutput;
import com.oocourse.elevator1.PersonRequest;

public class Elevator implements Runnable {
    private ElevatorQueue elevatorQueue;
    private WaitQueue waitQueue;
    private PersonRequest mainRequest;
    private Strategy strategy;
    private int floor;
    private int maxfloor;
    private int minfloor;
    private long speedMove;
    private long speedOpen;
    private long speedClose;

    public Elevator() {
        this.elevatorQueue = new ElevatorQueue(6);
        this.waitQueue = new WaitQueue();
        this.mainRequest = null;
        this.floor = 1;
        this.maxfloor = 20;
        this.minfloor = 1;
        this.speedMove = 400;
        this.speedOpen = 200;
        this.speedClose = 200;
    }

    public WaitQueue getWaitQueue() {
        return waitQueue;
    }

    @Override
    public void run() {
        synchronized (waitQueue) {
            if (waitQueue.getArrivePattern() == null) {
                try {
                    //没有设置模式时wait
                    waitQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.strategy = StrategyFactory.getNew(waitQueue);
        }
        while (true) {
            synchronized (waitQueue) {
                if (waitQueue.isEnd() && waitQueue.isEmpty() && elevatorQueue.isEmpty()) {
                    break;
                }
                if (waitQueue.isEmpty() && elevatorQueue.isEmpty()) {
                    try {
                        //没有请求了等待
                        waitQueue.wait();
                        if (waitQueue.isEnd() && waitQueue.isEmpty() && elevatorQueue.isEmpty()) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            switch (waitQueue.getArrivePattern()) {
                case "Night": {
                    dealNight();
                    break;
                }
                case "Morning": {
                    dealMorning();
                    break;
                }
                case "Random": {
                    dealRandom();
                    break;
                }
                default: {
                    dealRandom();
                }
            }
        }
    }

    private void dealNight() {
        //Night模式 等待所有请求，直至请求结束
        synchronized (waitQueue) {
            while (!waitQueue.isEnd()) {
                try {
                    waitQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        //跑到主请求那一层
        synchronized (waitQueue) {
            if (mainRequest == null) {
                mainRequest = strategy.getMainRequest();
            }
        }
        moveTo(mainRequest.getFromFloor());
        //主请求上车，捎带上车
        open();
        getIn(mainRequest);
        getInSubRequest();
        close();
        //捎带上车
        moveTo(this.floor - 1);
        while (this.floor != 1) {
            //先判断一下这一层有没有副请求，避免多余开门
            if (ifHasTake()) {
                open();
                getInSubRequest();
                close();
            }
            moveTo(this.floor - 1);
        }
        //主请求下车，副请求下车
        open();
        getOut(mainRequest);
        getOutSubRequest();
        //切换主请求
        changeMainRequest();
        if (mainRequest != null) {
            getInSubRequest();
        }
        close();
    }

    private void dealMorning() {
        if (mainRequest == null) {
            synchronized (waitQueue) {
                mainRequest = strategy.getMainRequest();
            }
            moveTo(mainRequest.getFromFloor());
            //主请求上车、捎带上车
            open();
            getIn(mainRequest);
            //morning模式，特殊捎带，能上车就上车
            synchronized (waitQueue) {
                while (!elevatorQueue.isFull()) {
                    if (waitQueue.isEnd() && waitQueue.isEmpty()) {
                        break;
                    } else if (waitQueue.isEmpty()) {
                        try {
                            waitQueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //如果电梯中没有乘客，将请求队列中到达时间最早的请求作为主请求
                        getIn(waitQueue.getPersonRequests().get(0));
                    }
                }
            }
            close();
        }
        //捎带下车
        moveTo(this.floor + 1);
        while (this.floor != mainRequest.getToFloor()) {
            if (ifHasOut()) {
                open();
                getOutSubRequest();
                close();
            }
            moveTo(this.floor + 1);
        }
        //主请求、捎带下车,切换主请求
        open();
        getOut(mainRequest);
        getOutSubRequest();
        changeMainRequest();
        if (mainRequest != null) {
            getInSubRequest();
        }
        close();
    }

    private void dealRandom() {
        if (mainRequest == null) {
            mainRequest = strategy.getMainRequest();
            //可优化，但好像不属于ALS？
            //moveTo(mainRequest.getFromFloor());
            moveTo(this.floor + mainRequestFromDir());
            while (this.floor != mainRequest.getFromFloor()) {
                if (ifHasOut() || ifHasTake()) {
                    open();
                    if (ifHasOut()) {
                        getOutSubRequest();
                    }
                    if (ifHasTake()) {
                        getInSubRequest();
                    }
                    close();
                }
                moveTo(this.floor + mainRequestFromDir());
            }
            open();
            getIn(mainRequest);
            getInSubRequest();
            close();
        }
        moveTo(this.floor + mainRequestToDir());
        while (this.floor != mainRequest.getToFloor()) {
            if (ifHasOut() || ifHasTake()) {
                open();
                if (ifHasOut()) {
                    getOutSubRequest();
                }
                if (ifHasTake()) {
                    getInSubRequest();
                }
                close();
            }
            moveTo(this.floor + mainRequestToDir());
        }
        //主请求、捎带下车,切换主请求
        open();
        getOut(mainRequest);
        getOutSubRequest();
        changeMainRequest();
        if (mainRequest != null) {
            getInSubRequest();
        } else {
            //mainRequest == null;
            synchronized (waitQueue) {
                if (((Random)strategy).getThisFloor(floor) != null) {
                    mainRequest = ((Random)strategy).getThisFloor(floor);
                    getIn(mainRequest);
                    getInSubRequest();
                }
            }
        }
        close();
    }

    private void getInSubRequest() {
        synchronized (waitQueue) {
            for (int i = 0; i < waitQueue.getPersonRequests().size(); i++) {
                if (isTake(waitQueue.getPersonRequests().get(i))) {
                    getIn(waitQueue.getPersonRequests().get(i));
                    i--;
                }
            }
        }
    }

    private void getOutSubRequest() {
        for (int i = 0; i < elevatorQueue.getPersonRequests().size(); i++) {
            if (elevatorQueue.getPersonRequests().get(i).getToFloor() == floor) {
                getOut(elevatorQueue.getPersonRequests().get(i));
                i--;
            }
        }
    }

    private void changeMainRequest() {
        if (elevatorQueue.getPersonRequests().isEmpty()) {
            mainRequest = null;
        } else {
            mainRequest = elevatorQueue.getPersonRequests().get(0);
        }
    }

    private int mainRequestFromDir() {
        if (mainRequest.getFromFloor() < this.floor) {
            return -1;
        } else if (mainRequest.getFromFloor() > this.floor) {
            return 1;
        } else {
            return 0;
        }
    }

    private int mainRequestToDir() {
        if (mainRequest.getToFloor() < this.floor) {
            return -1;
        } else if (mainRequest.getToFloor() > this.floor) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean ifHasOut() {
        for (PersonRequest p : elevatorQueue.getPersonRequests()) {
            if (p.getToFloor() == this.floor) {
                return true;
            }
        }
        return false;
    }

    private boolean ifHasTake() {
        synchronized (waitQueue) {
            for (PersonRequest p : waitQueue.getPersonRequests()) {
                if (isTake(p)) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean isTake(PersonRequest p) {
        if (!this.elevatorQueue.isFull() && p.getFromFloor() == floor &&
                ((mainRequest.getToFloor() < this.floor && p.getToFloor() < this.floor) ||
                        (mainRequest.getToFloor() > this.floor && p.getToFloor() > this.floor))) {
            return true;
        }
        return false;
    }

    private void getIn(PersonRequest p) {
        synchronized (waitQueue) {
            elevatorQueue.addRequest(p);
            TimableOutput.println("IN-" + p.getPersonId() + "-" + p.getFromFloor());
            waitQueue.removeRequest(p);
        }
    }

    private void getOut(PersonRequest p) {
        elevatorQueue.removeRequest(p);
        TimableOutput.println("OUT-" + p.getPersonId() + "-" + p.getToFloor());
    }

    private void sleepOpenAndClose() {
        sleep(speedOpen);
        sleep(speedClose);
    }

    private void open() {
        TimableOutput.println("OPEN-" + this.floor);
        sleepOpenAndClose();
    }

    private void close() {
        TimableOutput.println("CLOSE-" + this.floor);
    }

    private void moveTo(int targetFloor) {
        while (this.floor != targetFloor) {
            if (this.floor < targetFloor) {
                this.floor++;
            } else {
                this.floor--;
            }
            sleep(speedMove);
            arrive();
        }
    }

    private void arrive() {
        TimableOutput.println("ARRIVE-" + this.floor);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
