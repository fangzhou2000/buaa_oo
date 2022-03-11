import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

public class InputThread implements Runnable {
    private static final boolean DEBUG = true;
    private WaitQueue waitQueue;
    private ArrayList<Elevator> elevators;

    public InputThread(WaitQueue waitQueue, ArrayList<Elevator> elevators) {
        this.waitQueue = waitQueue;
        this.elevators = elevators;
    }

    @Override
    public void run() {
        // 实例化一对管道流
        PipedOutputStream myPipeOut = new PipedOutputStream();
        PipedInputStream myPipeIn = new PipedInputStream();
        // 将二者连接起来, PipedOutputStream的connect方法会抛出IOException
        try {
            myPipeOut.connect(myPipeIn);
        } catch (IOException e) {
            throw new AssertionError(e); // Never happen
        }
        ElevatorInput elevatorInput;
        if (DEBUG) { // 调试开关, 提交评测时务必关掉它！
            elevatorInput = new ElevatorInput(myPipeIn);
        } else {
            elevatorInput = new ElevatorInput(System.in);
        }
        new Thread(new DebugInput(myPipeOut)).start();

        //ElevatorInput elevatorInput = new ElevatorInput(System.in);
        String arrivePattern = elevatorInput.getArrivingPattern();
        synchronized (waitQueue) {
            waitQueue.setArrivePattern(arrivePattern);
            waitQueue.notifyAll();
        }
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                synchronized (waitQueue) {
                    waitQueue.close();
                    waitQueue.notifyAll();
                }
                break;
            } else {
                if (request instanceof PersonRequest) {
                    PersonRequest personRequest = (PersonRequest) request;
                    Person p = new Person(personRequest);
                    synchronized (waitQueue) {
                        waitQueue.addPerson(p);
                        waitQueue.notifyAll();
                    }
                } else if (request instanceof ElevatorRequest) {
                    ElevatorRequest elevatorRequest = (ElevatorRequest) request;
                    synchronized (waitQueue) {
                        Elevator elevator = new Elevator(
                                Integer.parseInt(elevatorRequest.getElevatorId()), waitQueue);
                        if (elevators.size() <= 5) {
                            new Thread(elevator).start();
                            elevators.add(elevator);
                        }
                    }
                }
            }
        }
        try {
            elevatorInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
