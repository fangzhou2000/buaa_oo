import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        WaitQueue waitQueue = new WaitQueue();
        Elevator elevator = new Elevator();
        Scheduler scheduler = new Scheduler(waitQueue, elevator);
        InputThread inputThread = new InputThread(waitQueue);
        new Thread(inputThread).start();
        new Thread(scheduler).start();
        new Thread(elevator).start();
    }
}
