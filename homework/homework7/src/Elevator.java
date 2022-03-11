import com.oocourse.TimableOutput;
import com.oocourse.elevator3.PersonRequest;

public class Elevator implements Runnable {
    private ElevatorQueue elevatorQueue;
    private WaitQueue waitQueue;
    private Person mainPerson;
    private Strategy strategy;
    private int id;
    private String type;
    private int direction;
    private int floor;
    private int maxfloor;
    private int minfloor;
    private long speedMove;
    private long speedOpen;
    private long speedClose;

    public Elevator(int id, String type, WaitQueue waitQueue) {
        this.id = id;
        this.type = type;
        this.waitQueue = waitQueue;
        this.mainPerson = null;
        this.floor = 1;
        this.direction = 1;
        this.maxfloor = 20;
        this.minfloor = 1;
        this.speedOpen = 200;
        this.speedClose = 200;
        switch (type) {
            case "A":
                this.elevatorQueue = new ElevatorQueue(8);
                this.speedMove = 600;
                break;
            case "B":
                this.elevatorQueue = new ElevatorQueue(6);
                this.speedMove = 400;
                break;
            case "C":
                this.elevatorQueue = new ElevatorQueue(4);
                this.speedMove = 200;
                break;
            default:
                break;
        }
    }

    @Override
    public void run() {
        synchronized (waitQueue) {
            if (waitQueue.getArrivePattern() == null) {
                try {
                    waitQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.strategy = StrategyFactory.getNew(waitQueue, type);
        }
        while (true) {
            synchronized (waitQueue) {
                //任何队列没有请求且结束输入时结束线程
                if (waitQueue.isEnd() && waitQueue.isEmpty() && elevatorQueue.isEmpty()) {
                    break;
                }
                //任何队列没有可以搭乘的请求时wait，即如果等待队列有其他电梯的请求，该电梯也要wait
                if (waitQueue.isEmptyForElevator(type) && elevatorQueue.isEmpty()) {
                    try {
                        waitQueue.wait();
                        //continue保证如果是被remove或close唤醒，重新判断是否结束线程
                        continue;
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
            //没有可以搭乘的请求时return（下一循环wait）
            synchronized (waitQueue) {
                if (waitQueue.isEmptyForElevator(type)) {
                    return;
                }
                //选择主请求
                mainPerson = strategy.getMainPerson();
                //如果换向，即换向，注意这个方法仅在选择主请求或主请求进电梯后使用，电梯内换主请求需要用另一个方法
                changeDirectionWhenGetMainPerson();
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
            //注意，因为有换乘可能不是到1楼
            moveTo(this.floor + this.direction);
        }
        //主请求下车，捎带下车
        open();
        getOutMainPerson();
        getOutSubPerson();
        //电梯内换主请求，如果有换乘可能会执行
        changeMainPersonInElevator();
        close();
    }

    private void dealMorning() {
        if (mainPerson == null) {
            //此时电梯为空
            //没有可以换乘的请求时return(下一循环wait)
            synchronized (waitQueue) {
                if (waitQueue.isEmptyForElevator(type)) {
                    return;
                }
                //选择主请求
                mainPerson = strategy.getMainPerson();
                //换向
                changeDirectionWhenGetMainPerson();
            }
            //移动至主请求FROM层
            moveTo(mainPerson.getPersonRequest().getFromFloor());
            //主请求上车，捎带上车
            open();
            getInMainPerson();
            //morning模式，特殊捎带，能上车就上车
            while (!elevatorQueue.isFull()) {
                synchronized (waitQueue) {
                    //输入结束就无需等待，直接上车
                    if (waitQueue.isEnd() || this.floor != 1) {
                        getInSubPerson();
                        break;
                    }
                    //如果输入没有结束，就等待，让尽可能多的人上车
                    if (waitQueue.isEmptyForElevator(type)) {
                        try {
                            waitQueue.wait();
                            //continue保证被remove或close唤醒，重新判断是否结束输入
                            continue;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    getInSubPerson();
                }
            }
            close();
        }
        //移动至主请求TO层
        while (this.floor != mainPerson.getPersonRequest().getToFloor()) {
            //如果有捎带上下车，则捎带上下车
            checkTakeAndOut();
            //注意，因为有换乘可能不是从1楼出发
            moveTo(this.floor + this.direction);
        }
        //主请求、捎带下车
        open();
        getOutMainPerson();
        getOutSubPerson();
        //电梯内换主请求，有换乘可能执行
        changeMainPersonInElevator();
        close();
    }

    private void dealRandom() {
        if (mainPerson == null) {
            //此时电梯为空
            //没有请求时return(下一循环wait)
            synchronized (waitQueue) {
                if (waitQueue.isEmptyForElevator(type)) {
                    return;
                }
                //选择主请求
                mainPerson = ((Random) strategy).getMainPerson(this.floor, this.direction);
                //换向
                changeDirectionWhenGetMainPerson();
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
        //电梯内换主请求，有换乘时可能执行
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

    private void changeDirectionWhenMainPersonInElevator() {
        if (floor < mainPerson.getPersonRequest().getToFloor()) {
            direction = 1;
        } else if (floor > mainPerson.getPersonRequest().getToFloor()) {
            direction = -1;
        }
    }

    private void changeDirectionWhenGetMainPerson() {
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
        mainPerson.setMainPersonFlag(0);
        changeDirectionWhenGetMainPerson();
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
            //因为有了换乘，记得换向
            changeDirectionWhenMainPersonInElevator();
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
        if (person.isAvailableForElevator(type)) {
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
        synchronized (waitQueue) {
            checkChangeWhenIn(person);
            waitQueue.removePerson(person);
            waitQueue.notifyAll();
        }
        elevatorQueue.addPerson(person);
        PersonRequest p = person.getPersonRequest();
        TimableOutput.println("IN-" + p.getPersonId() + "-" + p.getFromFloor() +
                "-" + this.id);
    }

    private void getOut(Person person) {
        elevatorQueue.removePerson(person);
        PersonRequest p = person.getPersonRequest();
        TimableOutput.println("OUT-" + p.getPersonId() + "-" + p.getToFloor() +
                "-" + this.id);
        checkChangeWhenOut(person);
    }

    private void checkChangeWhenIn(Person person) {
        //主请求上车的时候就把换乘请求加进等待队列，防止等待的电梯结束线程
        synchronized (waitQueue) {
            if (type.equals("B")) {
                PersonRequest p = person.getPersonRequest();
                if (person.isChangeForB()) {
                    PersonRequest newP = new PersonRequest(
                            p.getFromFloor(), p.getToFloor() - 1, p.getPersonId());
                    person.setPersonRequest(newP);
                    PersonRequest subP = new PersonRequest(
                            p.getToFloor() - 1, p.getToFloor(), p.getPersonId());
                    createSubPerson(subP);
                }
            } else if (type.equals("C")) {
                PersonRequest p = person.getPersonRequest();
                if (person.isChangeForC()) {
                    if (p.getFromFloor() <= 3) {
                        if (p.getToFloor() >= 15) {
                            PersonRequest newP = new PersonRequest(
                                    p.getFromFloor(), 19, p.getPersonId());
                            person.setPersonRequest(newP);
                            PersonRequest subP = new PersonRequest(
                                    19, p.getToFloor(), p.getPersonId());
                            createSubPerson(subP);
                        } else if (p.getFromFloor() != 3) {
                            PersonRequest newP = new PersonRequest(
                                    p.getFromFloor(), 3, p.getPersonId());
                            person.setPersonRequest(newP);
                            PersonRequest subP = new PersonRequest(
                                    3, p.getToFloor(), p.getPersonId());
                            createSubPerson(subP);
                        }
                    } else if (p.getFromFloor() >= 18) {
                        if (p.getToFloor() <= 6) {
                            PersonRequest newP = new PersonRequest(
                                    p.getFromFloor(), 3, p.getPersonId());
                            person.setPersonRequest(newP);
                            PersonRequest subP = new PersonRequest(
                                    3, p.getToFloor(), p.getPersonId());
                            createSubPerson(subP);
                            //18层换到19层还涉及换向的问题，先不换了
                        } else if (p.getFromFloor() == 20) {
                            PersonRequest newP = new PersonRequest(
                                    p.getFromFloor(), 19, p.getPersonId());
                            person.setPersonRequest(newP);
                            PersonRequest subP = new PersonRequest(
                                    19, p.getToFloor(), p.getPersonId());
                            createSubPerson(subP);
                        }
                    }
                }
            }
        }
    }

    private void createSubPerson(PersonRequest subP) {
        synchronized (waitQueue) {
            Person subPerson = new Person(subP);
            subPerson.setMainPersonFlag(1);
            waitQueue.addPerson(subPerson);
            //这个时候notify了也没用，mainPersonFlag为1，无法被选为主请求或捎带
        }
    }

    private void checkChangeWhenOut(Person person) {
        synchronized (waitQueue) {
            for (int i = 0; i < waitQueue.getPersons().size(); i++) {
                PersonRequest p = waitQueue.getPersons().get(i).getPersonRequest();
                if (p.getPersonId() == person.getPersonRequest().getPersonId()) {
                    waitQueue.getPersons().get(i).setMainPersonFlag(0);
                    waitQueue.notifyAll();
                }
            }
        }
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
