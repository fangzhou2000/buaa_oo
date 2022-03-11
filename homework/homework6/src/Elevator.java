import com.oocourse.TimableOutput;
import com.oocourse.elevator2.PersonRequest;

public class Elevator implements Runnable {
    private ElevatorQueue elevatorQueue;
    private WaitQueue waitQueue;
    private Person mainPerson;
    private Strategy strategy;
    private int id;
    //2个方向：上==1、下==-1
    private int direction;
    private int floor;
    private int maxfloor;
    private int minfloor;
    private long speedMove;
    private long speedOpen;
    private long speedClose;

    public Elevator(int id, WaitQueue waitQueue) {
        this.elevatorQueue = new ElevatorQueue(6);
        this.waitQueue = waitQueue;
        this.mainPerson = null;
        this.id = id;
        this.direction = 1;
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
            //没有模式时wait
            if (waitQueue.getArrivePattern() == null) {
                try {
                    waitQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //获得模式
            this.strategy = StrategyFactory.getNew(waitQueue);
        }
        while (true) {
            synchronized (waitQueue) {
                //任何队列没有请求且结束输入时结束线程
                if (waitQueue.isEnd() && waitQueue.isEmpty() &&
                        elevatorQueue.isEmpty() && mainPerson == null) {
                    break;
                }
                //任何队列没有请求时wait
                if (waitQueue.isEmpty() && elevatorQueue.isEmpty() && mainPerson == null) {
                    try {
                        waitQueue.wait();
                        //结束输入时也可能notify
                        if (waitQueue.isEnd() && waitQueue.isEmpty() &&
                                elevatorQueue.isEmpty() && mainPerson == null) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //判断模式
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
        //等待所有请求，不一定会输入结束，可能会加电梯
        synchronized (waitQueue) {
            if (!waitQueue.isEnd()) {
                try {
                    waitQueue.wait(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (mainPerson == null) {
            //此时电梯为空
            //没有请求时return（下一循环wait）
            synchronized (waitQueue) {
                if (waitQueue.isEmpty()) {
                    return;
                }
                //选择主请求
                mainPerson = strategy.getMainPerson();
                //如果换向，即换向，注意这个方法仅在选择主请求或主请求进电梯后使用，电梯内换主请求无需换向
                changeDirection();
            }
            //移动至主请求的FROM层
            moveTo(mainPerson.getPersonRequest().getFromFloor());
            //主请求上车，捎带上车
            open();
            getInMainPerson();
            getInSubPerson();
            close();
        }
        //移动至主请求TO层
        while (this.floor != mainPerson.getPersonRequest().getToFloor()) {
            //如果有捎带上下车，则捎带上下车
            checkTakeAndOut();
            moveTo(this.floor - 1);
        }
        //主请求下车，捎带下车
        open();
        getOutMainPerson();
        getOutSubPerson();
        //电梯内换主请求，Night模式不会执行
        changeMainPersonInElevator();
        close();
    }

    private void dealMorning() {
        if (mainPerson == null) {
            //此时电梯为空
            //没有请求时return(下一循环wait)
            synchronized (waitQueue) {
                if (waitQueue.isEmpty()) {
                    return;
                }
                //选择主请求
                mainPerson = strategy.getMainPerson();
                //换向
                changeDirection();
            }
            //移动至主请求FROM层
            moveTo(mainPerson.getPersonRequest().getFromFloor());
            //主请求上车，捎带上车
            open();
            getInMainPerson();
            //morning模式，特殊捎带，能上车就上车
            while (!elevatorQueue.isFull()) {
                synchronized (waitQueue) {
                    if (waitQueue.isEnd() && waitQueue.isEmpty()) {
                        break;
                    }
                    if (waitQueue.isEmpty()) {
                        try {
                            waitQueue.wait();
                            if (waitQueue.isEnd() && waitQueue.isEmpty()) {
                                break;
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    for (int i = 0; i < waitQueue.getPersons().size(); i++) {
                        if (isTake(waitQueue.getPersons().get(i))) {
                            getIn(waitQueue.getPersons().get(i));
                            break;
                        }
                    }
                }
            }
            close();
        }
        //移动至主请求TO层
        while (this.floor != mainPerson.getPersonRequest().getToFloor()) {
            //如果有捎带上下车，则捎带上下车
            checkTakeAndOut();
            moveTo(this.floor + 1);
        }
        //主请求、捎带下车
        open();
        getOutMainPerson();
        getOutSubPerson();
        //电梯内换主请求，Morning模式不会执行
        changeMainPersonInElevator();
        close();
    }

    private void dealRandom() {
        if (mainPerson == null) {
            //此时电梯为空
            //没有请求时return(下一循环wait)
            synchronized (waitQueue) {
                if (waitQueue.isEmpty()) {
                    return;
                }
                //选择主请求
                mainPerson = ((Random) strategy).getMainPerson(this.floor, this.direction);
                //换向
                changeDirection();
            }
            //移动至主请求FROM层
            moveTo(mainPerson.getPersonRequest().getFromFloor());
            //主请求上车，捎带上车
            open();
            getInMainPerson();
            getInSubPerson();
            close();
        }
        //移动至主请求TO层
        while (this.floor != mainPerson.getPersonRequest().getToFloor()) {
            //如果有捎带上下车，则捎带上下车
            checkTakeAndOut();
            moveTo(this.floor + this.direction);
        }
        //主请求下车，捎带下车
        open();
        getOutMainPerson();
        getOutSubPerson();
        //电梯内换主请求，Random可能执行，注意无需换向，因为捎带时保证同向
        changeMainPersonInElevator();
        if (mainPerson != null) {
            //如果电梯内换主请求，捎带上车
            getInSubPerson();
        } else if (mainPerson == null) {
            //如果没有执行电梯内换主请求，判断本层是否有请求，省一个开关门时间
            synchronized (waitQueue) {
                mainPerson =
                        ((Random) strategy).getMainPersonOfThisFloor(this.floor, this.direction);
            }
            if (mainPerson != null) {
                //判断有，主请求上车，捎带上车
                getInMainPerson();
                getInSubPerson();
            }
        }
        close();
    }

    private void changeDirection() {
        if (floor == mainPerson.getPersonRequest().getFromFloor()) {
            if (mainPerson.getPersonRequest().getToFloor() >
                    mainPerson.getPersonRequest().getFromFloor()) {
                direction = 1;
            } else if (mainPerson.getPersonRequest().getToFloor() <
                    mainPerson.getPersonRequest().getFromFloor()) {
                direction = -1;
            }
        } else if (floor < mainPerson.getPersonRequest().getFromFloor()) {
            direction = 1;
        } else if (floor > mainPerson.getPersonRequest().getFromFloor()) {
            direction = -1;
        }
    }

    private void getInMainPerson() {
        getIn(mainPerson);
        mainPerson.setStatus(0);
        changeDirection();
    }

    private void getOutMainPerson() {
        getOut(mainPerson);
        mainPerson = null;
    }

    private void getInSubPerson() {
        synchronized (waitQueue) {
            for (int i = 0; i < waitQueue.getPersons().size(); i++) {
                if (isTake(waitQueue.getPersons().get(i))) {
                    getIn(waitQueue.getPersons().get(i));
                    i--;
                }
            }
        }
    }

    private void getOutSubPerson() {
        for (int i = 0; i < elevatorQueue.getPersons().size(); i++) {
            if (elevatorQueue.getPersons().get(i).getPersonRequest().getToFloor() == floor) {
                getOut(elevatorQueue.getPersons().get(i));
                i--;
            }
        }
    }

    private void changeMainPersonInElevator() {
        if (elevatorQueue.getPersons().isEmpty()) {
            mainPerson = null;
        } else {
            mainPerson = elevatorQueue.getPersons().get(0);
        }
    }

    private void checkTakeAndOut() {
        int openFlag = 0;
        if (ifHasOut()) {
            open();
            getOutSubPerson();
            openFlag = 1;
        }
        if (ifHasTake()) {
            if (openFlag == 0) {
                open();
            }
            getInSubPerson();
            openFlag = 1;
        }
        if (openFlag == 1) {
            close();
        }
    }

    private boolean ifHasOut() {
        for (Person person : elevatorQueue.getPersons()) {
            PersonRequest p = person.getPersonRequest();
            if (p.getToFloor() == this.floor) {
                return true;
            }
        }
        return false;
    }

    private boolean ifHasTake() {
        synchronized (waitQueue) {
            for (Person person : waitQueue.getPersons()) {
                if (isTake(person)) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean isTake(Person person) {
        PersonRequest p = person.getPersonRequest();
        if (person.isAvailable()) {
            if (!this.elevatorQueue.isFull() && p.getFromFloor() == floor) {
                if (direction == 1) {
                    if (p.getToFloor() > this.floor) {
                        return true;
                    }
                } else if (direction == -1) {
                    if (p.getToFloor() < this.floor) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void getIn(Person person) {
        PersonRequest p = person.getPersonRequest();
        synchronized (waitQueue) {
            waitQueue.removePerson(person);
        }
        elevatorQueue.addPerson(person);
        TimableOutput.println("IN-" + p.getPersonId() + "-" + p.getFromFloor() +
                "-" + this.id);
    }

    private void getOut(Person person) {
        PersonRequest p = person.getPersonRequest();
        elevatorQueue.removePerson(person);
        TimableOutput.println("OUT-" + p.getPersonId() + "-" + p.getToFloor() +
                "-" + this.id);
    }

    private void sleepOpenAndClose() {
        sleep(speedOpen);
        sleep(speedClose);
    }

    private void open() {
        TimableOutput.println("OPEN-" + this.floor + "-" + this.id);
        sleepOpenAndClose();
    }

    private void close() {
        TimableOutput.println("CLOSE-" + this.floor + "-" + this.id);
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
        TimableOutput.println("ARRIVE-" + this.floor + "-" + this.id);
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
