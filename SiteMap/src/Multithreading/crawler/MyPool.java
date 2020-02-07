package Multithreading.crawler;
import java.util.concurrent.ForkJoinPool;

public class MyPool extends ForkJoinPool {
    private Thread thread;

    public MyPool(int parallelism) {
        super(parallelism);
    }

    void startPool(LinksFinderTask task) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LinksFinderTask.setFinishedTask(false);
                invoke(task);
                shutdown();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}
