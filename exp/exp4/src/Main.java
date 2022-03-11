public class Main {
    public static void main(String[] args) {
        Channel channel = new Channel(5); // 工人的个数
        ClientThread alice = new ClientThread("Alice", channel);
        alice.start();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        alice.stopThread();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClientThread bobby = new ClientThread("Bobby", channel);
        ClientThread chris = new ClientThread("Chris", channel);
        ClientThread vince = new ClientThread("Vince", channel);
        bobby.start();
        chris.start();
        vince.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bobby.stopThread();
        chris.stopThread();
        vince.stopThread();
    }
}