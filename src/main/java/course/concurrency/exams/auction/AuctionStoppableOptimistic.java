package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private Notifier notifier;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private final AtomicReference<Bid> latestBid = new AtomicReference<>(new Bid(0L, 0L, 0L));
    private final AtomicBoolean isAuctionEnded = new AtomicBoolean(false);

    public boolean propose(Bid bid) {
        if (isAuctionEnded.get()) {
            return false;
        }

        do {
            if (bid.getPrice() <= latestBid.get().getPrice()) {
                return false;
            }
        } while (!isAuctionEnded.get() && !latestBid.compareAndSet(latestBid.get(), bid));

        notifier.sendOutdatedMessage(latestBid.get());

        return true;
    }

    public Bid getLatestBid() {
        return latestBid.get();
    }

    public Bid stopAuction() {
        isAuctionEnded.set(true);

        return latestBid.get();
    }
}
