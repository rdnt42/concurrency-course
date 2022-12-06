package course.concurrency.blocking_queue;

import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 05.12.2022
 * Time: 22:14
 */
public class BlockingQueue<T> {
    private final List<T> queue = new LinkedList<>();
    private final int maxCount;

    Object lock = new Object();

    public BlockingQueue(int maxCount) {
        this.maxCount = maxCount;
    }

    public void enqueue(T value) {
        synchronized (lock) {
            while (maxCount == size()) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            queue.add(value);
            lock.notifyAll();
        }
    }

    public T dequeue() {
        synchronized (lock) {
            while (size() == 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            lock.notifyAll();

            return queue.remove(0);
        }
    }

    public int size() {
        synchronized (lock) {
            return queue.size();
        }
    }
}