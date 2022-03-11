import com.oocourse.TimableOutput;

import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        WaitQueue waitQueue = new WaitQueue();
        Elevator elevator1 = new Elevator(1, "A", waitQueue);
        Elevator elevator2 = new Elevator(2, "B", waitQueue);
        Elevator elevator3 = new Elevator(3, "C", waitQueue);
        ArrayList<Elevator> elevators = new ArrayList<>();
        elevators.add(elevator1);
        elevators.add(elevator2);
        elevators.add(elevator3);
        InputThread inputThread = new InputThread(waitQueue, elevators);
        new Thread(inputThread).start();
        new Thread(elevator1).start();
        new Thread(elevator2).start();
        new Thread(elevator3).start();
    }
}
