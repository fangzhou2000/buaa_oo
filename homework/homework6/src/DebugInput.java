import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DebugInput implements Runnable {

    private final OutputStream outputStream;

    public DebugInput(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void run() {
        File file = new File("input.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Timer timer = new Timer(); // 初始化一个定时器
        long currentMillis = System.currentTimeMillis();
        long maxMillis = 0; // 记录最后一条输入的时间
        while (scanner.hasNext()) {
            //读取格式:时间 请求/模式
            String string = scanner.next();
            Pattern pattern = Pattern.compile("\\[([\\d.]+)\\]([\\d-a-zA-Z]+)");
            Matcher matcher = pattern.matcher(string);
            long millis = 0;
            String input = "";
            if (matcher.find()) {
                millis = (long)(1000 * Double.parseDouble(matcher.group(1))); // 先读取时间
                input = matcher.group(2); // 读取一行输入
            } else {
                System.out.println("wrong");
            }
            maxMillis = millis; // 更新maxMillis
            String finalInput = input;
            timer.schedule(new TimerTask() { // 创建定时任务
                @Override
                public void run() {
                    try {
                        outputStream.write(finalInput.getBytes());
                        outputStream.write(0x0a); // 换行符
                        outputStream.flush(); // 强制刷新
                    } catch (IOException e) {
                        throw new AssertionError(e);
                    }
                }
            }, new Date(currentMillis + millis));
        }
        //关闭管道流以及关闭定时器，添加最后一个定时任务
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new AssertionError(e);
                }
                timer.cancel(); // 关闭定时器.
            }
        }, new Date(currentMillis + maxMillis + 100));
    }
}
