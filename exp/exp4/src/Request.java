import java.util.Random;

public class Request {
    private final String name; // 委托者
    private final int number; // 请求的编号
    private static final Random RANDOM = new Random();

    public Request(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public void execute() {
        System.out.println(Thread.currentThread().getName() + " executes " + this);
        try {
            Thread.sleep(RANDOM.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return "[ Request from " + name + " No." + number + " ]";
    }
}