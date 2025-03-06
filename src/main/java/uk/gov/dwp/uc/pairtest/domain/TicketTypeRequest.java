package uk.gov.dwp.uc.pairtest.domain;

public class TicketTypeRequest {

    private final int noOfTickets;
    private final TicketType ticketType;

    public TicketTypeRequest(TicketType type, int noOfTickets) {
        this.ticketType = type;
        this.noOfTickets = noOfTickets;
    }

    public int getNoOfTickets() {
        return noOfTickets;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public enum TicketType {
        ADULT, CHILD , INFANT
    }

}