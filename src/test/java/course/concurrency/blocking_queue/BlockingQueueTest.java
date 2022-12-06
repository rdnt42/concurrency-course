package course.concurrency.blocking_queue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 05.12.2022
 * Time: 22:36
 */
class BlockingQueueTest {
    private final int THREADS_COUNT = 72;
    private final ExecutorService executorService = Executors.newFixedThreadPool(THREADS_COUNT);

    @Test
    void enqueueBusyQueue() throws InterruptedException {
        int maxCount = 5;
        BlockingQueue<Integer> queue = new BlockingQueue<>(maxCount);
        BlockingQueue<Integer> mockedQueue = Mockito.spy(queue);
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executorService.submit(() -> mockedQueue.enqueue(finalI));
        }

        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(maxCount, mockedQueue.size());
        int totalCount = THREADS_COUNT + maxCount;
        verify(mockedQueue, times(totalCount)).enqueue(anyInt());
    }

    @Test
    void dequeueEmpty() throws InterruptedException {
        BlockingQueue<Integer> queue = new BlockingQueue<>(10);
        BlockingQueue<Integer> mockedQueue = Mockito.spy(queue);
        for (int i = 0; i < 5; i++) {
            executorService.submit(mockedQueue::dequeue);
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        assertEquals(0, mockedQueue.size());
    }

    @Test
    void enqueueWithDeque() throws InterruptedException {
        BlockingQueue<Integer> queue = new BlockingQueue<>(10);
        BlockingQueue<Integer> mockedQueue = Mockito.spy(queue);
        for (int i = 0; i < 6; i++) {
            int finalI = i;
            executorService.submit(() -> mockedQueue.enqueue(finalI));
        }
        for (int i = 0; i < 5; i++) {
            executorService.submit(mockedQueue::dequeue);
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        assertEquals(1, mockedQueue.size());
    }

    @Test
    void enqueueWithDequeFullQueue() throws InterruptedException {
        BlockingQueue<Integer> queue = new BlockingQueue<>(5);
        BlockingQueue<Integer> mockedQueue = Mockito.spy(queue);
        for (int i = 0; i < 7; i++) {
            int finalI = i;
            executorService.submit(() -> mockedQueue.enqueue(finalI));
        }
        for (int i = 0; i < 5; i++) {
            executorService.submit(mockedQueue::dequeue);
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        assertEquals(2, mockedQueue.size());
    }

    @Test
    void enqueueAfterDeque() throws InterruptedException {
        BlockingQueue<Integer> queue = new BlockingQueue<>(5);
        BlockingQueue<Integer> mockedQueue = Mockito.spy(queue);
        for (int i = 0; i < 5; i++) {
            executorService.submit(mockedQueue::dequeue);
        }

        for (int i = 0; i < 7; i++) {
            int finalI = i;
            executorService.submit(() -> mockedQueue.enqueue(finalI));
        }

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        assertEquals(2, mockedQueue.size());
    }
}