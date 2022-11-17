package course.concurrency.m3_shared.collections;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class RestaurantService {

    private Map<String, Restaurant> restaurantMap = new ConcurrentHashMap<>() {{
        put("A", new Restaurant("A"));
        put("B", new Restaurant("B"));
        put("C", new Restaurant("C"));
    }};

    private ConcurrentHashMap<String, LongAdder> stat = new ConcurrentHashMap<>();

    public Restaurant getByName(String restaurantName) {
        addToStat(restaurantName);

        return restaurantMap.get(restaurantName);
    }

    public void addToStat(String restaurantName) {
        LongAdder requestCount = stat.computeIfAbsent(restaurantName, k -> new LongAdder());
        requestCount.increment();
    }

    public Set<String> printStat() {
        Set<String> stats = new HashSet<>();
        for (Map.Entry<String, LongAdder> statItem : stat.entrySet()) {
            stats.add(statItem.getKey() + " - " + statItem.getValue());
        }

        return stats;
    }
}
