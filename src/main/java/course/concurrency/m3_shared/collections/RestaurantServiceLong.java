package course.concurrency.m3_shared.collections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RestaurantServiceLong extends RestaurantService {

    private final ConcurrentHashMap<String, Long> statLong = new ConcurrentHashMap<>();
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
        // Increment happens inside synchronized block so it's possible to use plain long value
        // In most cases workload is distributed across different keys, so it's ok to use this approach

        // Synchronized block becomes a bottleneck
        // when 2+ concurrent threads are intensely working with the same key
        // Situation which happens in test is actually rare
        statLong.merge(restaurantName, 1L, (k,v) -> k + 1);
    }

    public Set<String> printStat() {
        return statLong.entrySet().stream()
                .map(e -> e.getKey() + " - " + e.getValue())
                .collect(Collectors.toSet());
    }

}
