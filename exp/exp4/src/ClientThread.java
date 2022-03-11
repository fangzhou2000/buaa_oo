import java.util.Random;

public class ClientThread extends Thread {
    private volatile boolean stopped;
    private final Channel channel;
    private static final Random RANDOM = new Random();

    public ClientThread(String name, Channel channel) {
        super(name);
        stopped = false;
        this.channel = channel;
    }

    public void run() {
        channel.checkIn();
        for (int i = 0; !stopped; i++) {
            Request request = new Request(getName(), i);
            try {
                channel.putRequest(request);
                Thread.sleep(RANDOM.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (stopped) {
            System.out.println("[Client " + getName() + "] Stopped.");
        }
        channel.checkOut();
    }

    // To be completed
    public void stopThread() {
        // TODO:
        stopped = true;
    }
}