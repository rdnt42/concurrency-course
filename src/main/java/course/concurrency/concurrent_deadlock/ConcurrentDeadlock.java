package course.concurrency.concurrent_deadlock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

/**
 * Created with IntelliJ IDEA.
 * User: marowak
 * Date: 30.10.2022
 * Time: 18:34
 */
@SpringBootApplication
public class ConcurrentDeadlock {
    @Autowired
    private DeadLockService deadLockService;

    @EventListener(ApplicationReadyEvent.class)
    public void actionAfterStartup() {
        deadLockService.alwaysAdd();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConcurrentDeadlock.class, args);
    }
}
