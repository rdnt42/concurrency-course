package course.concurrency.exams.auction;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class AuctionStoppableOptimistic implements AuctionStoppable {

    private Notifier notifier;

    public AuctionStoppableOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private final AtomicMarkableReference<Bid> latestBid = new AtomicMarkableReference<>(new Bid(0L, 0L, 0L), false);

    public boolean propose(Bid bid) {
        if (latestBid.isMarked()) {
            return false;
        }

        do {
            if (bid.getPrice() <= latestBid.getReference().getPrice()) {
                return false;
            }
        } while (!latestBid.isMarked() && !latestBid.compareAndSet(latestBid.getReference(), bid, false, false));

        notifier.sendOutdatedMessage(latestBid.getReference());

        return true;
    }

    public Bid getLatestBid() {
        return latestBid.getReference();
    }

    public Bid stopAuction() {
        latestBid.set(latestBid.getReference(), true);

        return latestBid.getReference();
    }
}
