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
        INFANT(0),   // Infant tickets are free
        CHILD(15),   // Child tickets cost £15
        ADULT(25);   // Adult tickets cost £25

        private final int price;
        TicketType(int price) {
            this.price = price;
        }
        public int getPrice() {
            return price;
        }
    }

}