package uk.gov.dwp.uc.pairtest;

import thirdparty.paymentgateway.TicketPaymentService;
import thirdparty.paymentgateway.TicketPaymentServiceImpl;
import thirdparty.seatbooking.SeatReservationService;
import thirdparty.seatbooking.SeatReservationServiceImpl;
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
        if (accountId == null || accountId <= 0) {
            throw new InvalidPurchaseException("Invalid account ID");
        }

        if (ticketTypeRequests == null || ticketTypeRequests.length == 0) {
            throw new InvalidPurchaseException("At least one ticket type must be specified.");
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
        if (totalTickets <= 0 || totalTickets > MAX_TICKETS) {
            throw new InvalidPurchaseException(totalTickets <=0 ? "Atleast one ticket must be purchased" : "Cannot purchase more than 25 tickets at a time.");
        }

        // Validate presence of Adult ticket
        if (!hasAdultTicket) {
            throw new InvalidPurchaseException("Child and Infant tickets cannot be purchased without an Adult ticket.");
        }

        // Make payment
        paymentService.makePayment(accountId, totalAmount);

        // Reserve seats
        reservationService.reserveSeat(accountId, totalSeats);
    }

//    public static void main(String[] args) {
//        TicketPaymentService paymentService = new TicketPaymentServiceImpl();
//        SeatReservationService reservationService = new SeatReservationServiceImpl();
//
//        TicketService ticketService = new TicketServiceImpl(paymentService, reservationService);
//
//        TicketTypeRequest ticketRequestInfant = new TicketTypeRequest(TicketTypeRequest.TicketType.INFANT, 3);
//        TicketTypeRequest ticketRequestAdult = new TicketTypeRequest(TicketTypeRequest.TicketType.ADULT, 20);
//        TicketTypeRequest ticketRequestChild = new TicketTypeRequest(TicketTypeRequest.TicketType.CHILD, 3);
//
//        ticketService.purchaseTickets(null, null);
//    }
}
