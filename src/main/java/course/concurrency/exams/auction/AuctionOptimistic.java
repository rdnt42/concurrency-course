package course.concurrency.exams.auction;

public class AuctionOptimistic implements Auction {

    private Notifier notifier;

    public AuctionOptimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private volatile Bid latestBid;

    public boolean propose(Bid bid) {
        Bid previousBid = latestBid;
        long currentPrice = bid.getPrice();

        if (latestBid == null && currentPrice == bid.getPrice()) {
            latestBid = bid;

            return true;
        }

        while (true) {
            if (currentPrice > latestBid.getPrice()) {
                latestBid = bid;

                if (currentPrice != bid.getPrice()) {
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
