import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class InputThread implements Runnable {
    private static final boolean DEBUG = true;
    private WaitQueue waitQueue;

    public InputThread(WaitQueue waitQueue) {
        this.waitQueue = waitQueue;
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
            PersonRequest request = elevatorInput.nextPersonRequest();
            synchronized (waitQueue) {
                if (request == null) {
                    waitQueue.close();
                    waitQueue.notifyAll();
                    break;
                } else {
                    waitQueue.addRequest(request);
                    waitQueue.notifyAll();
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
