package course.concurrency.m2_async.cf.min_price;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PriceAggregator {
    ExecutorService executorService = Executors.newCachedThreadPool();

    private PriceRetriever priceRetriever = new PriceRetriever();

    public void setPriceRetriever(PriceRetriever priceRetriever) {
        this.priceRetriever = priceRetriever;
    }

    private Collection<Long> shopIds = Set.of(10l, 45l, 66l, 345l, 234l, 333l, 67l, 123l, 768l);

    public void setShops(Collection<Long> shopIds) {
        this.shopIds = shopIds;
    }

    public double getMinPrice(long itemId) {
        return getMinPriceCompletable(itemId);
    }

    private Double getMinPriceFromList(List<Double> prices) {
        if (prices.isEmpty()) {
            return Double.NaN;
        }

        return prices.stream()
                .filter(Objects::nonNull)
                .mapToDouble(p -> p)
                .min()
                .orElseThrow(NoSuchElementException::new);
    }

    private double getMinPriceCompletable(long itemId) {
        List<CompletableFuture<Double>> futures = shopIds.stream()
                .map(shopId -> CompletableFuture.supplyAsync(() -> priceRetriever.getPrice(itemId, shopId),
                                executorService)
                        .handle((s, t) -> s))
                .collect(Collectors.toList());

        CompletableFuture<List<Double>> completableFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .completeOnTimeout(null, 2500, TimeUnit.MILLISECONDS)
                .thenApply(v -> futures.stream()
                        .filter(CompletableFuture::isDone)
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));

        return completableFuture
                .thenApply(this::getMinPriceFromList)
                .join();
    }

    private Double getMinPriceCallable(long itemId) {
        List<Callable<Double>> tasks = new ArrayList<>();
        for (Long shopId : shopIds) {
            Callable<Double> callable = () -> priceRetriever.getPrice(itemId, shopId);
            tasks.add(callable);
        }

        List<Future<Double>> resultsFutures;
        try {
            resultsFutures = executorService.invokeAll(tasks, 2500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println("Invoke error " + e.getMessage());

            return Double.NaN;
        }

        List<Double> results = new ArrayList<>();
        for (Future<Double> future : resultsFutures) {
            if (future.isDone()) {
                try {
                    Double result = future.get();
                    results.add(result);
                } catch (Exception e) {
                    System.out.println("Future error " + e.getMessage());
                }
            }
        }

        return getMinPriceFromList(results);
    }
}
