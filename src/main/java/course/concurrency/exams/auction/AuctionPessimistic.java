package course.concurrency.exams.auction;

public class AuctionPessimistic implements Auction {

    private Notifier notifier;
    private final Object lock = new Object();

    public AuctionPessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private Bid latestBid;

    public boolean propose(Bid bid) {
        if (latestBid == null) {
            synchronized (lock) {
                if (latestBid == null) {
                    latestBid = bid;

                    return true;
                }
            }
        }

        if (bid.getPrice() > latestBid.getPrice()) {
            synchronized (lock) {
                if (bid.getPrice() > latestBid.getPrice()) {
                    notifier.sendOutdatedMessage(latestBid);
                    latestBid = bid;

                    return true;
                }
            }
        }

        return false;
    }

    public synchronized Bid getLatestBid() {
        return latestBid;
    }
}
