import java.util.ArrayList;

public class MainClass {
    public static void main(String[] args) {
        WaitQueue waitQueue = new WaitQueue();

        ArrayList<ProcessingQueue> processingQueues = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ProcessingQueue parallelQueue = new ProcessingQueue(i);
            processingQueues.add(parallelQueue);
            Process process = new Process(parallelQueue, i, waitQueue);
            process.start();
        }

        Scheduler schedule = new Scheduler(waitQueue, processingQueues);
        schedule.start();

        InputThread inputThread = new InputThread(waitQueue);
        inputThread.start();
    }
}