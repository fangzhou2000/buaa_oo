public class Channel {
    private static final int QUEUE_SIZE = 256;
    private final Request[] requestQueue = new Request[QUEUE_SIZE];
    private int tail = 0; // 下次putRequest的位置
    private int head = 0; // 下次takeRequest的位置

    private volatile boolean stopped = true;
    private final Worker[] workers;

    private final Object checkingInMutex = new Object();
    private volatile int checkedInClients = 0;

    public Channel(int threads) {
        workers = new Worker[threads];

        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("Worker-" + i);
        }
    }

    public void checkIn() {
        synchronized (checkingInMutex) {
            checkedInClients++;
            if (checkedInClients == 1) {
                startAllWorkers();
            }
        }
    }

    public void checkOut() {
        synchronized (checkingInMutex) {
            checkedInClients--;
            if (checkedInClients == 0) {
                stopAllWorkers();
            }
        }
    }

    private void startAllWorkers() {
        synchronized (requestQueue) {
            stopped = false;
            for (Worker worker : workers) {
                worker.start();
            }
        }
    }

    // To be completed
    private void stopAllWorkers() {
        // TODO:
        synchronized (requestQueue) {
            stopped = true;
            requestQueue.notifyAll();
        }
    }

    public void putRequest(Request request) {
        synchronized (requestQueue) {
            while (true) {
                if (!full()) {
                    requestQueue[tail] = request;
                    tail = (tail + 1) % requestQueue.length;
                    requestQueue.notifyAll();
                    break;
                }
                try {
                    requestQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Request takeRequest() {
        Request request = requestQueue[head];
        head = (head + 1) % requestQueue.length;
        requestQueue.notifyAll();
        return request;
    }

    private boolean full() {
        synchronized (requestQueue) {
            return (head == (tail + 1) % requestQueue.length);
        }
    }

    private boolean empty() {
        synchronized (requestQueue) {
            return (head == tail);
        }
    }

    private class Worker implements Runnable {
        private final String name;
        private volatile boolean running = false;

        public Worker(String name) {
            this.name = name;
        }

        public synchronized void start() {
            if (!running) {
                running = true;
                Thread thread =  new Thread(this);
                thread.setName(thread.getName() + ":" + name);
                thread.start();
            }
        }

        public void run() {
            try {
                System.out.println("[Worker " + name + "] Started.");
                while (true) {
                    Request request;
                    synchronized (requestQueue) {
                        while (!stopped && empty()) {
                            requestQueue.wait();
                        }
                        if (stopped && empty()) {
                            return;
                        }
                        request = takeRequest();
                    }
                    request.execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                synchronized (this) {
                    running = false;
                }
                System.out.println("[Worker " + name + "] Stopped.");
            }
        }
    }
}