package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.seatbooking.SeatReservationService;
import uk.gov.dwp.uc.pairtest.domain.TicketTypeRequest;
import uk.gov.dwp.uc.pairtest.exception.InvalidPurchaseException;

public class TicketServiceImpl implements TicketService{
    private final TicketPaymentService paymentService;
    private static final int MAX_TICKETS = 25;
    private final SeatReservationService reservationService;

    public TicketServiceImpl(TicketPaymentService paymentService, SeatReservationService reservationService) {
        this.paymentService = paymentService;
        this.reservationService = reservationService;
    }

    @Override
    public void purchaseTickets(Long accountId, TicketTypeRequest... ticketTypeRequests) throws InvalidPurchaseException {
//        Validate Account ID
        if (accountId <= 0 || accountId == null) {
            throw new InvalidPurchaseException("Invalid account ID");
        }

        // Validate ticket requests
        int totalTickets = 0;
        int totalAmount = 0;
        int totalSeats = 0;
        boolean hasAdultTicket = false;

        for (TicketTypeRequest request : ticketTypeRequests) {
            totalTickets += request.getNoOfTickets();

            switch (request.getTicketType()) {
                case INFANT:
                    totalAmount += 0; // Infants are free
                    break;
                case CHILD:
                    totalAmount += 15 * request.getNoOfTickets();
                    totalSeats += request.getNoOfTickets();
                    break;
                case ADULT:
                    totalAmount += 25 * request.getNoOfTickets();
                    totalSeats += request.getNoOfTickets();
                    hasAdultTicket = true;
                    break;
            }
        }

        // Validate total tickets
        if (totalTickets > MAX_TICKETS) {
            throw new InvalidPurchaseException("Cannot purchase more than 25 tickets at a time.");
        }

        // Validate presence of Adult ticket
        if (!hasAdultTicket && (totalSeats > 0)) {
            throw new InvalidPurchaseException("Child and Infant tickets cannot be purchased without an Adult ticket.");
        }

        // Make payment
        paymentService.makePayment(accountId, totalAmount);

        // Reserve seats
        reservationService.reserveSeat(accountId, totalSeats);
    }
}
