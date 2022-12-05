package course.concurrency.m3_shared.collections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class RestaurantServiceAdder extends RestaurantService {

    private final ConcurrentHashMap<String, LongAdder> stat = new ConcurrentHashMap<>();
    private Map<String, Restaurant> restaurantMap = new ConcurrentHashMap<>() {{
        put("A", new Restaurant("A"));
        put("B", new Restaurant("B"));
        put("C", new Restaurant("C"));
    }};

    public Restaurant getByName(String restaurantName) {
        addToStat(restaurantName);
        return restaurantMap.get(restaurantName);
    }

    public void addToStat(String restaurantName) {
        // All synchronization happens inside LongAdder
        // Every thread has a fixed number of steps to update value
        // If there are more than 2 concurrent threads this approach provides better scalability
        stat.computeIfAbsent(restaurantName, rn -> new LongAdder())
                .increment();
    }
    public Set<String> printStat() {
        return stat.entrySet().stream()
                .map(e -> e.getKey() + " - " + e.getValue())
                .collect(Collectors.toSet());
    }

}
