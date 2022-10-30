package course.concurrency.concurrent_deadlock;

import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 30.10.2022
 * Time: 18:35
 */
@Service
public class DeadLockService {
    ExecutorService executorService = Executors.newFixedThreadPool(100);
    private final ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap();

    public void alwaysAdd() {
        while (true) {
            Runnable task = () -> {
                System.out.println("alwaysAdd " + Thread.currentThread().getName());

                concurrentHashMap.compute("putKey", (key, value) ->
                    concurrentHashMap.put(key, 0));
            };

            executorService.execute(task);
        }
    }
}
