package course.concurrency.exams.auction;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Notifier {
    private final ExecutorService executorService = Executors.newFixedThreadPool(72);

    public void sendOutdatedMessage(Bid bid) {
        Runnable task = this::imitateSending;

        executorService.execute(task);
    }

    private void imitateSending() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
    }

    public void shutdown() {}
}
