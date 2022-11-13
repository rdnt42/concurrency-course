package course.concurrency.exams.auction;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private Notifier notifier;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private volatile Bid latestBid = new Bid(0L, 0L, 0L);
    private final Object lock = new Object();

    private volatile boolean isAuctionEnded = false;

    public boolean propose(Bid bid) {
        if (isAuctionEnded) {
            return false;
        }

        if (bid.getPrice() > latestBid.getPrice()) {
            synchronized (lock) {
                if (!isAuctionEnded && bid.getPrice() > latestBid.getPrice()) {
                    notifier.sendOutdatedMessage(latestBid);
                    latestBid = bid;

                    return true;
                }
            }
        }

        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }

    public synchronized Bid stopAuction() {
        isAuctionEnded = true;

        return latestBid;
    }
}
