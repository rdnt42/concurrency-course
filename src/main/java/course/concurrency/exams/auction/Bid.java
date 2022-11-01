package course.concurrency.exams.auction;

public class Bid {
    private Long id; // ID Ð·Ð°ÑÐ²ÐºÐ¸
    private Long participantId; // ID ÑƒÑ‡Ð°ÑÑ‚Ð½Ð¸ÐºÐ°
    private Long price; // Ð¿Ñ€ÐµÐ´Ð»Ð¾Ð¶ÐµÐ½Ð½Ð°Ñ Ñ†ÐµÐ½Ð°

    public Bid(Long id, Long participantId, Long price) {
        this.id = id;
        this.participantId = participantId;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public Long getPrice() {
        return price;
    }
}
