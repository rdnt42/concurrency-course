package course.concurrency.exams.auction;

public class AuctionOptimistic implements Auction {

    private Notifier notifier;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private volatile Bid latestBid = new Bid(0L, 0L, 0L);
    private volatile Bid previousBid;

    public boolean propose(Bid bid) {
        previousBid = bid;

        while (true) {
            if (bid.getPrice() > latestBid.getPrice()) {
                latestBid = bid;

                if (previousBid != bid) {
                    latestBid = previousBid;

                    continue;
                }

                notifier.sendOutdatedMessage(latestBid);

                return true;
            }

            return false;
        }
    }

    public synchronized Bid getLatestBid() {
        return latestBid;
    }
}
